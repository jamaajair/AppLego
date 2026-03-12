import type { Dispatch, SetStateAction, ChangeEvent, FormEvent } from "react";
import type { PageConfig } from "./config.ts";

export type GenericFormResult = {
    config: PageConfig | null;
    values: Record<string, unknown>;
    loading: boolean;
    error: string | null;
    submitting: boolean;
    isEdit: boolean;

    handleChange: (
        name: string
    ) => (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => void;

    handleSubmit: (e: FormEvent<HTMLFormElement>) => Promise<void>;
    handleReset: () => void;

    setValues: Dispatch<SetStateAction<Record<string, unknown>>>;
    setEdit?: Dispatch<SetStateAction<boolean>>;
};