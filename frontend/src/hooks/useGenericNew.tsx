import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import type { PageConfig } from "../types/config";
import { api } from "../services/api";
import { getLabelByLanguage } from "../services/utils";

export default function useGenericNew(configPath?: string | null, submitUrl?: string | null) {
    const [config, setConfig] = useState<PageConfig | null>(null);
    const [values, setValues] = useState<Record<string, unknown>>({});
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [submitting, setSubmitting] = useState(false);

    const navigate = useNavigate();

    useEffect(() => {
        if (!configPath) {
            setLoading(false);
            return;
        }

        const controller = new AbortController();

        const load = async () => {
            try {
                setLoading(true);

                const res = await fetch(configPath, { signal: controller.signal });
                if (!res.ok) {
                    setError(`HTTP ${res.status}`);
                    return;
                }

                const json: PageConfig = await res.json();
                setConfig(json);

                const initial: Record<string, unknown> = {};
                json.fields.forEach(f => {
                    initial[f.name] = f.default ?? (f.type === "date" ? null : "");
                });
                setValues(initial);

            } catch (e) {
                if ((e as any)?.name !== "AbortError") setError(String(e));
            } finally {
                setLoading(false);
            }
        };

        load();
        return () => controller.abort();
    }, [configPath]);

    const handleChange = (name: string) =>
        (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
            const el = e.target;
            let val: unknown = el.value;

            // Handle date
            if (el instanceof HTMLInputElement && el.type === "date") {
                val = el.value === "" ? null : el.value;
            }

            // Handle select
            if (el instanceof HTMLSelectElement) {
                val = el.value === "" ? null : el.value;
            }

            setValues(prev => ({ ...prev, [name]: val }));
        };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!config) return;

        setSubmitting(true);

        try {
            for (const f of config.fields) {
                if (f.required && !values[f.name]) {
                    setError(`Field ${getLabelByLanguage(f)} is required.`);
                    return;
                }
            }

            if (submitUrl) {
                const res = await api.post(submitUrl, values);
                if (res.status === 200 || res.status === 201) {
                    navigate(`/user/dossiers/${res.data.data.ref}`);
                    return;
                }
                setError("Request failed: " + res.status);
            }
        } catch (err) {
            setError(String(err));
        } finally {
            setSubmitting(false);
        }
    };

    const handleReset = () => {
        setError(null);
        setValues({});
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
    };
}