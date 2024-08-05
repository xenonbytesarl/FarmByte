import {Component} from '@angular/core';
import {NavbarComponent} from "./navbar/navbar.component";
import {SidebarComponent} from "./sidebar/sidebar.component";
import {RouterOutlet} from "@angular/router";
import {MenuModule} from "primeng/menu";
import {Button, ButtonDirective} from "primeng/button";
import {MenuItem} from "primeng/api";
import {NavbarRightComponent} from "./navbar/navbar-right/navbar-right.component";
import {AvatarModule} from "primeng/avatar";
import {BadgeModule} from "primeng/badge";
import {NavbarFeatureComponent} from "./navbar/navbar-left/navbar-feature/navbar-feature.component";
import {NavbarContentComponent} from "./navbar/navbar-left/navbar-content/navbar-content.component";
import {NavbarLeftComponent} from "./navbar/navbar-left/navbar-left.component";
import {CheckboxModule} from "primeng/checkbox";
import {Ripple} from "primeng/ripple";
import {InputTextModule} from "primeng/inputtext";


@Component({
  selector: 'farm-byte-layout',
  standalone: true,
  imports: [
    NavbarComponent,
    SidebarComponent,
    RouterOutlet,
    MenuModule,
    Button,
    NavbarRightComponent,
    AvatarModule,
    BadgeModule,
    NavbarFeatureComponent,
    NavbarContentComponent,
    NavbarLeftComponent,
    CheckboxModule,
    ButtonDirective,
    Ripple,
    InputTextModule

  ],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.scss'
})
export class LayoutComponent  {
  items: MenuItem[] | undefined;

  ngOnInit() {
    this.items = [
      {
        label: 'Options',
        items: [
          {
            label: 'Refresh',
            icon: 'fa fa-refresh'
          },
          {
            label: 'Export',
            icon: 'fa fa-upload'
          }
        ]
      }
    ];
  }
}
