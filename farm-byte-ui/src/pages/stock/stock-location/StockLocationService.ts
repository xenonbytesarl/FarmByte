import axios from "axios";
import {FindParamModel} from "@/shared/model/findParamModel.ts";
import {SuccessResponseModel} from "@/shared/model/successResponseModel.ts";
import {PageModel} from "@/shared/model/pageModel.ts";
import {StockLocationModel} from "@/pages/stock/stock-location/StockLocationModel.ts";
import {API_BASE_URL, API_JSON_HEADER} from "@/shared/constant/globalConstant.ts";
import {SearchParamModel} from "@/shared/model/searchParamModel.ts";

const findStockLocations = async(findParam: FindParamModel): Promise<SuccessResponseModel<PageModel<StockLocationModel>>> => {
    return await axios.get(API_BASE_URL + '/stock/stock-locations',
        {
            params: {...findParam},
            headers: API_JSON_HEADER
        }
    );
}

const searchStockLocations = async(searchParam: SearchParamModel): Promise<SuccessResponseModel<PageModel<StockLocationModel>>> => {
    return await axios.get(API_BASE_URL + '/stock/stock-locations/search',
        {
            params: {...searchParam},
            headers: API_JSON_HEADER
        }
    );
}

const findStockLocationById = async(stockLocationId: string): Promise<SuccessResponseModel<StockLocationModel>> => {
    return await axios.get(API_BASE_URL + `/stock/stock-locations/${stockLocationId}`,
        {
            headers: API_JSON_HEADER
        }
    );
}

const updateStockLocation = async(stockLocationId: string, stockLocation: StockLocationModel): Promise<SuccessResponseModel<StockLocationModel>> => {
    return await axios.put(API_BASE_URL + `/stock/stock-locations/${stockLocationId}`, stockLocation,
        {
            headers: API_JSON_HEADER
        }
    );
}

const createStockLocation = async(stockLocation: StockLocationModel): Promise<SuccessResponseModel<StockLocationModel>> => {
    return await axios.post(API_BASE_URL + '/stock/stock-locations', stockLocation,
        {
            headers: API_JSON_HEADER
        }
    );
}

const StockLocationService = {
    findStockLocations,
    searchStockLocations,
    findStockLocationById,
    updateStockLocation,
    createStockLocation
}

export default StockLocationService;