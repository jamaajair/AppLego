export type GenericNewProps = {
    configPath: string;
    submitUrl?: string;
    cancelTo?: string;
    submitLabel?: string;
    onSuccessNavigateTo?: (res: unknown) => string | null | undefined;
}