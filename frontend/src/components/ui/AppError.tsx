import { Alert, Box, Button } from "@mui/material";

type AppErrorProps = {
    message?: string;
    onRetry?: () => void;
    centered?: boolean;
};

export default function AppError({
                                     message = "Something went wrong.",
                                     onRetry,
                                     centered = false,
                                 }: AppErrorProps) {
    const content = (
        <Alert
            severity="error"
            action={
                onRetry && (
                    <Button color="inherit" size="small" onClick={onRetry}>
                        Retry
                    </Button>
                )
            }
        >
            {message}
        </Alert>
    );

    if (!centered) return content;

    return (
        <Box
            sx={{
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                minHeight: 200,
            }}
        >
            {content}
        </Box>
    );
}
