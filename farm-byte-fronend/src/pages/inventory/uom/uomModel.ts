import {UomTypeEnum} from "@/pages/inventory/uom/uomTypeEnum.ts";


export interface UomModel {
  id: string;
  name: string;
  uomCategoryId: string;
  uomType: UomTypeEnum;
  ratio: number;
  active: boolean;
}
