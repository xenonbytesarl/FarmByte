import {Component, inject} from '@angular/core';
import {NavBarStore} from "./navbar.store";
import {NgClass} from "@angular/common";
import {NavbarSearchComponent} from "./navbar-search/navbar-search.component";
import {NavbarRightComponent} from "./navbar-right/navbar-right.component";
import {NavbarLeftComponent} from "./navbar-left/navbar-left.component";

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    NgClass,
    NavbarSearchComponent,
    NavbarRightComponent,
    NavbarLeftComponent
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {
  readonly navbarStore = inject(NavBarStore);

  onMobileNavbar(): void {
    this.navbarStore.doToggleMobilebar()
  }
}
