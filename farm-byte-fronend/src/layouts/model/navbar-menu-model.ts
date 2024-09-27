import {NavbarMenuItemModel} from "@/layouts/model/navbar-menu-item-model.ts";

export interface NavbarMenuModel {
    label: string;
    link?: string;
    items?: NavbarMenuItemModel[];
}