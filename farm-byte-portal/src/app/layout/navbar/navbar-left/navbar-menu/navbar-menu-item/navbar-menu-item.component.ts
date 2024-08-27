import {Component, input, InputSignal} from '@angular/core';
import {Menu} from "../../../../sidebar/menu.model";

@Component({
  selector: 'app-navbar-menu-item',
  standalone: true,
  imports: [],
  templateUrl: './navbar-menu-item.component.html',
  styleUrl: './navbar-menu-item.component.scss'
})
export class NavbarMenuItemComponent {
  menu: InputSignal<Menu> = input.required();
}
