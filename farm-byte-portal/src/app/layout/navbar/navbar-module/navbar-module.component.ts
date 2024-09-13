import {Component, inject} from '@angular/core';
import {NavbarMenuStore} from "../../navbar-menu.store";

@Component({
  selector: 'farmbyte-navbar-module',
  standalone: true,
  imports: [],
  templateUrl: './navbar-module.component.html',
  styleUrl: './navbar-module.component.scss'
})
export class NavbarModuleComponent {
  readonly navbarMenuStore = inject(NavbarMenuStore);
}
