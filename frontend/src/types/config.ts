export type ColumnConfig = {
    field: string;
    headerName: string;
    flex?: number;
    labelByLanguage?: boolean;
    valuePath?: string;
};

export type FieldConfig = {
    name: string;
    label: string;
    type?: 'text' | 'textarea' | 'date' | 'select';
    options?: string[];
    required?: boolean;
    default?: unknown;
};

export type PageConfig = {
    title?: string;
    fields: FieldConfig[];
    submitUrl?: string;
};

export type Props = {
    configPath?: string;
    submitUrl?: string;
};

export type CodeDto = {
    code: string;
    labelFr: string;
    labelEn: string;
    labelNl: string;
    labelDe: string;
};