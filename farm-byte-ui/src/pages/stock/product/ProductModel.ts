import {ProductTypeEnum} from "@/pages/stock/product/ProductTypeEnum.ts";

export interface ProductModel {
  id: string;
  name: string;
  reference?: string;
  salePrice: number;
  purchasePrice: number;
  filename: string;
  type: ProductTypeEnum | "";
  categoryId: string;
  stockUomId: string;
  purchaseUomId: string;
  purchasable: boolean;
  sellable: boolean;
  active: boolean;
  encodedFile: string;
  mime: string;
}
