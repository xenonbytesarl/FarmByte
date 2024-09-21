import {ChangeDetectionStrategy, Component, ElementRef, signal, viewChild} from '@angular/core';
import {IconFieldModule} from "primeng/iconfield";
import {InputIconModule} from "primeng/inputicon";
import {InputTextModule} from "primeng/inputtext";
import {TranslateModule} from "@ngx-translate/core";
import {StyleClassModule} from "primeng/styleclass";
import {NgClass} from "@angular/common";

@Component({
  selector: 'farmbyte-tree-search',
  standalone: true,
  imports: [
    IconFieldModule,
    InputIconModule,
    InputTextModule,
    TranslateModule,
    StyleClassModule,
    NgClass
  ],
  template: `
    <div class="flex flex-row justify-content-end align-items-center w-full mb-3">
      <p-iconField iconPosition="right" class="w-30rem">
        <p-inputIcon>
          <i (click)="clear()" [ngClass]="searchFilter.value.length? 'pi pi-times cursor-pointer': 'pi pi-search'"></i>
        </p-inputIcon>
        <input
          pInputText
          type="text"
          #searchFilter
          (keyup)="applyFilter($event)"
          class="w-full border-round-3xl"
          placeholder="{{'tree_search_place_holder' | translate}}"/>
      </p-iconField>
    </div>
  `,
  styles: `

  `,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TreeSearchComponent {

  searchFilter = viewChild.required<ElementRef<HTMLInputElement>>('searchFilter');
  keyword = signal('');

  applyFilter(event: KeyboardEvent) {
    this.keyword.set((event.target as HTMLInputElement).value);
  }

  clear() {
    this.searchFilter().nativeElement.value = '';
    this.keyword.set('');
  }
}
