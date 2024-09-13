import {Component, HostListener, signal, WritableSignal} from '@angular/core';
import {TranslateModule} from "@ngx-translate/core";
import {NgClass} from "@angular/common";

@Component({
  selector: 'farmbyte-navbar-profile',
  standalone: true,
  imports: [
    TranslateModule,
    NgClass
  ],
  templateUrl: './navbar-profile.component.html',
  styleUrl: './navbar-profile.component.scss'
})
export class NavbarProfileComponent {

  isOpen: WritableSignal<boolean> = signal(false);

  toggleDropDown() {
    this.isOpen.set(!this.isOpen());
  }

  @HostListener('document:click', ['$event'])
  onClickOutside(event: MouseEvent): void {
    const target = event.target as HTMLElement;
    const clickInside = target.closest('#dropDownMenuItem_profile');
    if (!clickInside) {
      this.isOpen.set(false);
    }
  }
}
