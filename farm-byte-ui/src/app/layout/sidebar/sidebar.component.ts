import {Component, inject} from '@angular/core';
import {SidebarMenuStore} from "../store/sidebar-menu.store";
import {SidebarMenuComponent} from "./sidebar-menu/sidebar-menu.component";

@Component({
  selector: 'farmbyte-sidebar',
  standalone: true,
  imports: [
    SidebarMenuComponent
  ],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent {
  readonly sidebarMenuStore = inject(SidebarMenuStore);
}
