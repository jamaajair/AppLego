import type { ChangeEvent, FormEvent } from "react";
import type { PageConfig } from "./config.ts";

export interface GenericFormProps {
    config: PageConfig | null;
    values: Record<string, unknown>;
    error: string | null;
    submitting: boolean;

    handleChange: (
        name: string
    ) => (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => void;

    handleSubmit: (e: FormEvent<HTMLFormElement>) => Promise<void>;
    handleReset: () => void;

    readOnly?: boolean;
}