import { Component } from '@angular/core';
import { environment as env } from '../../environments/environment';


@Component({
  selector: 'app-menu-bar',
  templateUrl: './menu-bar.component.html',
  styleUrl: './menu-bar.component.css'
})

export class MenuBarComponent {
  readonly loginUrl =  env.loginUrl;

}
