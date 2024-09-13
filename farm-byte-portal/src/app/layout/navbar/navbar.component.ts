import {Component} from '@angular/core';
import {NavbarModuleComponent} from "./navbar-module/navbar-module.component";
import {NavbarMenuComponent} from "./navbar-menu/navbar-menu.component";

@Component({
  selector: 'farmbyte-navbar',
  standalone: true,
  imports: [
    NavbarModuleComponent,
    NavbarMenuComponent
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {
}
