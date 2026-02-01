import {DataGrid, type GridRowParams, type GridValidRowModel} from '@mui/x-data-grid';
import type { GridColDef } from '@mui/x-data-grid';
import Box from '@mui/material/Box';

interface GenericDataGridProps<T extends GridValidRowModel> {
    rows: readonly T[];
    columns: GridColDef[];
    loading?: boolean;
    error?: string | null;
    getRowId?: (row: T) => string | number;
    onRowClick?: (params: GridRowParams<T>) => void;
}

function GenericDataGrid<T extends GridValidRowModel>({
                                rows,
                                columns,
                                loading = false,
                                error = null,
                                getRowId,
                                onRowClick,
                            }: GenericDataGridProps<T>) {
    if (loading) return <div>Chargement...</div>;
    if (error) return <div style={{ color: 'red' }}>Erreur : {error}</div>;

    return (
        <Box sx={{
            position: 'fixed',
            left: 0,
            width: '100%',
            height: '75vh',
            margin: 0,
            padding: 0,
        }}>
            <DataGrid
                rows={rows}
                columns={columns.map((col) => ({ ...col, flex: col.flex || 1 }))}
                initialState={{ pagination: { paginationModel: { page: 0, pageSize: 30 } } }}
                pageSizeOptions={[30]}
                rowHeight={36}
                disableRowSelectionOnClick
                getRowId={getRowId}
                onRowClick={onRowClick}
                density="compact"
                sx={{
                    '& .MuiDataGrid-columnHeaders': {
                        backgroundColor: 'rgba(0, 123, 255, 0.12)',
                    },
                    '& .MuiDataGrid-columnHeaderTitle': {
                        color: '#003366',
                        fontWeight: 600,
                    },
                }}
            />
        </Box>
    );
}

export default GenericDataGrid;