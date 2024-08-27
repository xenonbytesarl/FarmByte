import {Component, inject} from '@angular/core';
import {SidebarStore} from "../sidebar.store";
import {RouterLink} from "@angular/router";
import {SidebarMenuItemComponent} from "./sidebar-menu-item/sidebar-menu-item.component";

@Component({
  selector: 'app-sidebar-menu',
  standalone: true,
  imports: [
    RouterLink,
    SidebarMenuItemComponent
  ],
  templateUrl: './sidebar-menu.component.html',
  styleUrl: './sidebar-menu.component.scss'
})
export class SidebarMenuComponent {
  readonly sidebarStore = inject(SidebarStore);
}
