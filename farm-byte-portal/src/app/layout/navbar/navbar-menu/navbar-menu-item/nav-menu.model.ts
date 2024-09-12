export interface NavMenu {
  label: string;
  icon?: string;
  link?: string;
  items?: NavMenu[];
}

export const navMenuItems: NavMenu[] = [
  {
    label: 'Operations',
    icon: 'keyboard_arrow_down',
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
    icon: 'keyboard_arrow_down',
    items: [
      {
        label: 'Products',
        link:  'products',
      },
      {
        label: 'Products categories',
        link:  'product-categories',
      }
    ]
  },
  {
    label: 'Configuration',
    icon: 'keyboard_arrow_down',
    items: [
      {
        label: 'Unit of measure',
        link:  'unit-of-measures',
      },
      {
        label: 'Category unit of measure',
        link:  'category-unit-of-measures',
      },
      {
        label: 'Location',
        link:  'locations',
      }
    ]
  }
]
