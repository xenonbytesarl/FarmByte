import {Component, HostListener, inject, OnInit, signal, WritableSignal} from '@angular/core';
import {RouterLink} from "@angular/router";
import {TranslateModule, TranslateService} from "@ngx-translate/core";
import {NgClass} from "@angular/common";
import {Language, NavbarMenuStore} from "../../store/navbar-menu.store";

@Component({
  selector: 'farmbyte-navbar-language',
  standalone: true,
  imports: [
    RouterLink,
    TranslateModule,
    NgClass
  ],
  templateUrl: './navbar-language.component.html',
  styleUrl: './navbar-language.component.scss'
})
export class NavbarLanguageComponent implements OnInit {

  isOpen: WritableSignal<boolean> = signal(false);
  readonly navbarMenuStore = inject(NavbarMenuStore);
  readonly translate = inject(TranslateService);

  toggleDropDown(): void {
    this.isOpen.set(!this.isOpen());
  }

  @HostListener('document:click', ['$event'])
  onClickOutside(event: MouseEvent): void {
    const target = event.target as HTMLElement;
    const clickInside = target.closest('#dropDownMenuItem_language');
    if (!clickInside) {
      this.isOpen.set(false);
    }
  }

  selectLanguage(language: Language) {
    this.navbarMenuStore.changeLanguage(language);
  }

  ngOnInit(): void {
    this.translate.use(this.navbarMenuStore.selectedLanguage());
  }
}
