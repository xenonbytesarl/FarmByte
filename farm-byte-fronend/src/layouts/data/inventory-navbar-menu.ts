import {NavbarMenuModel} from "@/layouts/model/navbar-menu-model.ts";

export const inventoryNavbarMenus: NavbarMenuModel[] = [
    {
        label: 'Opérations',
        items: [
            {
                label: 'Ajustement d\'inventaire',
                link: '/inventory/inventory-adjustments',
            },
            {
                label: 'Transfert',
                link: '/inventory/transfers',
            }
        ]
    } ,
    {
        label: 'Article',
        items: [
            {
                label: 'Article',
                link: '/inventory/products',
            },
            {
                label: 'Catégorie de produit',
                link: '/inventory/product-categories',
            }
        ]
    },
    {
        label: 'Report',
        link: '/inventory/reports',
    } ,
    {
        label: 'Setting',
        items: [
            {
                label: 'Unit of measure',
                link: '/inventory/uoms',
            },
            {
                label: 'Catégorie unit of measure',
                link: '/inventory/uom-categories',
            }
        ]
    },
];

