import {Component, HostListener, inject, input, InputSignal, signal, WritableSignal} from '@angular/core';
import {NavbarMenu} from "../../../models/navbar-menu.model";
import {NavbarMenuStore} from "../../../store/navbar-menu.store";
import {Router, RouterLink} from "@angular/router";
import {NgClass} from "@angular/common";
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'farmbyte-navbar-menu-item',
  standalone: true,
  imports: [
    NgClass,
    RouterLink,
    TranslateModule
  ],
  templateUrl: './navbar-menu-item.component.html',
  styleUrl: './navbar-menu-item.component.scss'
})
export class NavbarMenuItemComponent {
  navbarMenu: InputSignal<NavbarMenu | undefined > = input();
  isOpen: WritableSignal<boolean> = signal(false);
  readonly navBarMenuStore = inject(NavbarMenuStore);
  router: Router = inject(Router);

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

  isLastSubMenu(index: number, navBarMenu: NavbarMenu | undefined): boolean {
    if(navBarMenu === undefined) {
      return false;
    }
    return navBarMenu.items !== undefined && navBarMenu.items.length === index + 1;
  }
}
