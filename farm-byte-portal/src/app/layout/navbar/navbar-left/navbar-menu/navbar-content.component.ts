import {Component} from '@angular/core';
import {NavbarModuleComponent} from "./navbar-module/navbar-module.component";
import {NavbarMenuComponent} from "./navbar-menu/navbar-menu.component";

@Component({
  selector: 'farm-byte-navbar-menu',
  standalone: true,
  imports: [
    NavbarMenuComponent,
    NavbarModuleComponent,
  ],
  templateUrl: './navbar-content.component.html',
  styleUrl: './navbar-content.component.scss'
})
export class NavbarContentComponent {
}
