import {Component, ViewChild} from '@angular/core';
import {Sidebar, SidebarModule} from "primeng/sidebar";
import {Button} from "primeng/button";
import {AvatarModule} from "primeng/avatar";
import {Ripple} from "primeng/ripple";
import {StyleClassModule} from "primeng/styleclass";
import {SidebarBrandComponent} from "./sidebar-brand/sidebar-brand.component";
import {SidebarMenuComponent} from "./sidebar-menu/sidebar-menu.component";
import {SidebarFooterComponent} from "./sidebar-footer/sidebar-footer.component";

@Component({
  selector: 'farm-byte-sidebar',
  standalone: true,
  imports: [
    SidebarModule,
    Button,
    AvatarModule,
    Ripple,
    StyleClassModule,
    SidebarBrandComponent,
    SidebarMenuComponent,
    SidebarFooterComponent
  ],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent {
  @ViewChild('sidebarRef') sidebarRef!: Sidebar;

  closeCallback(e: Event): void {
    this.sidebarRef.close(e);
  }

  sidebarVisible: boolean = true;
}
