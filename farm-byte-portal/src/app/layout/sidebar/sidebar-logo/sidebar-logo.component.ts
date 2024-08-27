import {Component, inject} from '@angular/core';
import {NgClass} from "@angular/common";
import {NavBarStore} from "../../navbar/navbar.store";

@Component({
  selector: 'app-sidebar-logo',
  standalone: true,
  imports: [
    NgClass
  ],
  templateUrl: './sidebar-logo.component.html',
  styleUrl: './sidebar-logo.component.scss'
})
export class SidebarLogoComponent {
  readonly navbarStore = inject(NavBarStore);
}
