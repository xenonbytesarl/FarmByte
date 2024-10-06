import {FindParamModel} from "@/shared/model/findParamModel.ts";
import {SuccessResponseModel} from "@/shared/model/successResponseModel.ts";
import {PageModel} from "@/shared/model/pageModel.ts";
import {UomModel} from "@/pages/inventory/uom/UomModel.ts";
import axios from "axios";
import {API_BASE_URL, API_JSON_HEADER} from "@/shared/constant/globalConstant.ts";
import {SearchParamModel} from "@/shared/model/searchParamModel.ts";

const findUoms = async (findParam: FindParamModel): Promise<SuccessResponseModel<PageModel<UomModel>>> => {
    return await axios.get(API_BASE_URL + '/catalog/uoms',
        {
            params: {...findParam},
            headers: API_JSON_HEADER
        }
    );
}

const searchUoms = async (searchParam: SearchParamModel): Promise<SuccessResponseModel<PageModel<UomModel>>> => {
    return await axios.get(API_BASE_URL + '/catalog/uoms/search',
        {
            params: {...searchParam},
            headers: API_JSON_HEADER
        }
    );
}

const findUomById = async (uomId: string): Promise<SuccessResponseModel<UomModel>> => {
    return await axios.get(API_BASE_URL + `/catalog/uoms/${uomId}`,
        {
            headers: API_JSON_HEADER
        }
    );
}

const updateUom = async (uomId: string, uom: UomModel): Promise<SuccessResponseModel<UomModel>> => {
    return await axios.put(API_BASE_URL + `/catalog/uoms/${uomId}`, uom,
        {
            headers: API_JSON_HEADER
        }
    );
}

const createUom = async (uom: UomModel): Promise<SuccessResponseModel<UomModel>> => {
    return await axios.post(API_BASE_URL + `/catalog/uoms`, uom,
        {
            headers: API_JSON_HEADER
        }
    );
}

const uomService = {
    findUoms,
    searchUoms,
    findUomById,
    updateUom,
    createUom
}

export default uomService;