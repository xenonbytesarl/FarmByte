import {FindParamModel} from "@/shared/model/findParamModel.ts";
import {SuccessResponseModel} from "@/shared/model/successResponseModel.ts";
import {PageModel} from "@/shared/model/pageModel.ts";
import {ProductCategoryModel} from "@/pages/inventory/product-category/ProductCategoryModel.ts";
import axios from "axios";
import {API_BASE_URL, API_JSON_HEADER} from "@/shared/constant/globalConstant.ts";

const findProductCategories = async (findParam: FindParamModel): Promise<SuccessResponseModel<PageModel<ProductCategoryModel>>> => {
    return await axios.get(API_BASE_URL + '/catalog/product-categories',
        {
            params: {...findParam},
            headers: API_JSON_HEADER
        }
    );
}

const productCategoryService = {
    findProductCategories,
}

export default productCategoryService;