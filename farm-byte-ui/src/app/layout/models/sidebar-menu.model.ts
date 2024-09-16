import {NavbarMenu} from "./navbar-menu.model";

export interface SidebarMenu {
  icon: string;
  label: string;
  link: string;
  navbarMenuItems?: NavbarMenu[];
}


