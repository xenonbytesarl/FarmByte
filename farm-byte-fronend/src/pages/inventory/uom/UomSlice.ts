import {createAsyncThunk, createEntityAdapter, createSlice, PayloadAction} from "@reduxjs/toolkit";
import {SuccessResponseModel} from "@/shared/model/successResponseModel.ts";
import {PageModel} from "@/shared/model/pageModel.ts";
import {FindParamModel} from "@/shared/model/findParamModel.ts";
import {RootState} from "@/Store.ts";
import {UomModel} from "@/pages/inventory/uom/UomModel.ts";
import uomService from "@/pages/inventory/uom/UomService.ts";
import {DEFAULT_SIZE_VALUE} from "@/constants/page.constant.ts";


const uomAdapter = createEntityAdapter<UomModel>({
});

const uomInitialState = uomAdapter.getInitialState({
    loading: false,
    pageSize: DEFAULT_SIZE_VALUE,
    totalElements: 0,
    message: '',
    error: null
});


export const findUoms = createAsyncThunk('uom/findUoms', async (findParam: FindParamModel): Promise<SuccessResponseModel<PageModel<UomModel>>> => {
    try {
        return uomService.findUoms(findParam);
    } catch (error) {
        console.log('error', error);
    }
});


const uomSlice = createSlice({
    name: "uom",
    initialState: uomInitialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(findUoms.pending, (state) => {
                state.loading = true;
                state.message = '';
                state.error = null;
            })
            .addCase(findUoms.fulfilled, (state, action: PayloadAction<SuccessResponseModel<PageModel<UomModel>>>) => {
                state.loading = false;
                state.pageSize = action.payload.data.content.pageSize;
                state.totalElements = action.payload.data.content.totalElements;
                state.message = action.payload.message;
                uomAdapter.upsertMany(state, action.payload.data.content.elements)
            })
            .addCase(findUoms.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload;
            })
    }
});

export const getPageSize = (state: RootState) => state.inventory.uom.pageSize;
export const getTotalElements = (state: RootState) => state.inventory.uom.totalElements;
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