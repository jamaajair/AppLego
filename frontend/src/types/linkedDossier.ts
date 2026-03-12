import type { Dossier } from "./dossier";
import type { LinkKind } from "./linkKind";

export type LinkedDossier  = {
  parentDossier: Dossier;
  childDossier: Dossier;
  sequenceNumber: number;
  linkKind: LinkKind;
}

export type LinkPayload = {
    parentDossierRef: string;
    childDossierRef: string;
    sequenceNumber: number;
    linkKind: string;
};