import {Component} from '@angular/core';
import {NavbarModuleComponent} from "./navbar-module/navbar-module.component";
import {NavbarMenuComponent} from "./navbar-menu/navbar-menu.component";
import {NavbarLanguageComponent} from "./navbar-language/navbar-language.component";
import {NavbarProfileComponent} from "./navbar-profile/navbar-profile.component";

@Component({
  selector: 'farmbyte-navbar',
  standalone: true,
  imports: [
    NavbarModuleComponent,
    NavbarMenuComponent,
    NavbarLanguageComponent,
    NavbarProfileComponent
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {

}
