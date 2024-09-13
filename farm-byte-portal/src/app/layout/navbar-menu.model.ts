export interface NavbarMenu {
  label: string;
  link?: string;
  items?: NavbarMenu[];
}

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
        link:  'inventory/unit-of-measures',
      },
      {
        label: 'navbarMenu_inventory_category-unit-of-measure',
        link:  'inventory/category-unit-of-measures',
      },
      {
        label: 'navbarMenu_inventory_location',
        link:  'inventory/locations',
      }
    ]
  }
  /*{
    label: 'Operations',
    items: [
      {
        label: 'Stock adjustment',
        link:  'stock-adjustments',
      },
      {
        label: 'Transfers',
        link:  'transfers',
      }
    ]
  },
  {
    label: 'Products',
    items: [
      {
        label: 'Products',
        link:  'inventory/products',
      },
      {
        label: 'Products categories',
        link:  'inventory/product-categories',
      }
    ]
  },
  {
    label: 'Configuration',
    items: [
      {
        label: 'Unit of measure',
        link:  'inventory/unit-of-measures',
      },
      {
        label: 'Category unit of measure',
        link:  'inventory/category-unit-of-measures',
      },
      {
        label: 'Location',
        link:  'inventory/locations',
      }
    ]
  }*/
]
