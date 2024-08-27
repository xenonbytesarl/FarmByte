import {Component, inject} from '@angular/core';
import {NavBarStore} from "../navbar.store";

@Component({
  selector: 'app-navbar-right',
  standalone: true,
  imports: [],
  templateUrl: './navbar-right.component.html',
  styleUrl: './navbar-right.component.scss'
})
export class NavbarRightComponent {
  readonly navbarStore = inject(NavBarStore);
}
