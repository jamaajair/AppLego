// typescript
import { useState, useEffect, useRef } from "react";
import { useNavigate, useParams } from "react-router-dom";
import type { PageConfig } from "../types/config";
import { api } from "../services/api";
import type { GenericFormResult } from "../types/hooks";

export default function useGenericEdit<T = Record<string, unknown>>(
    configPath?: string | null,
    dataUrl?: string | null,
    submitUrl?: string | null
): GenericFormResult {

    const [config, setConfig] = useState<PageConfig | null>(null);
    const [values, setValues] = useState<Record<string, unknown>>({});
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [submitting, setSubmitting] = useState(false);
    const [isEdit, setEdit] = useState(false);

    const initialValues = useRef<Record<string, unknown>>({});
    const navigate = useNavigate();
    const params = useParams();
    const id = params.id ?? null;

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

                if(!mounted)return;

                const json: PageConfig = await res.json();
                setConfig(json);
                setError(null);

            } catch (e: any) {
                if (e.name !== "AbortError") setError(String(e));
            } finally {
                if(mounted) setLoading(false);
            }
        };

        loadConfig();
        return () =>  {
            mounted = false;
            controller.abort();
        }

    }, [configPath]);

    useEffect(() => {
        if (!config || !dataUrl || !submitUrl) return;

        const controller = new AbortController();

        const loadData = async () => {
            try {
                setLoading(true);

                const res = await api.get(dataUrl, { signal: controller.signal });

                const dto = res?.data;

                if (!dto) {
                    setError("No data returned by backend");
                    return;
                }

                const mapped: Record<string, unknown> = {};

                config.fields.forEach((f) => {
                    const raw = dto[f.name];

                    if (raw && typeof raw === "object" && "code" in raw) {
                        mapped[f.name] = raw.code;
                        return;
                    }

                    mapped[f.name] = raw ?? (f.type === "date" ? null : "");
                });

                setValues(mapped);
                initialValues.current = mapped;
                setError(null);

            } catch (e: any) {
                if (e.name !== "AbortError") {
                    setError(String(e));
                }
            } finally {
                setLoading(false);
            }
        };

        loadData();
        return () => controller.abort();

    }, [config, dataUrl]);

    const handleChange =
        (name: string) =>
        (
            e: React.ChangeEvent<
                HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement
            >
        ) => {
            const target = e.target;
            let v: unknown = target.value;

            // DATE
            if (target instanceof HTMLInputElement && target.type === "date") {
                v = target.value === "" ? null : target.value;
            }

            // SELECT / MULTI
            if (target instanceof HTMLSelectElement) {
                if (target.multiple) {
                    const selected = Array.from(target.selectedOptions).map((o) => o.value);
                    v = selected.length ? selected : null;
                } else {
                    v = target.value === "" ? null : target.value;
                }
            }

            setValues((prev) => ({ ...prev, [name]: v }));
        };

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (!config) return;

        setSubmitting(true);
        setError(null);

        try {
            const payload = { ...values };

            const res = await api.patch(submitUrl, payload);

            if ([200, 201, 204].includes(res.status)) {
                navigate(0);
                return;
            }
            setError(`HTTP ${res.status}`);


        } catch (err: any) {
            if (err.name !== "AbortError") {
                setError(String(err));
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
        isEdit,
        submitting,
        handleChange,
        handleSubmit,
        handleReset,
        setValues,
        setEdit,
    } as GenericFormResult;
}
