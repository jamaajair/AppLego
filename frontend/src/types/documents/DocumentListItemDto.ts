export interface DocumentListItemDto {
  // DocumentRelatedSet
  rankInSet: number;
  roleInSetCode: string;

  // DocumentReference
  ref: number;
  typeCode: string;
  notes: string;
  isDocLocal: number;

  // LocalDocument
  label: string;
  stateCode: string;
}