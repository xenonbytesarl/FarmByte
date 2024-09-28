import {SidebarMenuModel} from "@/layouts/model/sidebar-menu.ts";

export const sidebarMenus :SidebarMenuModel[] = [
    {
        label: 'Dashboard',
        icon: 'dashboard',
        link: '/dashboard',
    },
    {
        label: 'Inventory',
        icon: 'inventory',
        link: '/inventory',
    },
    {
        label: 'Sale',
        icon: 'storefront',
        link: '/sale',
    },
    {
        label: 'Purchase',
        icon: 'shopping_bag',
        link: '/purchase',
    },
    {
        label: 'Invoice',
        icon: 'receipt',
        link: '/invoice',
    },
    {
        label: 'Farm',
        icon: 'agriculture',
        link: '/farm',
    },
    {
        label: 'Setting',
        icon: 'settings',
        link: '/setting',
    }
];