import type {LinkedDossier} from "./linkedDossier.ts";
import type {Code} from "./code.ts";

export type Dossier = {
    ref: string;
    label: string;
}

export type PageState = {
    dossier: Dossier | null;
    linkedDossiers: LinkedDossier[];
    dossiers: Dossier[];
    linkKinds: Code[];
};