import {Component, input, InputSignal} from '@angular/core';
import {Button} from "primeng/button";
import {TranslateModule} from "@ngx-translate/core";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'farmbyte-tree-header',
  standalone: true,
  imports: [
    Button,
    TranslateModule,
    RouterLink
  ],
  template: `
    <div class="flex flex-row justify-content-between align-items-center  mb-3 w-full">
      <p class="text-2xl font-bold">{{title() | translate}}</p>
      <p-button [routerLink]="link()" size="small" [rounded]="true" label="{{'tree_button_label_save' | translate}}">
        <i class="material-symbols-outlined">add</i>
      </p-button>
    </div>
  `,
  styles: ``
})
export class TreeHeaderComponent {
  title: InputSignal<string> =  input('');
  link: InputSignal<string> =  input('');
}
