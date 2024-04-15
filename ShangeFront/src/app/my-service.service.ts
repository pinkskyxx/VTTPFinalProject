import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { EventDetails, mapRequest, mapRequestModify, userRegister } from './models';
import { environment as env } from '../environments/environment'
@Injectable({
  providedIn: 'root'
})
export class MyServiceService {

  readonly apiUrl =  env.url;
  readonly jwtID =  env.loginID;
  readonly jwtPW =  env.loginPW;
  constructor() { }

  private http=inject(HttpClient)

  login(username: string, password: string): Observable<any> {
    username=this.jwtID;
    password=this.jwtPW;
    return this.http.post<any>(`${this.apiUrl}/authenticate`, { username, password });
  }

  checkUsernameAvailability(username: string): Observable<any> {
    return this.http.get(`/api/username/check?username=${username}`);
  }

  signUpOrganisation(details: userRegister): Observable<userRegister> {
    const formData = new FormData();
    console.log("details.dateOfBirth: "+details.dateOfBirth)
    console.log("details.dateOfSignup"+ details.dateOfSignup)

    const headers = new HttpHeaders()
    .set('Content-Type', 'application/json')
    .set('Authorization', 'Bearer ' + localStorage.getItem('token'));

    return this.http.post<userRegister>(`${this.apiUrl}/api/signUpOrganisation`, details, { headers });
  }

  signUpVolunteer(details: userRegister): Observable<userRegister> {
    const formData = new FormData();
    console.log("details.dateOfBirth: "+details.dateOfBirth)
    console.log("details.dateOfSignup"+ details.dateOfSignup)

    const headers = new HttpHeaders()
    .set('Content-Type', 'application/json')
    .set('Authorization', 'Bearer ' + localStorage.getItem('token'));

    return this.http.post<userRegister>(`${this.apiUrl}/api/signUpVolunteer`, details, { headers });
  }

  sendEventRequest(details: mapRequest[]): Observable<mapRequest[]> {
    console.log(details)
    // const headers = new HttpHeaders().set('Content-Type', 'application/json');
    const headers = new HttpHeaders()
    .set('Content-Type', 'application/json')
    .set('Authorization', 'Bearer ' + localStorage.getItem('token'));
    return this.http.post<mapRequest[]>(`${this.apiUrl}/api/mapEventRequest`, details, { headers });
  }

  private formatDate(date: Date | string): string {
    if (typeof date === 'string') {
      return date;
    } else {
      const d = new Date(date);
      const year = d.getFullYear();
      const month = ('0' + (d.getMonth() + 1)).slice(-2);
      const day = ('0' + d.getDate()).slice(-2);
      return `${year}-${month}-${day}`;
    }
  }

  sendEventModifyPost(requestData: any): Observable<mapRequestModify[]> {
    const headers = { 'Content-Type': 'application/json' };
    return this.http.post<mapRequestModify[]>(`${this.apiUrl}/api/mapEventRequest`, requestData, { headers });
  }

  sendEventModifyGet(requestData: any): Observable<mapRequestModify[]> {
    console.log(requestData)
    console.log("Datefrom: " + requestData.fromDate)
    console.log("Dateto: " + requestData.toDate)
    const params = new HttpParams()
      .set('fromDate', this.formatDateM(requestData.fromDate))
      .set('toDate', this.formatDateM(requestData.toDate));
      let headers = {
        'Authorization': 'Bearer ' + localStorage.getItem('token')
      };
    return this.http.get<mapRequestModify[]>(`${this.apiUrl}/api/mapEventRequestModify`, { headers, params });
  }

  sendAllEventGet(): Observable<EventDetails[]> {
      let headers = {
        'Authorization': 'Bearer ' + localStorage.getItem('token')
      };
    return this.http.get<EventDetails[]>(`${this.apiUrl}/api/EventDetailsAll`, { headers });
  }

  formatDateM(dateObj: { year: number, month: number, day: number } | undefined): string {
    if (!dateObj) {
      console.error('formatDateM called with undefined dateObj');
      return ''; // Or return a default/fallback date string
    }

    const pad = (num: number) => num.toString().padStart(2, '0');
    return `${dateObj.year}-${pad(dateObj.month)}-${pad(dateObj.day)}`;
  }

  deleteMapRequestModifyById(id: string): Observable<void> {
    let headers = {
      'Authorization': 'Bearer ' + localStorage.getItem('token')
    };
    return this.http.delete<void>(`${this.apiUrl}/api/mapEventRequestModify/delete/${id}`, { headers });
  }

  putMapRequestModifyById(id: string, requestbody: any): Observable<void> {
    const headers = new HttpHeaders()
    .set('Content-Type', 'application/json')
    .set('Authorization', 'Bearer ' + localStorage.getItem('token'));
    return this.http.put<void>(`${this.apiUrl}/api/mapEventRequestModify/update/${id}`, requestbody, { headers });

  }
}
