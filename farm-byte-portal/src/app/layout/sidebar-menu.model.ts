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
    label: 'Dashboard',
    link: 'dashboard',
  },
  {
    icon: 'inventory',
    label: 'Inventory',
    link: 'inventory',
    navbarMenuItems: inventoryNavbarMenus
  }
]
