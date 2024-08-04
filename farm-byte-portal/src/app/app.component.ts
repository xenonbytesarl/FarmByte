import {Component, inject, OnInit} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {Button} from "primeng/button";
import {NavbarComponent} from "./layout/navbar/navbar.component";
import {SidebarComponent} from "./layout/sidebar/sidebar.component";
import {LayoutComponent} from "./layout/layout.component";

@Component({
  selector: 'farm-byte-root',
  standalone: true,
  imports: [RouterOutlet, Button, NavbarComponent, SidebarComponent, LayoutComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit{



  ngOnInit(): void {

  }

}
