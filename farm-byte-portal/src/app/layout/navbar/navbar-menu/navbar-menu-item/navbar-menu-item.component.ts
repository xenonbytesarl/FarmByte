import {Component, HostListener, input, InputSignal, signal, WritableSignal} from '@angular/core';
import {NgClass} from "@angular/common";
import {NavbarMenu} from "../../../navbar-menu.model";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'farmbyte-navbar-menu-item',
  standalone: true,
  imports: [
    NgClass,
    RouterLink
  ],
  templateUrl: './navbar-menu-item.component.html',
  styleUrl: './navbar-menu-item.component.scss'
})
export class NavbarMenuItemComponent {
  navbarMenu: InputSignal<NavbarMenu | undefined > = input();
  isOpen: WritableSignal<boolean> = signal(false);

  toggleDropDown(): void {
    this.isOpen.set(!this.isOpen());
  }

  @HostListener('document:click', ['$event'])
  onClickOutside(event: MouseEvent): void {
    const target = event.target as HTMLElement;
    const clickInside = target.closest('#dropDownMenuItem_'  + this.navbarMenu()?.label);
    if (!clickInside) {
      this.isOpen.set(false);
    }
  }
}
