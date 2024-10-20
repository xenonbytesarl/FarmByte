import {FindParamModel} from "@/shared/model/findParamModel.ts";
import {SuccessResponseModel} from "@/shared/model/successResponseModel.ts";
import {PageModel} from "@/shared/model/pageModel.ts";
import {ProductCategoryModel} from "@/pages/stock/product-category/ProductCategoryModel.ts";
import axios from "axios";
import {API_BASE_URL, API_JSON_HEADER} from "@/shared/constant/globalConstant.ts";
import {SearchParamModel} from "@/shared/model/searchParamModel.ts";

const findProductCategories = async (findParam: FindParamModel): Promise<SuccessResponseModel<PageModel<ProductCategoryModel>>> => {
    return await axios.get(API_BASE_URL + '/catalog/product-categories',
        {
            params: {...findParam},
            headers: API_JSON_HEADER
        }
    );
}

const searchProductCategories = async (searchParam: SearchParamModel): Promise<SuccessResponseModel<PageModel<ProductCategoryModel>>> => {
    return await axios.get(API_BASE_URL + '/catalog/product-categories/search',
        {
            params: {...searchParam},
            headers: API_JSON_HEADER
        }
    );
}

const findProductCategoryById = async (productCategoryId: string): Promise<SuccessResponseModel<ProductCategoryModel>> => {
    return await axios.get(API_BASE_URL + `/catalog/product-categories/${productCategoryId}`,
        {
            headers: API_JSON_HEADER
        }
    );
}

const updateProductCategory = async (productCategoryId: string, productCategory: ProductCategoryModel): Promise<SuccessResponseModel<ProductCategoryModel>> => {
    return await axios.put(API_BASE_URL + `/catalog/product-categories/${productCategoryId}`, productCategory,
        {
            headers: API_JSON_HEADER
        }
    );
}

const createProductCategory = async (productCategory: ProductCategoryModel): Promise<SuccessResponseModel<ProductCategoryModel>> => {
    return await axios.post(API_BASE_URL + `/catalog/product-categories`, productCategory,
        {
            headers: API_JSON_HEADER
        }
    );
}

const productCategoryService = {
    findProductCategories,
    searchProductCategories,
    findProductCategoryById,
    updateProductCategory,
    createProductCategory
}

export default productCategoryService;