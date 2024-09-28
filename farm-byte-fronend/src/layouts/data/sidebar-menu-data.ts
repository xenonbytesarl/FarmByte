import {SidebarMenuModel} from "@/layouts/model/sidebar-menu.ts";
import {inventoryNavbarMenus} from "@/layouts/data/inventory-navbar-menu.ts";

export const sidebarMenus :SidebarMenuModel[] = [
    {
        label: 'sidebarMenu_dashboard',
        icon: 'dashboard',
        link: '/dashboard',
    },
    {
        label: 'sidebarMenu_inventory',
        icon: 'inventory',
        link: '/inventory',
        navbarMenu: inventoryNavbarMenus
    },
    {
        label: 'sidebarMenu_sale',
        icon: 'storefront',
        link: '/sale',
    },
    {
        label: 'sidebarMenu_purchase',
        icon: 'shopping_bag',
        link: '/purchase',
    },
    {
        label: 'sidebarMenu_invoice',
        icon: 'receipt',
        link: '/invoice',
    },
    {
        label: 'sidebarMenu_farm',
        icon: 'agriculture',
        link: '/farm',
    },
    {
        label: 'sidebarMenu_setting',
        icon: 'settings',
        link: '/setting',
    }
];