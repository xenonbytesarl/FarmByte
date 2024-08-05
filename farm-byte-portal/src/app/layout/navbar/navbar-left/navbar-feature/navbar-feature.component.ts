import { Component } from '@angular/core';
import {ImageModule} from "primeng/image";

@Component({
  selector: 'farm-byte-navbar-feature',
  standalone: true,
  imports: [
    ImageModule
  ],
  templateUrl: './navbar-feature.component.html',
  styleUrl: './navbar-feature.component.scss'
})
export class NavbarFeatureComponent {

}
