import {SidebarMenuModel} from "@/layouts/model/SidebarMenuModel.ts";
import {stockNavbarMenus} from "@/layouts/data/stockNavbarMenuData.ts";

export const sidebarMenus :SidebarMenuModel[] = [
    {
        label: 'sidebarMenu_dashboard',
        icon: 'dashboard',
        link: '/dashboard',
    },
    {
        label: 'sidebarMenu_stock',
        icon: 'inventory',
        link: '/stock',
        navbarMenu: stockNavbarMenus
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