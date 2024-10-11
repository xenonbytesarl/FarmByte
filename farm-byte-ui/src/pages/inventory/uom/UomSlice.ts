import {createAsyncThunk, createEntityAdapter, createSlice, isAnyOf} from "@reduxjs/toolkit";
import {SuccessResponseModel} from "@/shared/model/successResponseModel.ts";
import {PageModel} from "@/shared/model/pageModel.ts";
import {FindParamModel} from "@/shared/model/findParamModel.ts";
import {RootState} from "@/Store.ts";
import {UomModel} from "@/pages/inventory/uom/UomModel.ts";
import uomService from "@/pages/inventory/uom/UomService.ts";
import {DEFAULT_SIZE_VALUE} from "@/constants/page.constant.ts";
import {SearchParamModel} from "@/shared/model/searchParamModel.ts";
import {UNKNOWN_ERROR} from "@/shared/constant/globalConstant.ts";


const uomAdapter = createEntityAdapter<UomModel>({
});

const uomInitialState = uomAdapter.getInitialState({
    loading: false,
    pageSize: DEFAULT_SIZE_VALUE,
    totalElements: 0,
    totalPages: 0,
    message: '',
    error: null
});


export const findUoms = createAsyncThunk('uom/findUoms', async (findParam: FindParamModel, {rejectWithValue}) => {
     try {
            const response =  await uomService.findUoms(findParam);
            return response.data;
        } catch (error) {
            if (error && error.response && error.response.data) {
                return rejectWithValue(error.response.data);
            } else if (error.message) {
                return rejectWithValue(error.message);
            } else {
                return rejectWithValue(UNKNOWN_ERROR);
            }
        }
});

export const searchUoms = createAsyncThunk('uom/searchUoms', async (searchParam: SearchParamModel, {rejectWithValue}) => {
     try {
            const response =  await uomService.searchUoms(searchParam);
            return response.data;
        } catch (error) {
            if (error && error.response && error.response.data) {
                return rejectWithValue(error.response.data);
            } else if (error.message) {
                return rejectWithValue(error.message);
            } else {
                return rejectWithValue(UNKNOWN_ERROR);
            }
        }
});

export const findUomById = createAsyncThunk('uom/findUomById', async (uomId: string, {rejectWithValue}) => {
    try {
        const response =  await uomService.findUomById(uomId);
        return response.data;
    } catch (error) {
        if (error && error.response && error.response.data) {
            return rejectWithValue(error.response.data);
        } else if (error.message) {
            return rejectWithValue(error.message);
        } else {
            return rejectWithValue(UNKNOWN_ERROR);
        }
    }
});

export const updateUom = createAsyncThunk('uom/updateUom', async ({uomId, uom}:{uomId: string, uom: UomModel}, {rejectWithValue}) => {
    try {
        const response =  await uomService.updateUom(uomId, uom);
        return response.data;
    } catch (error) {
        if (error && error.response && error.response.data) {
            return rejectWithValue(error.response.data);
        } else if (error.message) {
            return rejectWithValue(error.message);
        } else {
            return rejectWithValue(UNKNOWN_ERROR);
        }
    }
});

export const createUom = createAsyncThunk('uom/createUom', async ( uom: UomModel, {rejectWithValue}) => {
    try {
        const response =  await uomService.createUom(uom);
        return response.data;
    } catch (error) {
        if (error && error.response && error.response.data) {
            return rejectWithValue(error.response.data);
        } else if (error.message) {
            return rejectWithValue(error.message);
        } else {
            return rejectWithValue(UNKNOWN_ERROR);
        }
    }
});

const uomSlice = createSlice({
    name: "uom",
    initialState: uomInitialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(findUomById.fulfilled, (state, action) => {
                const {data, message} = action.payload as SuccessResponseModel<UomModel>;
                state.loading = false;
                state.totalElements = state.totalElements + 1;
                state.message = message;
                uomAdapter.addOne(state, data.content);
            })
            .addCase(updateUom.fulfilled, (state, action) => {
                const {data, message} = action.payload as SuccessResponseModel<UomModel>;
                state.loading = false;
                state.message = message;
                uomAdapter.updateOne(state, {id: data.content.id, changes: data.content});
            })
            .addCase(createUom.fulfilled, (state, action) => {
                const {data, message} = action.payload as SuccessResponseModel<UomModel>;
                state.loading = false;
                state.totalElements = state.totalElements + 1;
                state.message = message;
                uomAdapter.addOne(state, data.content);
            })
            .addMatcher(isAnyOf(findUoms.fulfilled, searchUoms.fulfilled), (state, action) => {
                const {data, message} = action.payload as SuccessResponseModel<PageModel<UomModel>>;
                state.loading = false;
                state.pageSize = data.content.pageSize;
                state.totalElements = data.content.totalElements;
                state.totalPages = data.content.totalPages;
                state.message = message;
                uomAdapter.setAll(state, data.content.elements)
            })
            .addMatcher(
                isAnyOf(
                    findUoms.pending,
                    searchUoms.pending,
                    findUomById.pending,
                    updateUom.pending,
                    createUom.pending,
                ), (state) => {
                    state.loading = true;
                    state.message = '';
                    state.error = null;
                })
            .addMatcher(
                isAnyOf(
                    findUoms.rejected,
                    searchUoms.rejected,
                    findUomById.rejected,
                    updateUom.rejected,
                    createUom.rejected
                ), (state, action) => {
                    state.loading = false;
                    state.message = '';
                    state.error = action.payload;
                })
    }
});

export const getPageSize = (state: RootState) => state.inventory.uom.pageSize;
export const getTotalElements = (state: RootState) => state.inventory.uom.totalElements;
export const getTotalPages = (state: RootState) => state.inventory.uom.totalPages;
export const getMessage = (state: RootState) => state.inventory.uom.message;
export const getError = (state: RootState) => state.inventory.uom.error;
export const getLoading = (state: RootState) => state.inventory.uom.loading;

export const {
    selectAll: selectUoms,
    selectIds: selectUomIds,
    selectById: selectUomById,
    selectEntities: selectUomEntities,
} = uomAdapter.getSelectors((state: RootState) => state.inventory.uom);

export default uomSlice.reducer;