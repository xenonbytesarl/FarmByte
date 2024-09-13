import { Component } from '@angular/core';
import {LayoutComponent} from "./layout/layout.component";
import {TranslateModule, TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'farmbyte-root',
  standalone: true,
  imports: [
    LayoutComponent,
    TranslateModule
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  constructor(private translate: TranslateService) {
    this.translate.addLangs(['fr', 'en']);
  }
}
