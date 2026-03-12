import { Box, Stack, Typography } from "@mui/material";

type PageShellProps = {
    title: string;
    action?: React.ReactNode;
    children: React.ReactNode;
};

export default function PageShell({ title, action, children }: PageShellProps) {
    return (
        <Box
            sx={{
                maxWidth: 1200,
                mx: "auto",
                px: 3,
                py: 4,
            }}
        >
            {/* Header */}
            <Stack
                direction="row"
                alignItems="center"
                justifyContent="space-between"
                sx={{ mb: 3 }}
            >
                <Typography variant="h4" fontWeight={600}>
                    {title}
                </Typography>

                {action && <Box>{action}</Box>}
            </Stack>

            {/* Content */}
            <Box>
                {children}
            </Box>
        </Box>
    );
}