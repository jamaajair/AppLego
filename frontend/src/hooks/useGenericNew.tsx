import { useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import type { PageConfig } from "../types/config";
import type { GenericFormResult } from "../types/genericFormResult";
import { api } from "../services/api";
import { getLabelByLanguage } from "../services/utils";

type CreateResponse = never;

type UseGenericNewOptions = {
    configPath?: string | null;
    submitUrl?: string | null;

    onSuccessNavigateTo?: (res: CreateResponse) => string | null | undefined;
    onSuccess?: (res: CreateResponse) => void;

    validateRequired?: boolean;
};

export default function useGenericNew(
    configPath?: string | null,
    submitUrl?: string | null,
    options?: Omit<UseGenericNewOptions, "configPath" | "submitUrl">
): GenericFormResult {
    const navigate = useNavigate();

    const [config, setConfig] = useState<PageConfig | null>(null);
    const [values, setValues] = useState<Record<string, unknown>>({});
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [submitting, setSubmitting] = useState(false);

    const initialValues = useRef<Record<string, unknown>>({});

    const validateRequired = options?.validateRequired ?? true;

    useEffect(() => {
        if (!configPath) {
            setLoading(false);
            return;
        }

        const controller = new AbortController();
        let mounted = true;

        const loadConfig = async () => {
            try {
                setLoading(true);

                const res = await fetch(configPath, { signal: controller.signal });
                if (!res.ok) {
                    setError(`HTTP ${res.status}`);
                    return;
                }

                const json: PageConfig = await res.json();
                if (!mounted) return;

                setConfig(json);
                setError(null);

                const initial: Record<string, unknown> = {};

                (json.fields ?? []).forEach((f: any) => {
                    if (f.default !== undefined) {
                        initial[f.name] = f.default;
                        return;
                    }

                    if (f.type === "date") {
                        initial[f.name] = null;
                        return;
                    }

                    if (f.type === "select") {
                        initial[f.name] = f.multiple ? [] : null;
                        return;
                    }

                    initial[f.name] = "";
                });

                setValues(initial);
                initialValues.current = initial;
            } catch (e: any) {
                if (e?.name !== "AbortError") setError(String(e));
            } finally {
                if (mounted) setLoading(false);
            }
        };

        loadConfig();

        return () => {
            mounted = false;
            controller.abort();
        };
    }, [configPath]);

    const handleChange =
        (name: string) =>
            (
                e: React.ChangeEvent<
                    HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement
                >
            ) => {
                const target = e.target;
                let v: unknown = target.value;

                if (target instanceof HTMLInputElement && target.type === "date") {
                    v = target.value === "" ? null : target.value;
                }

                if (target instanceof HTMLSelectElement) {
                    if (target.multiple) {
                        const selected = Array.from(target.selectedOptions).map((o) => o.value);
                        v = selected;
                    } else {
                        v = target.value === "" ? null : target.value;
                    }
                }

                setValues((prev) => ({ ...prev, [name]: v }));
            };

    const parseBackendError = (err: any): string => {
        const msg =
            err?.response?.data?.message ??
            err?.response?.data?.error ??
            err?.message;

        const details = err?.response?.data?.details;

        if (details && typeof details === "object") {
            const firstKey = Object.keys(details)[0];
            const firstVal = (details as any)[firstKey];
            if (firstKey && firstVal) return `${firstKey}: ${String(firstVal)}`;
        }

        if (typeof msg === "string" && msg.trim().length) return msg;

        const status = err?.response?.status;
        if (status) return `Request failed (HTTP ${status})`;

        return "Request failed";
    };

    const isEmptyValue = (val: unknown) => {
        if (val === null || val === undefined) return true;
        if (typeof val === "string") return val.trim() === "";
        if (Array.isArray(val)) return val.length === 0;
        return false;
    };

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (!config) return;
        if (!submitUrl) return;

        setSubmitting(true);
        setError(null);

        try {
            if (validateRequired) {
                for (const f of config.fields ?? []) {
                    if (f.required && isEmptyValue(values[f.name])) {
                        setError(`${getLabelByLanguage(f)} is required.`);
                        return;
                    }
                }
            }

            const payload = { ...values };

            const res = await api.post(submitUrl, payload);

            options?.onSuccess?.(res);

            const target = options?.onSuccessNavigateTo?.(res);
            if (target) {
                navigate(target);
                return;
            }
        } catch (err: any) {
            if (err?.name !== "AbortError") {
                setError(parseBackendError(err));
            }
        } finally {
            setSubmitting(false);
        }
    };

    const handleReset = () => {
        setValues(initialValues.current);
        setError(null);
    };

    return {
        config,
        values,
        loading,
        error,
        submitting,
        handleChange,
        handleSubmit,
        handleReset,
        setValues,
    } as GenericFormResult;
}