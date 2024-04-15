import { Component, inject } from '@angular/core';
import { StateManageService } from '../state-manage.service';
import { map, take } from 'rxjs';
import { mapRequest } from '../models';
import { MyServiceService } from '../my-service.service';

@Component({
  selector: 'app-event-request-display',
  templateUrl: './event-request-display.component.html',
  styleUrl: './event-request-display.component.css'
})
export class EventRequestDisplayComponent {

  private mapService = inject(MyServiceService);
  constructor(public stateService: StateManageService) {}


  submittingToBackend: boolean = false;
  submitStateToBackend(): void {
    if (this.submittingToBackend) {
      return;
    }

    this.submittingToBackend = true;

    console.log(this.stateService.data$);

    this.mapService.login("", "")
    .subscribe(
      (data) => {

        localStorage.setItem('token', data.jwt);

        this.stateService.data$

        .pipe(
          take(1), // Automatically unsubscribe after receiving first emission
          map(data => {
            return data.map(item => ({
              address: item.address,
              locX: item.locX,
              locY: item.locY,
              myid: item.myid,
              title: item.title,
              description: item.description,
              eventDate: new Date(item.eventDate),
              requestDate: new Date(item.requestDate),
              password: item.password,
              status: item.status,
              reply: item.reply
            }));
          })
        )
        .subscribe((mappedData: mapRequest[]) => {
          console.log(mappedData)
          this.mapService.sendEventRequest(mappedData)
            .subscribe(response => {

              console.log('Response from backend:', response);
              alert("Event Request sent successfully");
              // Clear state display memory
              this.stateService.setData([]);
              localStorage.clear(); //cannot see token anymore
            },
            error => {

              if (error.error && error.error.result) {
                alert('Error from backend: ' + error.error.result);
              } else {
                alert('An error occurred. Please try again later.');
              }
              localStorage.clear();
            },
            () => {

              this.submittingToBackend = false;
            });
        });


      },
      (error) => {
        console.error("Error logging in:", error);
        localStorage.clear();
      }



    );


  }
  submitStateToBackend1(): void {
    if (this.submittingToBackend) {
      // Submission is already in progress, ignore this click
      return;
    }

    this.submittingToBackend = true;

    console.log(this.stateService.data$);
    this.stateService.data$
      .pipe(
        take(1), // Automatically unsubscribe after receiving the first emission
        map(data => {
          return data.map(item => ({
            address: item.address,
            locX: item.locX,
            locY: item.locY,
            myid: item.myid,
            title: item.title,
            description: item.description,
            eventDate: new Date(item.eventDate),
            requestDate: new Date(item.requestDate),
            password: item.password,
            status: item.status,
            reply: item.reply
          }));
        })
      )
      .subscribe((mappedData: mapRequest[]) => {
        console.log(mappedData)
        this.mapService.sendEventRequest(mappedData)
          .subscribe(response => {
            // Handle response from backend if needed
            console.log('Response from backend:', response);
            alert("Event Request sent successfully");
            // Clear the state display memory
            this.stateService.setData([]); // Assuming setData method is available in stateService to update the data$
          },
          error => {
            // Handle error if needed
            // console.error('Error:', error);
            // alert("Event Request Error");
            // Display the error message from the backend
            if (error.error && error.error.result) {
              alert('Error from backend: ' + error.error.result);
            } else {
              alert('An error occurred. Please try again later.');
            }
          },
          () => {
            // Set submittingToBackend to false after completing the request (success, error, or completion)
            this.submittingToBackend = false;
          });
      });
  }
// submitStateToBackend(): void {
//   if (this.submittingToBackend) {
//     // Submission is already in progress, ignore this call
//     console.log("sending to back end...")
//     return;
//   }

//   this.submittingToBackend = true;

//   console.log(this.stateService.data$)
//   this.stateService.data$
//     .pipe(
//       map(data => {
//         return data.map(item => ({
//           address: item.address,
//           locX: item.locX,
//           locY: item.locY,
//           myid: item.myid,
//           title: item.title,
//           description: item.description,
//           eventDate: new Date(item.eventDate),
//           requestDate: new Date(item.requestDate),
//           password: item.password,
//           status: item.status
//         }));
//       })
//     )
//     .subscribe((mappedData: mapRequest[]) => {
//       console.log(mappedData)
//       this.mapService.sendEventRequest(mappedData)
//         .subscribe(response => {
//           // Handle response from backend if needed
//           console.log('Response from backend:', response);
//           alert("Event Request sent successfully");
//           // Clear the state display memory
//           this.stateService.setData([]); // Assuming setData method is available in stateService to update the data$

//         },
//         error => {
//           // Handle error if needed
//           // console.error('Error:', error);
//           // alert("Event Request Error");
//           // Display the error message from the backend
//           if (error.error && error.error.result) {
//             alert('Error from backend: ' + error.error.result);
//           } else {
//             alert('An error occurred. Please try again later.');
//           }
//         })
//         .add(() => {
//           // Set submittingToBackend to false after completing the request (success or error)
//           this.submittingToBackend = false;
//         });
//     });
// }
}
