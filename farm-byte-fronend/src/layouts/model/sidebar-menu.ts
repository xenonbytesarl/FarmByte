import {NavbarMenuModel} from "@/layouts/model/navbar-menu-model.ts";

export interface SidebarMenuModel {
    label: string;
    icon: string;
    link: string;
    navbarMenu?: NavbarMenuModel[];
}
