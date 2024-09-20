import {Component, input, InputSignal} from '@angular/core';
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'farmbyte-no-data-found',
  standalone: true,
  imports: [
    TranslateModule
  ],
  template: `
    <p class="text-gray-400 font-medium mt-5 text-center">{{ message() | translate}}</p>
  `,
  styles: ``
})
export class NoDataFoundComponent {
  message: InputSignal<string> =  input('');
}
