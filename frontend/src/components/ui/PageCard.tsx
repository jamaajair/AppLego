import { Paper, Stack, Box, Typography, Divider } from "@mui/material";
import type {ReactNode} from "react";

type PageCardProps = {
    title?: string;
    subtitle?: string;
    rightSlot?: ReactNode;
    children: ReactNode;
};

export default function PageCard({ title, subtitle, rightSlot, children }: PageCardProps) {
    return (
        <Paper elevation={1} sx={{ p: 3, borderRadius: 3 }}>
            <Stack spacing={2}>
                {(title || subtitle || rightSlot) && (
                    <>
                        <Box
                            sx={{
                                display: "flex",
                                alignItems: "flex-start",
                                justifyContent: "space-between",
                                gap: 2
                            }}
                        >
                            <Box sx={{ minWidth: 0 }}>
                                {title && (
                                    <Typography variant="h6">
                                        {title}
                                    </Typography>
                                )}
                                {subtitle && (
                                    <Typography variant="body2" color="text.secondary">
                                        {subtitle}
                                    </Typography>
                                )}
                            </Box>
                            {rightSlot && <Box>{rightSlot}</Box>}
                        </Box>
                        <Divider />
                    </>
                )}

                {/* 👇 CENTRAGE PROPRE */}
                <Box
                    sx={{
                        width: "100%",
                        maxWidth: 1100,
                        mx: "auto"
                    }}
                >
                    {children}
                </Box>
            </Stack>
        </Paper>
    );
}
