import {NavbarMenuModel} from "@/layouts/model/NavbarMenuModel.ts";

export interface SidebarMenuModel {
    label: string;
    icon: string;
    link: string;
    navbarMenu?: NavbarMenuModel[];
}
