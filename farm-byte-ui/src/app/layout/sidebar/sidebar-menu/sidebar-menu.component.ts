import {Component, inject, input, InputSignal, OnInit} from '@angular/core';
import {NavbarMenuStore} from "../../store/navbar-menu.store";
import {SidebarMenu} from "../../models/sidebar-menu.model";
import {SidebarMenuStore} from "../../store/sidebar-menu.store";
import {Router} from "@angular/router";
import {NavbarMenu} from "../../models/navbar-menu.model";
import {NgClass} from "@angular/common";
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'farmbyte-sidebar-menu',
  standalone: true,
  imports: [
    NgClass,
    TranslateModule
  ],
  templateUrl: './sidebar-menu.component.html',
  styleUrl: './sidebar-menu.component.scss'
})
export class SidebarMenuComponent implements OnInit{
  sidebarMenu: InputSignal<SidebarMenu> = input.required();
  readonly navbarMenuStore = inject(NavbarMenuStore);
  readonly sidebarMenuStore = inject(SidebarMenuStore);
  readonly router = inject(Router);

  ngOnInit(): void {
    this.initSidebar();
  }

  private initSidebar() {
    if (this.sidebarMenuStore.sidebarMenus()) {
      this.navbarMenuStore.loadNavbarMenus(
        this.sidebarMenuStore.sidebarMenus()[1]?.navbarMenuItems as NavbarMenu[],
        this.sidebarMenuStore.sidebarMenus()[1]?.label
      );
      this.sidebarMenuStore.updateCurrentSidebarMenu(this.sidebarMenuStore.sidebarMenus()[0] as SidebarMenu);
    }
  }

  onClick(sidebarMenu: SidebarMenu | undefined): void {
    this.navbarMenuStore.loadNavbarMenus(
      sidebarMenu?.navbarMenuItems? sidebarMenu.navbarMenuItems: [],
      sidebarMenu?.label? sidebarMenu.label: ''
    );
    this.sidebarMenuStore.updateCurrentSidebarMenu(sidebarMenu as SidebarMenu);
    this.router.navigate([sidebarMenu?.link]).then(() => {});
  }
}
