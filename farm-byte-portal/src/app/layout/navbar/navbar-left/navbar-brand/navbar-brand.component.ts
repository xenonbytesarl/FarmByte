import { Component } from '@angular/core';
import {ImageModule} from "primeng/image";

@Component({
  selector: 'farm-byte-brand',
  standalone: true,
  imports: [
    ImageModule
  ],
  templateUrl: './navbar-brand.component.html',
  styleUrl: './navbar-brand.component.scss'
})
export class NavbarBrandComponent {

}
