import { Component } from '@angular/core';
import {Ripple} from "primeng/ripple";

@Component({
  selector: 'farm-byte-sidebar-menu',
  standalone: true,
    imports: [
        Ripple
    ],
  templateUrl: './sidebar-menu.component.html',
  styleUrl: './sidebar-menu.component.scss'
})
export class SidebarMenuComponent {

}
