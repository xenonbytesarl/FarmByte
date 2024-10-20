import {UomTypeEnum} from "@/pages/stock/uom/UomTypeEnum.ts";


export interface UomModel {
  id: string;
  name: string;
  uomCategoryId: string;
  uomType: UomTypeEnum;
  ratio: number;
  active: boolean;
}
