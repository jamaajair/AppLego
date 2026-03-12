import * as React from "react";
import {
    DataGrid,
    type GridRowParams,
    type GridValidRowModel,
    type GridPaginationModel,
} from "@mui/x-data-grid";
import type { GridColDef } from "@mui/x-data-grid";
import Box from "@mui/material/Box";
import Paper from "@mui/material/Paper";
import Alert from "@mui/material/Alert";
import Skeleton from "@mui/material/Skeleton";

interface GenericDataGridProps<T extends GridValidRowModel> {
    rows: readonly T[];
    columns: GridColDef[];
    loading?: boolean;
    error?: string | null;
    getRowId?: (row: T) => string | number;
    onRowClick?: (params: GridRowParams<T>) => void;
    height?: number;
    rowHeight?: number;
    headerHeight?: number;
    toolbar?: boolean;
    pageSizeOptions?: number[];
    initialPageSize?: number;
}

const FIXED_PAGE_SIZE = 40;

function GenericDataGrid<T extends GridValidRowModel>({
                                                          rows,
                                                          columns,
                                                          loading = false,
                                                          error = null,
                                                          getRowId,
                                                          onRowClick,
                                                          height = 640,
                                                          rowHeight = 24,
                                                          headerHeight = 32,
                                                          toolbar = false,
                                                          pageSizeOptions = [FIXED_PAGE_SIZE],
                                                      }: GenericDataGridProps<T>) {
    const defaultPageSize = pageSizeOptions[0] ?? FIXED_PAGE_SIZE;
    const [paginationModel, setPaginationModel] = React.useState<GridPaginationModel>({
        page: 0,
        pageSize: defaultPageSize,
    });

    React.useEffect(() => {
        setPaginationModel((m) => ({ ...m, page: 0}));
    }, [rows]);

    if (error) {
        return (
            <Alert severity="error" sx={{ borderRadius: 2 }}>
                {error}
            </Alert>
        );
    }

    if (loading) {
        return (
            <Paper variant="outlined" sx={{ borderRadius: 3, p: 2 }}>
                <Skeleton height={36} />
                <Skeleton height={36} />
                <Skeleton height={36} />
                <Skeleton height={36} />
            </Paper>
        );
    }

    return (
        <Paper variant="outlined" sx={{ borderRadius: 3, width: "100%", overflow: "hidden" }}>
            <Box sx={{ height, width: "100%", minWidth: 0 }}>
                <DataGrid
                    rows={rows}
                    columns={columns}
                    getRowId={getRowId}
                    onRowClick={onRowClick}
                    disableRowSelectionOnClick
                    rowHeight={rowHeight}
                    columnHeaderHeight={headerHeight}
                    density="compact"
                    pagination
                    paginationModel={paginationModel}
                    onPaginationModelChange={setPaginationModel}
                    pageSizeOptions={pageSizeOptions}
                    showToolbar={toolbar}
                    slotProps={
                        toolbar
                            ? {
                                toolbar: {
                                    showQuickFilter: true,
                                    quickFilterProps: { debounceMs: 300 },
                                },
                            }
                            : undefined
                    }
                    sx={{
                        border: "none",
                        "& .MuiDataGrid-cell": {
                            overflow: "hidden",
                            textOverflow: "ellipsis",
                            whiteSpace: "nowrap",
                        },
                        "& .MuiDataGrid-columnHeaders": {
                            bgcolor: "background.default",
                            borderBottom: (t) => `1px solid ${t.palette.divider}`,
                        },
                        "& .MuiDataGrid-columnHeaderTitle": {
                            fontWeight: 700,
                            fontSize: "0.8rem",
                        },
                        "& .MuiDataGrid-row": {
                            cursor: onRowClick ? "pointer" : "default",
                        },
                        "& .MuiDataGrid-cell:focus, & .MuiDataGrid-columnHeader:focus": {
                            outline: "none",
                        },
                    }}
                />
            </Box>
        </Paper>
    );
}

export default GenericDataGrid;
