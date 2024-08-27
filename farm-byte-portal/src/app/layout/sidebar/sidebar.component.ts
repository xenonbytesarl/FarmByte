import {ChangeDetectionStrategy, Component, inject} from '@angular/core';
import {RouterLink, RouterLinkActive} from "@angular/router";
import {NgClass} from "@angular/common";
import {SidebarMenuComponent} from "./sidebar-menu/sidebar-menu.component";
import {SidebarLogoComponent} from "./sidebar-logo/sidebar-logo.component";
import {NavBarStore} from "../navbar/navbar.store";

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [
    RouterLink,
    RouterLinkActive,
    NgClass,
    SidebarMenuComponent,
    SidebarLogoComponent
  ],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SidebarComponent {
}
