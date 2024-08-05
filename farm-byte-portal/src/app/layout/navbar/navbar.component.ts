import {Component} from '@angular/core';
import {NavbarLeftComponent} from "./navbar-left/navbar-left.component";
import {NavbarRightComponent} from "./navbar-right/navbar-right.component";

@Component({
  selector: 'farm-byte-navbar',
  standalone: true,
  imports: [
    NavbarLeftComponent,
    NavbarRightComponent
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {

}
