import {Component, OnInit} from '@angular/core';
import {Button, ButtonDirective} from "primeng/button";
import {ImageModule} from "primeng/image";
import {MenuModule} from "primeng/menu";
import {MenuItem} from "primeng/api";
import {ToolbarModule} from "primeng/toolbar";
import {AvatarModule} from "primeng/avatar";
import {SidebarModule} from "primeng/sidebar";
import {RouterLink} from "@angular/router";
import {BadgeModule} from "primeng/badge";
import {MenubarModule} from "primeng/menubar";
import {NgClass, NgStyle} from "@angular/common";
import {ToastModule} from "primeng/toast";
import {Ripple} from "primeng/ripple";
import {StyleClassModule} from "primeng/styleclass";
import {NavbarBrandComponent} from "./navbar-brand/navbar-brand.component";
import {NavbarContentComponent} from "./navbar-menu/navbar-content.component";

@Component({
  selector: 'farm-byte-nav-left',
  standalone: true,
  imports: [
    Button,
    ImageModule,
    MenuModule,
    ToolbarModule,
    AvatarModule,
    SidebarModule,
    Button,
    RouterLink,
    BadgeModule,
    MenubarModule,
    NgClass,
    ToastModule,
    MenuModule,
    Ripple,
    StyleClassModule,
    ButtonDirective,
    NgStyle,
    NavbarBrandComponent,
    NavbarContentComponent,
  ],
  templateUrl: './navbar-left.component.html',
  styleUrl: './navbar-left.component.scss'
})
export class NavbarLeftComponent  {

}
