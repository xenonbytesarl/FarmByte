import {ProductTypeEnum} from "@/pages/inventory/product/ProductTypeEnum.ts";

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
  encodedFilename: string;
  mime: string;
}
