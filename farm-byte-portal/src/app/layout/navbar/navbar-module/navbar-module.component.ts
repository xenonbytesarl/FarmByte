import {Component, inject} from '@angular/core';
import {NavbarMenuStore} from "../../navbar-menu.store";
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'farmbyte-navbar-module',
  standalone: true,
  imports: [
    TranslateModule
  ],
  templateUrl: './navbar-module.component.html',
  styleUrl: './navbar-module.component.scss'
})
export class NavbarModuleComponent {
  readonly navbarMenuStore = inject(NavbarMenuStore);
}
