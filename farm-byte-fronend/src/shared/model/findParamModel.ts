import {Direction} from "@/constants/directionConstant.ts";

export interface FindParamModel {
  page: number;
  size: number;
  attribute: string;
  direction: Direction
}
