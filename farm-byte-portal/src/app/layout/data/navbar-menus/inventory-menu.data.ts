import {NavbarMenu} from "../../models/navbar-menu.model";

export const inventoryNavbarMenus: NavbarMenu[] = [
  {
    label: 'navbarMenu_inventory_operations',
    items: [
      {
        label: 'navbarMenu_inventory_stock-adjustment',
        link:  'stock-adjustments',
      },
      {
        label: 'navbarMenu_inventory_transfers',
        link:  'transfers',
      }
    ]
  },
  {
    label: 'navbarMenu_inventory_products',
    items: [
      {
        label: 'navbarMenu_inventory_products',
        link:  'inventory/products',
      },
      {
        label: 'navbarMenu_inventory_products-categories',
        link:  'inventory/product-categories',
      }
    ]
  },
  {
    label: 'navbarMenu_inventory_settings',
    items: [
      {
        label: 'navbarMenu_inventory_unit-of-measure',
        link:  'inventory/uoms',
      },
      {
        label: 'navbarMenu_inventory_category-unit-of-measure',
        link:  'inventory/uom-categories',
      },
      {
        label: 'navbarMenu_inventory_location',
        link:  'inventory/stock-locations',
      }
    ]
  }
];
