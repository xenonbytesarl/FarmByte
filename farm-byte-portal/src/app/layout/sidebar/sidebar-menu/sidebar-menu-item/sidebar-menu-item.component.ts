import {Component, inject, input, InputSignal} from '@angular/core';
import {Router, RouterLink} from "@angular/router";
import {SidebarMenu} from "../../menu.model";
import {SidebarStore} from "../../sidebar.store";
import {NgClass} from "@angular/common";
import {NavBarStore} from "../../../navbar/navbar.store";

@Component({
  selector: 'app-sidebar-menu-item',
  standalone: true,
  imports: [
    RouterLink,
    NgClass
  ],
  templateUrl: './sidebar-menu-item.component.html',
  styleUrl: './sidebar-menu-item.component.scss'
})
export class SidebarMenuItemComponent {
  sidebarMenu: InputSignal<SidebarMenu | undefined> = input.required();
  router = inject(Router);

  navbarStore = inject(NavBarStore);
  sidebarStore = inject(SidebarStore);

  onSelectSidebarMenu(sidebarMenu: SidebarMenu | undefined): void {
    this.sidebarStore.changeSidebarMenus(sidebarMenu);
    this.router.navigate([sidebarMenu?.link]);
  }
}
