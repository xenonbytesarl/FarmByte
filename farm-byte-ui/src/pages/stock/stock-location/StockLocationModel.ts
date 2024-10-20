import {StockLocationTypeEnum} from "@/pages/stock/stock-location/StockLocationTypeEnum.ts";

export interface StockLocationModel {
    id: string;
    name: string;
    type: StockLocationTypeEnum | "";
    parentId: string;
    active: true
}