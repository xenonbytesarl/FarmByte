import {AxiosError} from "axios";
import {createEntityAdapter, createAsyncThunk, createSlice, isAnyOf} from "@reduxjs/toolkit";
import {StockLocationModel} from "@/pages/stock/stock-location/StockLocationModel.ts";
import {DEFAULT_SIZE_VALUE} from "@/constants/page.constant.ts";
import {FindParamModel} from "@/shared/model/findParamModel.ts";
import {ErrorResponseModel} from "@/shared/model/errorResponseModel.ts";
import {UNKNOWN_ERROR} from "@/shared/constant/globalConstant.ts";
import stockLocationService from "@/pages/stock/stock-location/StockLocationService.ts";
import {SearchParamModel} from "@/shared/model/searchParamModel.ts";
import {RootState} from "@/Store.ts";

const stockLocationAdapter = createEntityAdapter<StockLocationModel>({});

const stockLocationInitialState = stockLocationAdapter.getInitialState({
    loading: false,
    pageSize: DEFAULT_SIZE_VALUE,
    totalElements: 0,
    totalPages: 0,
    message: '',
    error: null
});

export const findStockLocations = createAsyncThunk('stockLocation/findStockLocations',  async (findParam: FindParamModel, {rejectWithValue}) => {
    try {
        const response =  await stockLocationService.findStockLocations(findParam);
        // @ts-ignore
        return {content: response.data.data.content, message: response.data.message}
    } catch (apiError) {
        const error = apiError as AxiosError<ErrorResponseModel>;
        if (error && error.response && error.response.data) {
            return rejectWithValue(error.response.data);
        } else if (error.message) {
            return rejectWithValue(error.message);
        } else {
            return rejectWithValue(UNKNOWN_ERROR);
        }
    }
});

export const searchStockLocations = createAsyncThunk('stockLocation/searchStockLocations',  async (searchParam: SearchParamModel, {rejectWithValue}) => {
    try {
        const response =  await stockLocationService.searchStockLocations(searchParam);
        // @ts-ignore
        return {content: response.data.data.content, message: response.data.message}
    } catch (apiError) {
        const error = apiError as AxiosError<ErrorResponseModel>;
        if (error && error.response && error.response.data) {
            return rejectWithValue(error.response.data);
        } else if (error.message) {
            return rejectWithValue(error.message);
        } else {
            return rejectWithValue(UNKNOWN_ERROR);
        }
    }
});

export const findStockLocationById = createAsyncThunk('stockLocation/findStockLocationById',  async (stockLocationId: string, {rejectWithValue}) => {
    try {
        const response =  await stockLocationService.findStockLocationById(stockLocationId);
        // @ts-ignore
        return {content: response.data.data.content, message: response.data.message}
    } catch (apiError) {
        const error = apiError as AxiosError<ErrorResponseModel>;
        if (error && error.response && error.response.data) {
            return rejectWithValue(error.response.data);
        } else if (error.message) {
            return rejectWithValue(error.message);
        } else {
            return rejectWithValue(UNKNOWN_ERROR);
        }
    }
});

export const updateStockLocation = createAsyncThunk('stockLocation/updateStockLocation',  async ({stockLocationId, stockLocation}: {stockLocationId: string, stockLocation: StockLocationModel}, {rejectWithValue}) => {
    try {
        const response =  await stockLocationService.updateStockLocation(stockLocationId, stockLocation);
        // @ts-ignore
        return {content: response.data.data.content, message: response.data.message}
    } catch (apiError) {
        const error = apiError as AxiosError<ErrorResponseModel>;
        if (error && error.response && error.response.data) {
            return rejectWithValue(error.response.data);
        } else if (error.message) {
            return rejectWithValue(error.message);
        } else {
            return rejectWithValue(UNKNOWN_ERROR);
        }
    }
});

export const createStockLocation = createAsyncThunk('createLocation/updateStockLocation',  async ( stockLocation: StockLocationModel, {rejectWithValue}) => {
    try {
        const response =  await stockLocationService.createStockLocation(stockLocation);
        // @ts-ignore
        return {content: response.data.data.content, message: response.data.message}
    } catch (apiError) {
        const error = apiError as AxiosError<ErrorResponseModel>;
        if (error && error.response && error.response.data) {
            return rejectWithValue(error.response.data);
        } else if (error.message) {
            return rejectWithValue(error.message);
        } else {
            return rejectWithValue(UNKNOWN_ERROR);
        }
    }
});

const stockLocationSlice = createSlice({
    name: 'stockLocation',
    initialState: stockLocationInitialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(findStockLocationById.fulfilled, (state, action) => {
                const {content, message} = action.payload;
                state.loading = false;
                state.message = message;
                stockLocationAdapter.addOne(state, content);
            })
            .addCase(updateStockLocation.fulfilled, (state, action) => {
                const {content, message} = action.payload;
                state.loading = false;
                state.message = message;
                stockLocationAdapter.updateOne(state, {id: content.id, changes: content});
            })
            .addCase(createStockLocation.fulfilled, (state, action) => {
                const {content, message} = action.payload;
                state.loading = false;
                state.totalElements = state.totalElements + 1;
                state.message = message;
                stockLocationAdapter.addOne(state, content);
            })
            .addMatcher(isAnyOf(findStockLocations.fulfilled, searchStockLocations.fulfilled), (state, action) => {
                const {content, message} = action.payload;
                state.loading = false;
                state.pageSize = content.pageSize as number;
                state.totalElements = content.totalElements as number;
                state.totalPages = content.totalPages as number;
                state.message = message;
                stockLocationAdapter.setAll(state, content.elements)
            })
            .addMatcher(
                isAnyOf(
                    findStockLocations.pending,
                    searchStockLocations.pending,
                    findStockLocationById.pending,
                    updateStockLocation.pending,
                    createStockLocation.pending,
                ), (state) => {
                    state.loading = true;
                    state.message = '';
                    state.error = null;
                })
            .addMatcher(
                isAnyOf(
                    findStockLocations.rejected,
                    searchStockLocations.rejected,
                    findStockLocationById.rejected,
                    updateStockLocation.rejected,
                    createStockLocation.rejected
                ), (state, action) => {
                    state.loading = false;
                    state.message = '';
                    state.error = action.payload as any;
                })
    }
});

export const getPageSize = (state: RootState) => state.stock.stockLocation.pageSize;
export const getTotalElements = (state: RootState) => state.stock.stockLocation.totalElements;
export const getTotalPages = (state: RootState) => state.stock.stockLocation.totalPages;
export const getMessage = (state: RootState) => state.stock.stockLocation.message;
export const getError = (state: RootState) => state.stock.stockLocation.error;
export const getLoading = (state: RootState) => state.stock.stockLocation.loading;

export const {
    selectAll: selectStockLocations,
    selectIds: selectStockLocationIds,
    selectById: selectStockLocationById,
    selectEntities: selectStockLocationEntities,
} = stockLocationAdapter.getSelectors((state: RootState) => state.stock.stockLocation);

export default stockLocationSlice.reducer;