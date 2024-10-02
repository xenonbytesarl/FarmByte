import {FindParamModel} from "@/shared/model/findParamModel.ts";
import axios from "axios";
import {API_BASE_URL, API_JSON_HEADER} from "@/shared/constant/globalConstant.ts";
import {SuccessResponseModel} from "@/shared/model/successResponseModel.ts";
import {PageModel} from "@/shared/model/pageModel.ts";
import {UomCategoryModel} from "@/pages/inventory/uom-category/UomCategoryModel.ts";

const findUomCategories = async (findParam: FindParamModel): Promise<SuccessResponseModel<PageModel<UomCategoryModel>>> => {
    return await axios.get(API_BASE_URL + '/catalog/uom-categories',
        {
            params: {...findParam},
            headers: API_JSON_HEADER
        }
    );
}

const uomCategoryService = {
    findUomCategories,
}

export default uomCategoryService;