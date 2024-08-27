import {Component, inject} from '@angular/core';
import {NavBarStore} from "../navbar.store";
import {SidebarStore} from "../../sidebar/sidebar.store";
import {NavbarMenuComponent} from "./navbar-menu/navbar-menu.component";

@Component({
  selector: 'app-navbar-left',
  standalone: true,
  imports: [
    NavbarMenuComponent
  ],
  templateUrl: './navbar-left.component.html',
  styleUrl: './navbar-left.component.scss'
})
export class NavbarLeftComponent {
  readonly navbarStore = inject(NavBarStore);
  readonly sidebarStore = inject(SidebarStore);

  onToggleSidebar(): void {
    this.navbarStore.doToggleSidebar()
  }
}
