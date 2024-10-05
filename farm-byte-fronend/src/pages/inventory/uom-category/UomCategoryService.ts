import {FindParamModel} from "@/shared/model/findParamModel.ts";
import axios from "axios";
import {API_BASE_URL, API_JSON_HEADER} from "@/shared/constant/globalConstant.ts";
import {SuccessResponseModel} from "@/shared/model/successResponseModel.ts";
import {PageModel} from "@/shared/model/pageModel.ts";
import {UomCategoryModel} from "@/pages/inventory/uom-category/UomCategoryModel.ts";
import {SearchParamModel} from "@/shared/model/searchParamModel.ts";
import {UomModel} from "@/pages/inventory/uom/UomModel.ts";

const findUomCategories = async (findParam: FindParamModel): Promise<SuccessResponseModel<PageModel<UomCategoryModel>>> => {
    return await axios.get(API_BASE_URL + '/catalog/uom-categories',
        {
            params: {...findParam},
            headers: API_JSON_HEADER
        }
    );
}

const searchUomCategories = async (searchParam: SearchParamModel): Promise<SuccessResponseModel<PageModel<UomCategoryModel>>> => {
    return await axios.get(API_BASE_URL + '/catalog/uom-categories/search',
        {
            params: {...searchParam},
            headers: API_JSON_HEADER
        }
    );
}


const findUomCategoryById = async (uomCategoryId: string): Promise<SuccessResponseModel<PageModel<UomCategoryModel>>> => {
    return await axios.get(API_BASE_URL + `/catalog/uom-categories/${uomCategoryId}`,
        {
            headers: API_JSON_HEADER
        }
    );
}
const uomCategoryService = {
    findUomCategories,
    searchUomCategories,
    findUomCategoryById
}

export default uomCategoryService;