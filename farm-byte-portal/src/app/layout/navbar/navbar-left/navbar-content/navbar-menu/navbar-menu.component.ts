import {Component, OnInit} from '@angular/core';
import {Button, ButtonDirective} from "primeng/button";
import {MenuModule} from "primeng/menu";
import {ImageModule} from "primeng/image";
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
import {MenuItem} from "primeng/api";

@Component({
  selector: 'farm-byte-navbar-module-menu',
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
      NgStyle
    ],
  templateUrl: './navbar-menu.component.html',
  styleUrl: './navbar-menu.component.scss'
})
export class NavbarMenuComponent implements OnInit {
  items: MenuItem[] | undefined;




  ngOnInit(): void {
    this.items = [
      {
        items: [
          {
            label: 'Inventaire',
            icon: 'fa fa-user'
          },
          {
            label: 'Transfert',
            icon: 'fa fa-bell'
          }
        ]
      }
    ];

  }

}
