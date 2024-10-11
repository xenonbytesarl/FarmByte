import {NavbarMenuModel} from "@/layouts/model/NavbarMenuModel.ts";

export const inventoryNavbarMenus: NavbarMenuModel[] = [
    {
        label: 'navbarMenu_inventory_operations',
        items: [
            {
                label: 'navbarMenu_inventory_stock-adjustment',
                link: '/inventory/inventory-adjustments',
            },
            {
                label:'navbarMenu_inventory_transfers',
                link: '/inventory/transfers',
            }
        ]
    } ,
    {
        label: 'navbarMenu_inventory_products',
        items: [
            {
                label: 'navbarMenu_inventory_products',
                link: '/inventory/products',
            },
            {
                label: 'navbarMenu_inventory_products-categories',
                link: '/inventory/product-categories',
            }
        ]
    },
    {
        label: 'navbarMenu_inventory_report',
        link: '/inventory/reports',
    } ,
    {
        label: 'navbarMenu_inventory_setting',
        items: [
            {
                label: 'navbarMenu_inventory_unit-of-measure',
                link: '/inventory/uoms',
            },
            {
                label: 'navbarMenu_inventory_category-unit-of-measure',
                link: '/inventory/uom-categories',
            },
            {
                label: 'navbarMenu_inventory_location',
                link:  'inventory/stock-locations',
            }
        ]
    },
];

