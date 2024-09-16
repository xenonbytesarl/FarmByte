import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {LayoutComponent} from "./layout/layout.component";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'farmbyte-root',
  standalone: true,
  imports: [RouterOutlet, LayoutComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  constructor(private translate: TranslateService) {
    this.translate.addLangs(['fr', 'en']);
    //TODO remove after
    this.translate.use('fr');
  }
}
