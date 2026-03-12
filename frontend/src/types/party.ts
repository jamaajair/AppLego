export type Party = {
    ref: number;
    label: string;
    kind: string;
}

export interface PartiesPopupProps {
    open: boolean;
    onClose: () => void;
    parties: Party[];
}