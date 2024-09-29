import {BaseResponseModel} from "./baseResponseModel.ts";

export interface ErrorResponseModel extends BaseResponseModel{
  reason: string;
  path: string;
  trackId?: string;
  error?: Array<{
    field: string;
    message: string;
  }>
}
