import {SidebarMenu} from "../models/sidebar-menu.model";
import {inventoryNavbarMenus} from "./navbar-menus/inventory-menu.data";

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
];
