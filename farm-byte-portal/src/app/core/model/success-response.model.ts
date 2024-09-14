import {BaseResponseModel} from "./base-response.model";

export interface SuccessResponseModel<T> extends BaseResponseModel {
  message?: string;
  data: {
    content: T
  };
}
