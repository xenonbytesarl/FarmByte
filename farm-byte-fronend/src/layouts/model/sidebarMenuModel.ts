import {NavbarMenuModel} from "@/layouts/model/navbarMenuModel.ts";

export interface SidebarMenuModel {
    label: string;
    icon: string;
    link: string;
    navbarMenu?: NavbarMenuModel[];
}
