export interface Dossier {
    ref: string;
    label: string;
    comments: string;
    createdAt: string;
    startNoLaterThan: string;
    expectCompletion: string;
    state: {
        code: string;
        labelNl: string;
        labelEn: string;
        labelFr: string;
        labelDe: string;
    };
    type: {
        code: string;
        labelNl: string;
        labelEn: string;
        labelFr: string;
        labelDe: string;
    };
}