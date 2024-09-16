import {UomType} from "../enums/uom-type.enum";

export interface UomModel {
  id?: string;
  name: string;
  uomCategoryId: string;
  uomType: UomType;
  ratio: number;
  active: boolean;
}
