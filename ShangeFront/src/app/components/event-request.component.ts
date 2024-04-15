import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { Loader } from '@googlemaps/js-api-loader';
import { environment } from '../../environments/environment';
import { mapRequest } from '../models';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { StateManageService } from '../state-manage.service';

@Component({
  selector: 'app-event-request',
  templateUrl: './event-request.component.html',
  styleUrl: './event-request.component.css'
})
export class EventRequestComponent implements OnInit, OnDestroy{

  newData: mapRequest = { address: '', myid: '', eventDate: new Date(), requestDate: new Date(), title: '', description: '', password: '', locX: 0, locY: 0, status:false, reply:'' };

  dataForm!: FormGroup;
  times: string[] = [];
  today!: string;
  showPassword: boolean = false;

  markerPositions: any;
  lat = 1.3;
  lng = 103.8;
  display: any;
  map: any;

  private formBuilder=inject(FormBuilder)
  private stateService=inject(StateManageService)


  ngOnInit(): void {
    this.loadGoogleMapsScript();
    this.today = new Date().toISOString().split('T')[0];
    this.initializeForm();
  }
ngOnDestroy(): void {

  this.stateService.setData([]);
}
  initializeForm(): void {
    this.dataForm = this.formBuilder.group({
      address: ['', Validators.required],
      locX: [''],
      locY: [''],
      eventDate: ['', [Validators.required, this.dateMoreThanTodayValidator]],
      requestDate: [this.today],
      myid: [''],
      status: [false],
      password: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(6)]],
      title: ['',  [Validators.required, Validators.minLength(8), Validators.maxLength(60)]],
      description: ['',  [Validators.required, Validators.minLength(8), Validators.maxLength(250)]]
    });
  }

  dateMoreThanTodayValidator(control: any): { [key: string]: any } | null {
    const year = control.value?.year;
    const month = control.value?.month;
    const day = control.value?.day;

    if (year && month && day) {
      const selectedDate = new Date(+year, +month - 1, +day);
      const today = new Date();

      if (selectedDate < today) {
        return { 'dateMoreThanToday': true };
      }
    }
    return null;
  }
  onDateSelect(): void {
    if (this.eventDate) {
      this.eventDate.updateValueAndValidity();
    }
  }
  get address() {
    return this.dataForm.get('address')!;
  }
  get locX() {
    return this.dataForm.get('locX')!;
  }
  get locY() {
    return this.dataForm.get('locY')!;
  }
  get eventDate() {
    return this.dataForm.get('eventDate')!;
  }
  get requestDate() {
    return this.dataForm.get('requestDate')!;
  }
  get password() {
    return this.dataForm.get('password')!;
  }
  get title() {
    return this.dataForm.get('title')!;
  }
  get description() {
    return this.dataForm.get('description')!;
  }

  private padNumber(num: number): string {
    return num < 10 ? '0' + num : num.toString();
  }

  onAdd(){
    const currentDate = new Date();
    const id = `${currentDate.getFullYear()}${this.padNumber(currentDate.getMonth() + 1)}${this.padNumber(currentDate.getDate())}${this.padNumber(currentDate.getHours())}${this.padNumber(currentDate.getMinutes())}${this.padNumber(currentDate.getSeconds())}`;
    this.newData.myid = id;

  this.newData.address = this.dataForm.value.address;
  this.newData.description = this.dataForm.value.description;

  this.newData.locX = this.dataForm.value.locX;
  this.newData.locY = this.dataForm.value.locY;
  this.newData.password = this.dataForm.value.password;
  this.newData.status = false;

  this.newData.title = this.dataForm.value.title;
  console.log("all data eventDate: " + this.dataForm.value.eventDate)
  console.log("all data requestDate: " + this.dataForm.value.requestDate)

 if (typeof this.dataForm.value.eventDate === 'object') {
  this.newData.eventDate = this.parseDate(this.dataForm.value.eventDate);
} else {
  this.newData.eventDate = new Date(this.dataForm.value.eventDate);
}

if (typeof this.dataForm.value.requestDate === 'object') {
  this.newData.requestDate = this.parseDate(this.dataForm.value.requestDate);
} else {
  this.newData.requestDate = new Date(this.dataForm.value.requestDate);
}

    console.log("all data eventDate: " + this.newData.eventDate)
    console.log("all data requestDate: " + this.newData.requestDate)
    this.stateService.addData(this.newData);
    this.resetForm();
  }

  parseDate(dateObject: any): Date {
    if (dateObject && typeof dateObject === 'object' && dateObject.year && dateObject.month && dateObject.day) {
      return new Date(dateObject.year, dateObject.month - 1, dateObject.day);
    } else {
      return dateObject;
    }
  }


  resetForm(): void {
    this.dataForm.reset();
    this.dataForm.get('requestDate')!.setValue(new Date());
    this.newData = { address: '', myid: '', eventDate: new Date(), requestDate: new Date(), title: '', description: '', password: '', locX: 0, locY: 0, status: false, reply:'' };

  }
  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }
  loadGoogleMapsScript(): void {
    const loader = new Loader({
      apiKey: environment.googleMapApi,
      version: 'weekly',
      libraries: ['geometry', 'places']
    });

    loader.load().then(() => {
      console.log('Google Maps API loaded successfully');
      this.initMap();
    }).catch((error) => {
      console.error('Error loading Google Maps API:', error);
    });
  }

  public initMap() {
    const mapOptions: google.maps.MapOptions = {
      center: { lat: this.lat, lng: this.lng },
      zoom: 12,
    };

    const mapElement = document.getElementById('map');
    if (mapElement) {
      this.map = new google.maps.Map(mapElement, mapOptions);

      this.map.addListener(
        'mousemove',
        (event: { latLng: { toJSON: () => any } }) => {
          this.display = event.latLng.toJSON();
        }
      );

    this.map.addListener('click', (event: { latLng: { toJSON: () => any; }; }) => {
      this.addMarker(event.latLng.toJSON());
    });


    } else {
      console.error("Map element with id 'map' not found.");
    }

  }


addMarker(position: google.maps.LatLngLiteral) {

  const geocoder = new google.maps.Geocoder();

  const latLng = new google.maps.LatLng(position.lat, position.lng);

  //reverse geocoding to get address
  geocoder.geocode({ location: latLng }, (results, status) => {
    if (status === 'OK') {
      if (results[0]) {
        // Extract ormatted address fr first result
        const address = results[0].formatted_address;

        console.log('Marker Address:', address,' ' ,results.length);
        results.forEach(element => {
          console.log('Marker Address:=>', element);
        });

        this.dataForm.patchValue({
          address: address,
          locX: position.lat,
          locY: position.lng
        });
      } else {
        console.error('No results found');
      }
    } else {
      console.error('Geocoder failed due to:', status);
    }
  });

  new google.maps.Marker({
    position: position,
    map: this.map
  });
}

}
