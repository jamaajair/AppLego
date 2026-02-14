import type { Dossier } from "./dossier";
import type { LinkKind } from "./linkKind";

export interface LinkedDossier {
  parentDossier: Dossier;
  childDossier: Dossier;
  sequenceNumber: number;
  linkKind: LinkKind;
}