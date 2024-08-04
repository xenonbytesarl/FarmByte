import { Component } from '@angular/core';
import {AvatarModule} from "primeng/avatar";
import {Ripple} from "primeng/ripple";

@Component({
  selector: 'farm-byte-sidebar-footer',
  standalone: true,
    imports: [
        AvatarModule,
        Ripple
    ],
  templateUrl: './sidebar-footer.component.html',
  styleUrl: './sidebar-footer.component.scss'
})
export class SidebarFooterComponent {

}
