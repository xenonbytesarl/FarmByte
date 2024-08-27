import {Component, inject} from '@angular/core';
import {SidebarComponent} from "./sidebar/sidebar.component";
import {NavbarComponent} from "./navbar/navbar.component";
import {RouterOutlet} from "@angular/router";
import {NavBarStore} from "./navbar/navbar.store";
import {NgClass} from "@angular/common";

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [
    SidebarComponent,
    NavbarComponent,
    RouterOutlet,
    NgClass
  ],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.scss'
})
export class LayoutComponent {
  readonly navbarStore = inject(NavBarStore);
}
