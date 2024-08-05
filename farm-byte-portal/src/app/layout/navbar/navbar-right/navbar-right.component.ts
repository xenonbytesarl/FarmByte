import {Component} from '@angular/core';
import {AvatarModule} from "primeng/avatar";
import {BadgeModule} from "primeng/badge";

@Component({
  selector: 'farm-byte-navbar-right',
  standalone: true,
    imports: [
        AvatarModule,
        BadgeModule,

    ],
  templateUrl: './navbar-right.component.html',
  styleUrl: './navbar-right.component.scss'
})
export class NavbarRightComponent {

}
