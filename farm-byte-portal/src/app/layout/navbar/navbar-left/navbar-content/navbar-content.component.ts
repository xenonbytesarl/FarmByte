import {Component} from '@angular/core';
import {NavbarMenuComponent} from "./navbar-menu/navbar-menu.component";
import {Button} from "primeng/button";
import {MenuModule} from "primeng/menu";
import {MenuItem} from "primeng/api";

@Component({
  selector: 'farm-byte-navbar-content',
  standalone: true,
  imports: [
    NavbarMenuComponent,
    Button,
    MenuModule,
  ],
  templateUrl: './navbar-content.component.html',
  styleUrl: './navbar-content.component.scss'
})
export class NavbarContentComponent {
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
