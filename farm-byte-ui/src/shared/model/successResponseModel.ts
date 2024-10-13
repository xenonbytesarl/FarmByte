import {BaseResponseModel} from "./baseResponseModel.ts";

export interface SuccessResponseModel<T> extends BaseResponseModel {
  message: string;
  data: {
    content: T
  };
}
