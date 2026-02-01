import type {GridColDef} from "@mui/x-data-grid";
import type {ColumnConfig} from "../types/config.ts";

const formatEuropeanDate = (isoDate: string): string => {
    const date = new Date(isoDate);
    return date.toLocaleString('fr-FR', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
    }) + ' - ' + date.toLocaleString('fr-FR', {
        hour: '2-digit',
        minute: '2-digit',
    });
};

const getLabelByLanguage = (obj: Record<string, string> | undefined): string => {

    if (!obj) {
        return 'Undefined';
    }

    const lang = navigator.language;
    if (lang.startsWith('fr')) {
        return obj['labelFr'] || 'Undefined';
    } else if (lang.startsWith('en')) {
        return obj['labelEn'] || 'Undefined';
    } else if (lang.startsWith('nl')) {
        return obj['labelNl'] || 'Undefined';
    } else if (lang.startsWith('de')) {
        return obj['labelDe'] || 'Undefined';
    }
    return 'Undefined';

};

async function loadColumnsFromJson(url: string): Promise<GridColDef[]> {
    const res = await fetch(url);
    if (!res.ok) throw new Error(`Failed to load columns from ${url}`);
    const columnDefs = (await res.json()) as ColumnConfig[];

    return columnDefs.map(col => ({
        field: col.field,
        headerName: col.headerName,
        flex: col.flex ?? 1,
        ...(col.labelByLanguage && col.valuePath && {
            valueGetter: (_: unknown, row: unknown) => getLabelByLanguage(row?.[col.valuePath])
        })
    }));
}

export { formatEuropeanDate, getLabelByLanguage, loadColumnsFromJson };