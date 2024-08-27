import {SidebarMenu} from "./menu.model";

export const sidebarMenus: SidebarMenu[] = [
  {
    label: 'Dashboard',
    icon: 'dashboard',
    link: 'dashboard',
    navbarMenu: {
      module: 'Dashboard'
    }
  },
  {
    label: 'Sale',
    icon: 'storefront',
    link: 'sale',
    navbarMenu: {
      module: 'Sale',
      menus: [
        {
          label: 'Operation',
          items: [
            {
              label: 'Sale Quotation',
              link: 'sale-quotations'
            },
            {
              label: 'Sale Order',
              link: 'sale-orders'
            }
          ]
        },
        {
          label: 'Address Book',
          items: [
            {
              label: 'Customer',
              link: 'customers'
            }
          ]
        },
        {
          label: 'Products',
          items: [
            {
              label: 'Product',
              link: 'products'
            },
            {
              label: 'Product  Category',
              link: 'product-categories'
            }
          ]
        }
      ]
    }
  },
  {
    label: 'Stock',
    icon: 'inventory',
    link: '/stock',
    navbarMenu: {
      module: 'Stock',
      menus: [
        {
          label: 'Operation',
          items: [
            {
              label: 'Transfers',
              link: '/transfers'
            },
            {
              label: 'Inventory',
              link: '/inventories'
            }
          ]
        },
        {
          label: 'Products',
          items: [
            {
              label: 'Product',
              link: '/products'
            },
            {
              label: 'Product  Category',
              link: '/product-categories'
            }
          ]
        }
      ]
    }
  }
];
