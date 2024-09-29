import {createAsyncThunk, createEntityAdapter, createSlice, PayloadAction} from "@reduxjs/toolkit";
import {ProductModel} from "@/pages/inventory/product/productModel.ts";
import axios from "axios";
import {SuccessResponseModel} from "@/shared/model/successResponseModel.ts";
import {PageModel} from "@/shared/model/pageModel.ts";
import {FindParamModel} from "@/shared/model/findParamModel.ts";
import {RootState} from "@/store/store.ts";
import {API_BASE_URL, API_JSON_HEADER} from "@/shared/constant/globalConstant.ts";


const productAdapter = createEntityAdapter<ProductModel>({
});

const productInitialState = productAdapter.getInitialState({
    loading: false,
    pageSize: 0,
    totalElements: 0,
    message: '',
    error: null
});


export const findProducts = createAsyncThunk('product/findProducts', async (findParam: FindParamModel): Promise<SuccessResponseModel<PageModel<ProductModel>>> => {

    const response = await axios.get(
        API_BASE_URL + '/catalog/products',
        {
            params: {...findParam},
            headers: API_JSON_HEADER
        }
    );
    return response.data;
});


const productSlice = createSlice({
    name: "product",
    initialState: productInitialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(findProducts.pending, (state) => {
                state.loading = true;
                state.message = '';
                state.error = null;
            })
            .addCase(findProducts.fulfilled, (state, action: PayloadAction<SuccessResponseModel<PageModel<ProductModel>>>) => {
                state.loading = false;
                state.pageSize = action.payload.data.content.pageSize;
                state.totalElements = action.payload.data.content.totalElements;
                state.message = action.payload.message;
                productAdapter.upsertMany(state, action.payload.data.content.elements)
            })
            .addCase(findProducts.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload;
            })
    }
});

export const getPageSize = (state: RootState) => state.inventory.product.pageSize;
export const getTotalElements = (state: RootState) => state.inventory.product.totalElements;
export const getMessage = (state: RootState) => state.inventory.product.message;
export const getError = (state: RootState) => state.inventory.product.error;
export const getLoading = (state: RootState) => state.inventory.product.loading;

export const {
    selectAll: selectProducts,
    selectIds: selectProductIds,
    selectById: selectProductById,
    selectEntities: selectProductEntities,
} = productAdapter.getSelectors((state: RootState) => state.inventory.product);

export default productSlice.reducer;