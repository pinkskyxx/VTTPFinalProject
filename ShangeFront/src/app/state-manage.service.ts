import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { mapRequest, mapRequestModify } from './models';

@Injectable({
  providedIn: 'root'
})
export class StateManageService {

  private dataSubject: BehaviorSubject<mapRequest[]> = new BehaviorSubject<mapRequest[]>([]);
  public data$: Observable<mapRequest[]> = this.dataSubject.asObservable();

  constructor() {}

  addData(newData: mapRequest): void {
    const currentData = this.dataSubject.value;
    this.dataSubject.next([...currentData, newData]);
  }

  deleteData(id: string): void {
    const currentData = this.dataSubject.value;
    const updatedData = currentData.filter(item => item.myid !== id);
    this.dataSubject.next(updatedData);
  }

  setData(newData: mapRequest[]): void {
    this.dataSubject.next(newData);
  }

  private dataModifySubject: BehaviorSubject<mapRequestModify[]> = new BehaviorSubject<mapRequestModify[]>([]);
  public dataModify$: Observable<mapRequestModify[]> = this.dataModifySubject.asObservable();

  addModifyData(newData: mapRequestModify): void {
    const currentData = this.dataModifySubject.value;
    this.dataModifySubject.next([...currentData, newData]);
  }

  deleteModifyData(id: string): void {
    const currentData = this.dataModifySubject.value;
    const updatedData = currentData.filter(item => item.id !== id);
    this.dataModifySubject.next(updatedData);
  }

  setModifyData(newData: mapRequestModify[]): void {
    this.dataModifySubject.next(newData);
  }

}
