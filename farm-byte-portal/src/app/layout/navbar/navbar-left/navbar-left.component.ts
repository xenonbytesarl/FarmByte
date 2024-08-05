import {Component} from '@angular/core';
import {NavbarFeatureComponent} from "./navbar-feature/navbar-feature.component";
import {NavbarContentComponent} from "./navbar-content/navbar-content.component";

@Component({
  selector: 'farm-byte-navbar-left',
  standalone: true,
  imports: [
    NavbarFeatureComponent,
    NavbarContentComponent,
  ],
  templateUrl: './navbar-left.component.html',
  styleUrl: './navbar-left.component.scss'
})
export class NavbarLeftComponent  {

}
