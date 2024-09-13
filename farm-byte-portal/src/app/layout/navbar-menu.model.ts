export interface NavbarMenu {
  label: string;
  link?: string;
  items?: NavbarMenu[];
}

export const inventoryNavbarMenus: NavbarMenu[] = [
  {
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
  }
]
