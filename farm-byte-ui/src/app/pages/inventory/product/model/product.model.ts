import {ProductType} from "../enums/product-type.enum";

export interface ProductModel {
  id: string;
  name: string;
  reference?: string;
  salePrice: number;
  purchasePrice: number;
  filename: string;
  type: ProductType;
  categoryId: string;
  stockUomId: string;
  purchaseUomId: string;
  purchasable: boolean;
  sellable: boolean;
  active: boolean;
}
