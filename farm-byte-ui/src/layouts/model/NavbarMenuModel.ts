import {NavbarMenuItemModel} from "@/layouts/model/NavbarMenuItemModel.ts";

export interface NavbarMenuModel {
    label: string;
    link?: string;
    items?: NavbarMenuItemModel[];
}