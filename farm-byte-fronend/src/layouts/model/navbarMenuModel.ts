import {NavbarMenuItemModel} from "@/layouts/model/navbarMenuItemModel.ts";

export interface NavbarMenuModel {
    label: string;
    link?: string;
    items?: NavbarMenuItemModel[];
}