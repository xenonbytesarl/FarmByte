import {FindParamModel} from "@/shared/model/findParamModel.ts";
import {SuccessResponseModel} from "@/shared/model/successResponseModel.ts";
import {PageModel} from "@/shared/model/pageModel.ts";
import {UomModel} from "@/pages/inventory/uom/UomModel.ts";
import axios from "axios";
import {API_BASE_URL, API_JSON_HEADER} from "@/shared/constant/globalConstant.ts";

const findUoms = async (findParam: FindParamModel): Promise<SuccessResponseModel<PageModel<UomModel>>> => {
    return await axios.get(API_BASE_URL + '/catalog/uoms',
        {
            params: {...findParam},
            headers: API_JSON_HEADER
        }
    );
}

const uomService = {
    findUoms,
}

export default uomService;