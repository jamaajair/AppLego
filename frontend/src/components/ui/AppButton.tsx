import Button, { type ButtonProps } from "@mui/material/Button";
import CircularProgress from "@mui/material/CircularProgress";

export type AppButtonIntent = "primary" | "secondary" | "danger" | "ghost";

export type AppButtonProps = Omit<ButtonProps, "color" | "variant"> & {
    intent?: AppButtonIntent;
    loading?: boolean;
};

function mapIntent(intent: AppButtonIntent): Pick<ButtonProps, "color" | "variant"> {
    switch (intent) {
        case "primary":
            return { variant: "contained", color: "primary" };
        case "secondary":
            return { variant: "outlined", color: "primary" };
        case "danger":
            return { variant: "contained", color: "error" };
        case "ghost":
            return { variant: "text", color: "primary" };
    }
}

export function AppButton({
                              intent = "primary",
                              loading = false,
                              disabled,
                              children,
                              startIcon,
                              endIcon,
                              ...props
                          }: AppButtonProps) {
    const mapped = mapIntent(intent);

    return (
        <Button
            {...mapped}
            {...props}
            disabled={disabled || loading}
            startIcon={
                loading ? <CircularProgress size={18} color="inherit" /> : startIcon
            }
            endIcon={loading ? undefined : endIcon}
        >
            {children}
        </Button>
    );
}
