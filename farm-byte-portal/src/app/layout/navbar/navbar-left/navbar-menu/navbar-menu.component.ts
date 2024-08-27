import {Component, inject} from '@angular/core';
import {SidebarStore} from "../../../sidebar/sidebar.store";
import {NavbarMenuItemComponent} from "./navbar-menu-item/navbar-menu-item.component";

@Component({
  selector: 'app-navbar-menu',
  standalone: true,
  imports: [
    NavbarMenuItemComponent
  ],
  templateUrl: './navbar-menu.component.html',
  styleUrl: './navbar-menu.component.scss'
})
export class NavbarMenuComponent {
  readonly sidebarStore = inject(SidebarStore);

}
