import {inventoryNavbarMenus, NavbarMenu} from "./navbar-menu.model";

export interface SidebarMenu {
  icon: string;
  label: string;
  link: string;
  navbarMenuItems?: NavbarMenu[];
}

export const sidebarMenuItems: SidebarMenu[] = [
  {
    icon: 'dashboard',
    label: 'sidebarMenu_dashboard',
    link: 'dashboard',
  },
  {
    icon: 'inventory',
    label: 'sidebarMenu_inventory',
    link: 'inventory',
    navbarMenuItems: inventoryNavbarMenus
  }
]
