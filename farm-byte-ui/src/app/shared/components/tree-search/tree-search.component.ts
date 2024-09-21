import {Component, ElementRef, signal, viewChild} from '@angular/core';
import {IconFieldModule} from "primeng/iconfield";
import {InputIconModule} from "primeng/inputicon";
import {InputTextModule} from "primeng/inputtext";
import {TranslateModule} from "@ngx-translate/core";
import {StyleClassModule} from "primeng/styleclass";

@Component({
  selector: 'farmbyte-tree-search',
  standalone: true,
  imports: [
    IconFieldModule,
    InputIconModule,
    InputTextModule,
    TranslateModule,
    StyleClassModule
  ],
  template: `
    <div class="flex flex-row justify-content-end align-items-center w-full mb-3">
      <p-iconField iconPosition="left" class="w-30rem">
        <p-inputIcon>
          <i class="pi pi-search"></i>
        </p-inputIcon>
        <input
          pInputText
          type="text"
          #searchFilter
          (keyup)="applyFilter($event)"
          class="w-full"
          placeholder="{{'tree_search_place_holder' | translate}}"/>
      </p-iconField>
    </div>
  `,
  styles: `

  `
})
export class TreeSearchComponent {

  searchFilter = viewChild.required<ElementRef<HTMLInputElement>>('searchFilter');
  keyword = signal('');

  applyFilter(event: KeyboardEvent) {
    this.keyword.set((event.target as HTMLInputElement).value);
  }
}
