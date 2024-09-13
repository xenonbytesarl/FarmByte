import {Component, inject, input, InputSignal, OnInit} from '@angular/core';
import {SidebarMenu} from "../../sidebar-menu.model";
import {NavbarMenuStore} from "../../navbar-menu.store";
import {NavbarMenu} from "../../navbar-menu.model";
import {SidebarMenuStore} from "../../sidebar-menu.store";
import {NgClass} from "@angular/common";
import {Router} from "@angular/router";
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
export class SidebarMenuComponent implements OnInit {

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
        this.sidebarMenuStore.sidebarMenus()[0]?.navbarMenuItems as NavbarMenu[],
        this.sidebarMenuStore.sidebarMenus()[0]?.label
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
