import {Component, inject} from '@angular/core';
import {NavBarStore} from "../navbar.store";

@Component({
  selector: 'app-navbar-search',
  standalone: true,
  imports: [],
  templateUrl: './navbar-search.component.html',
  styleUrl: './navbar-search.component.scss'
})
export class NavbarSearchComponent {
  readonly navbarStore = inject(NavBarStore);
}
