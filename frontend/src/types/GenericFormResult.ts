export type GenericFormResult = {
  config: PageConfig | null;
  values: Record<string, unknown>;
  loading: boolean;
  error: string | null;
  submitting: boolean;
  isEdit: boolean;
  handleChange: (name: string) => (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => void;
  handleSubmit: (e: React.FormEvent) => Promise<void>;
  handleReset: () => void;
  setValues: (v: Record<string, unknown>) => void;
};