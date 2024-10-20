import {NavbarMenuModel} from "@/layouts/model/NavbarMenuModel.ts";

export const stockNavbarMenus: NavbarMenuModel[] = [
    {
        label: 'navbarMenu_stock_operations',
        items: [
            {
                label: 'navbarMenu_stock_stock-adjustment',
                link: '/stock/stock-adjustments',
            },
            {
                label:'navbarMenu_stock_transfers',
                link: '/stock/transfers',
            }
        ]
    } ,
    {
        label: 'navbarMenu_stock_products',
        items: [
            {
                label: 'navbarMenu_stock_products',
                link: '/stock/products',
            },
            {
                label: 'navbarMenu_stock_products-categories',
                link: '/stock/product-categories',
            }
        ]
    },
    {
        label: 'navbarMenu_stock_report',
        link: '/stock/reports',
    } ,
    {
        label: 'navbarMenu_stock_setting',
        items: [
            {
                label: 'navbarMenu_stock_unit-of-measure',
                link: '/stock/uoms',
            },
            {
                label: 'navbarMenu_stock_category-unit-of-measure',
                link: '/stock/uom-categories',
            },
            {
                label: 'navbarMenu_stock_location',
                link:  'stock/stock-locations',
            }
        ]
    },
];

