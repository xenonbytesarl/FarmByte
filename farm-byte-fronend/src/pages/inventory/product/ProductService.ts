import {FindParamModel} from "@/shared/model/findParamModel.ts";
import {SuccessResponseModel} from "@/shared/model/successResponseModel.ts";
import {PageModel} from "@/shared/model/pageModel.ts";
import {ProductModel} from "@/pages/inventory/product/ProductModel.ts";
import axios from "axios";
import {API_BASE_URL, API_JSON_HEADER} from "@/shared/constant/globalConstant.ts";
import {SearchParamModel} from "@/shared/model/searchParamModel.ts";

const findProducts = async (findParam: FindParamModel): Promise<SuccessResponseModel<PageModel<ProductModel>>> => {
    return await axios.get(API_BASE_URL + '/catalog/products',
        {
            params: {...findParam},
            headers: API_JSON_HEADER
        });
}

const searchProducts = async (searchParam: SearchParamModel): Promise<SuccessResponseModel<PageModel<ProductModel>>> => {
    return await axios.get(API_BASE_URL + '/catalog/products/search',
        {
            params: {...searchParam},
            headers: API_JSON_HEADER
        });
}

const findProductById = async (productId: string): Promise<SuccessResponseModel<ProductModel>> => {
    return await axios.get(API_BASE_URL + `/catalog/products/${productId}`,
        {
            headers: API_JSON_HEADER
        });
}

const productService = {
    findProducts,
    searchProducts,
    findProductById
}

export default productService;