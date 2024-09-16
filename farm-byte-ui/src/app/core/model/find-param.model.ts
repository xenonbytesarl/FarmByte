import {Direction} from "../enums/direction.enum";

export interface FindParamModel {
  page: number;
  size: number;
  attribute: string;
  direction: Direction
}
