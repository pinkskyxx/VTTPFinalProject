import { Component, inject } from '@angular/core';
import { MyServiceService } from '../my-service.service';
import { EventDetails } from '../models';

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrl: './main-page.component.css'
})
export class MainPageComponent {

  private myService = inject(MyServiceService);
  myEventDetails: EventDetails[] = [];

  OnSearch(): void {
    this.myService.login("", "")
      .subscribe(
        (data) => {
          // Extract the JWT token from the response and store it in localStorage
          localStorage.setItem('token', data.jwt);

        this.myService.sendAllEventGet().subscribe(
          (response: EventDetails[]) => {
            // Handle the response from the backend
            console.log('Response from backend:', response);
            // Update component with retrieved data
            this.myEventDetails = response;
            localStorage.clear()
          },
          (error) => {
            // Handle errors if any
            console.error('Error:', error);
          }
        );
        },
        (error) => {
          console.error("Error logging in:", error);
        }
      );
  }
}
