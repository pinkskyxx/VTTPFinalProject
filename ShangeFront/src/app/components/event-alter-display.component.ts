import { Component, Input, OnInit, inject } from '@angular/core';
import { mapRequestModify } from '../models';
import { StateManageService } from '../state-manage.service';
import { MyServiceService } from '../my-service.service';

@Component({
  selector: 'app-event-alter-display',
  templateUrl: './event-alter-display.component.html',
  styleUrl: './event-alter-display.component.css'
})
export class EventAlterDisplayComponent implements OnInit{

  @Input() mapRequestModifyData: mapRequestModify[] = [];
  passwordInputs: string[] = [];

  constructor() { }

  private userService = inject(MyServiceService);
  private stateManageService = inject(StateManageService)

  ngOnInit(): void {
    this.stateManageService.dataModify$.subscribe(data => {
      this.mapRequestModifyData = data;
    });
  }

  updateEventDate(event: Event, data: mapRequestModify): void {
    // Ensure the event target is an HTMLInputElement
    if (event.target instanceof HTMLInputElement) {
      data.eventDate = event.target.valueAsDate ?? new Date(); // Use default value if null
    }
  }

  formatDate(date: Date | string): string {
    if (!date) {
      return '';
    }

    if (typeof date === 'string') {
      date = new Date(date);
    }

    // Check if conversion successful
    if (isNaN(date.getTime())) {
      return '';
    }

    const year = date.getFullYear();
    const month = ('0' + (date.getMonth() + 1)).slice(-2);
    const day = ('0' + date.getDate()).slice(-2);

    return `${year}-${month}-${day}`;
  }

  modifyData(id: string, data: mapRequestModify, password: string, newPassword: string) {
// Check if password matches data password
if (password === newPassword) {
  this.userService.putMapRequestModifyById(id, data).subscribe(
    () => {
      console.log('Record updated successfully.');
      alert('Record updated successfully.');
    },
    (error) => {
      console.error('Error update record:', error);
      if (error.status === 400 && error.error && error.error.result) {

        alert(error.error.result);
      } else {
        alert('An error occurred. Please try again later.');
      }
    }
  );
} else {
  alert('Wrong password');
}
  }



  deleteData(id: string, password: string, newPassword: string, rowIndex: number) {

    if (password === newPassword) {
      this.userService.deleteMapRequestModifyById(id).subscribe(
        () => {
          console.log('Record deleted successfully.');
          this.passwordInputs.splice(rowIndex, 1);
          this.stateManageService.deleteModifyData(id)
          alert('Record deleted successfully.');
        },
        (error) => {
          console.error('Error deleting data:', error);

          if (error.status === 400 && error.error && error.error.result) {
            alert(error.error.result);
          } else {
            alert('An error occurred. Please try again later.');
          }
        }
      );
    } else {

      alert('Wrong password');
    }
  }

  deleteDataLogic(id: string): void {
  }

  handlePasswordInput(data: any): void {
    data.passwordEntered = true; // password has been entered
  }
}
