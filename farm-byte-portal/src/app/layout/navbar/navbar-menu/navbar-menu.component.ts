import {Component, inject} from '@angular/core';
import {NavbarMenuItemComponent} from "./navbar-menu-item/navbar-menu-item.component";
import {NavbarMenuStore} from "../../store/navbar-menu.store";

@Component({
  selector: 'farmbyte-navbar-menu',
  standalone: true,
  imports: [
    NavbarMenuItemComponent
  ],
  templateUrl: './navbar-menu.component.html',
  styleUrl: './navbar-menu.component.scss'
})
export class NavbarMenuComponent {
 readonly navMenuStore = inject(NavbarMenuStore);
}
