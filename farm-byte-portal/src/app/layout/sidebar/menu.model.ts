export interface BaseMenu {
  label: string;
  link?: string;
}

export interface Menu extends BaseMenu {
  items?: Menu[];
}

export interface NavbarMenu {
  module: string;
  menus?: Menu[];
}

export interface SidebarMenu extends BaseMenu {
  icon: string;
  navbarMenu: NavbarMenu;
}
