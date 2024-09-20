import {Component, ElementRef, signal, viewChild} from '@angular/core';
import {IconFieldModule} from "primeng/iconfield";
import {InputIconModule} from "primeng/inputicon";
import {InputTextModule} from "primeng/inputtext";
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'farmbyte-tree-search',
  standalone: true,
    imports: [
        IconFieldModule,
        InputIconModule,
        InputTextModule,
        TranslateModule
    ],
  template: `
    <div class="flex justify-items-end align-items-center mb-3">
      <p-iconField iconPosition="left" class="ml-auto">
        <p-inputIcon>
          <i class="material-symbols-outlined">search</i>
        </p-inputIcon>
        <input
          pInputText
          type="text"
          #searchFilter
          (keyup)="applyFilter($event)"
          placeholder="{{'tree_search_place_holder' | translate}}"/>
      </p-iconField>
    </div>
  `,
  styles: ``
})
export class TreeSearchComponent {

  searchFilter = viewChild.required<ElementRef<HTMLInputElement>>('searchFilter');
  keyword = signal('');

  applyFilter(event: KeyboardEvent) {
    this.keyword.set((event.target as HTMLInputElement).value);
  }
}
