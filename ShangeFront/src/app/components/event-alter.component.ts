import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, ValidationErrors, Validators } from '@angular/forms';

import { StateManageService } from '../state-manage.service';
import { MyServiceService } from '../my-service.service';
import { mapRequestModify } from '../models';

@Component({
  selector: 'app-event-alter',
  templateUrl: './event-alter.component.html',
  styleUrl: './event-alter.component.css',
})
export class EventAlterComponent implements OnInit, OnDestroy {
  private formBuilder = inject(FormBuilder);
  private stateService = inject(StateManageService);
  private mapService = inject(MyServiceService);

  searchForm!: FormGroup;

  ngOnInit(): void {
    this.createForm();
  }
  ngOnDestroy(): void {

    this.stateService.setModifyData([]);
    localStorage.clear();
  }

  createForm() {
    this.searchForm = this.formBuilder.group({
      dateFrom: ['', Validators.required],
      dateTo: ['', Validators.required ],
    }, { validator: this.dateToMoreThanDateFromValidator });
  }


  dateToMoreThanDateFromValidator(control: FormGroup) {
    const dateFromObj = control.get('dateFrom')?.value;
    const dateToObj = control.get('dateTo')?.value;

    if (dateFromObj){
      const dateFrom = new Date(dateFromObj.year, dateFromObj.month - 1, dateFromObj.day);
      console.log("DateFrom:", dateFrom);
    }

    if (dateToObj){
      const dateTo = new Date(dateToObj.year, dateToObj.month - 1, dateToObj.day);
      console.log("DateTo:", dateTo);
    }


    if (dateFromObj && dateToObj) {

      const dateFrom = new Date(dateFromObj.year, dateFromObj.month - 1, dateFromObj.day);
      const dateTo = new Date(dateToObj.year, dateToObj.month - 1, dateToObj.day);

      console.log("DateFrom:", dateFrom);
      console.log("DateTo:", dateTo);

      if (dateTo < dateFrom) {
        console.log("check is true");
        return { dateToMoreThanDateFrom: true };
      }
    }

    console.log("check is null");
    return null;
  }

  get dateFrom() {
    return this.searchForm.get('dateFrom');
  }
  get dateTo() {
    return this.searchForm.get('dateTo');
  }


  search(): void {
    this.mapService.login("", "")
      .subscribe(
        (data) => {
          // Extract JWT token from response, store in localStorage
          localStorage.setItem('token', data.jwt);
          // have the token, call getGameName() function
          const fromDate: Date = this.searchForm.value.dateFrom;
          const toDate: Date = this.searchForm.value.dateTo;
          // Perform search w fromDate and toDate
          console.log("DateFrom: "+fromDate);
          console.log("DateTo: "+toDate);
        const requestData = {
          fromDate: fromDate,
          toDate: toDate,
        };



        this.mapService.sendEventModifyGet(requestData).subscribe(
          (response: mapRequestModify[]) => {
            // Handle response from backend
            console.log('Response from backend:', response);
            // Update component with retrieved data
            this.stateService.setModifyData(response);
          },
          (error) => {
            // Handle errors
            console.error('Error:', error);
          }
        );
        },
        (error) => {
          console.error("Error logging in:", error);
        }
      );
  }

  search1(): void {
    if (this.searchForm.valid) {
      const fromDate: Date = this.searchForm.value.dateFrom;
      const toDate: Date = this.searchForm.value.dateTo;
      console.log("DateFrom: "+fromDate);
      console.log("DateTo: "+toDate);
    const requestData = {
      fromDate: fromDate,
      toDate: toDate,
    };



    this.mapService.sendEventModifyGet(requestData).subscribe(
      (response: mapRequestModify[]) => {
        console.log('Response from backend:', response);
        this.stateService.setModifyData(response);
      },
      (error) => {

        console.error('Error:', error);
      }
    );
    } else {
      // Form invalid, handle accordingly
    }
  }

  searchBoth(dateFrom: Date, dateTo: Date): void {
    const fromDate = this.formatDate(dateFrom);
    const toDate = this.formatDate(dateTo);

    const requestData = {
      fromDate: fromDate,
      toDate: toDate,
    };

    this.mapService.sendEventModifyGet(requestData).subscribe(
      (response: mapRequestModify[]) => {

        console.log('Response from backend:', response);
        // Update your component with retrieved data
      },
      (error) => {
        // Handle errors if have
        console.error('Error:', error);
      }
    );
  }

  formatDate(date: Date): string {
    // Format date before sending backend
    return `${date.getFullYear()}-${this.padZero(
      date.getMonth() + 1
    )}-${this.padZero(date.getDate())}`;
  }

  padZero(num: number): string {
    return num < 10 ? '0' + num : num.toString();
  }
}
