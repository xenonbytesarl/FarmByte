import {FindParamModel} from "@/shared/model/findParamModel.ts";
import {SuccessResponseModel} from "@/shared/model/successResponseModel.ts";
import {PageModel} from "@/shared/model/pageModel.ts";
import {ProductModel} from "@/pages/stock/product/ProductModel.ts";
import axios from "axios";
import {API_BASE_URL, API_FORM_DATA_HEADER, API_JSON_HEADER} from "@/shared/constant/globalConstant.ts";
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

const updateProduct = async (productId: string, product: ProductModel, file: File): Promise<SuccessResponseModel<ProductModel>> => {
    const formData = new FormData();
    formData.append('image', file);
    formData.append('updateProductViewRequest', new Blob([JSON.stringify(product)], { type: 'application/json' }));
    return await axios.put(API_BASE_URL + `/catalog/products/${productId}`, formData, {
        headers: API_FORM_DATA_HEADER
    })
}

const createProduct = async (product: ProductModel, file: File): Promise<SuccessResponseModel<ProductModel>> => {
    const formData = new FormData();
    formData.append('image', file);
    formData.append('createProductViewRequest', new Blob([JSON.stringify(product)], { type: 'application/json' }));
    return await axios.post(API_BASE_URL + `/catalog/products`, formData, {
        headers: API_FORM_DATA_HEADER
    })
}

const productService = {
    findProducts,
    searchProducts,
    findProductById,
    updateProduct,
    createProduct
}

export default productService;