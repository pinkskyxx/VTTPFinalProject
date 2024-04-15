import { Component, inject } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  ValidationErrors,
  Validators,
} from '@angular/forms';
import { MyServiceService } from '../my-service.service';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { environment as env } from '../../environments/environment';

@Component({
  selector: 'app-sign-up-organisation',
  templateUrl: './sign-up-organisation.component.html',
  styleUrl: './sign-up-organisation.component.css',
})
export class SignUpOrganisationComponent {
  private fb = inject(FormBuilder);
  private userService = inject(MyServiceService);
  private router = inject(Router);
  readonly loginUrl =  env.loginUrl;
  signUpForm!: FormGroup;
  todayDate = Date.now();
  usernameAvailable?: boolean;
  createNewUserSub?: Subscription;
  showPassword: boolean = false;

  ngOnInit(): void {
    this.createForm();
  }

  get userName() {
    return this.signUpForm.get('userName')!;
  }
  get password() {
    return this.signUpForm.get('password')!;
  }
  get fullName() {
    return this.signUpForm.get('fullName')!;
  }
  get address() {
    return this.signUpForm.get('address')!;
  }
  get dateOfBirth() {
    return this.signUpForm.get('dateOfBirth')!;
  }
  get email() {
    return this.signUpForm.get('email')!;
  }
  get phoneNo() {
    return this.signUpForm.get('phoneNo')!;
  }

  createForm() {
    this.signUpForm = this.fb.group({
      userName: [
        '',
        [
          Validators.required,
          Validators.minLength(5),
          Validators.maxLength(20),
        ],
      ],
      password: [
        '',
        [
          Validators.required,
          Validators.minLength(5),
          Validators.maxLength(20),
        ],
      ],
      fullName: [
        '',
        [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(20),
        ],
      ],
      address: [
        '',
        [
          Validators.required,
          Validators.minLength(5),
          Validators.maxLength(50),
        ],
      ],
      dateOfBirth: [''],
      email: ['', [Validators.required, Validators.email]],
      phoneNo: [
        '',
        [Validators.required, Validators.minLength(8), Validators.maxLength(8)],
      ],
      dateOfSignup: [''],
      gender: [''],
      type: [''],
    });
  }

  checkUsernameAvailability() {
    const username = this.userName.value;
    if (username) {
      this.userService.checkUsernameAvailability(username).subscribe({
        next: (available) => {
          this.usernameAvailable = available;
        },
        error: () => {
        },
      });
    }
  }

  dateOfBirthValidator(control: FormControl): ValidationErrors | null {
    const selectedDate = new Date(control.value);
    const currentDate = new Date();
    currentDate.setHours(0, 0, 0, 0);
    if (selectedDate > currentDate) {
      return { dateOfBirthInvalid: true };
    }
    return null;
  }

  onSubmit() {
    console.log('Submit');

    this.userService.login("", "")
    .subscribe(
      (data) => {
        // Extract JWT token from response, store in localStorage
        localStorage.setItem('token', data.jwt);

        this.createNewUserSub = this.userService
        .signUpOrganisation({
          ...this.signUpForm.value,
          dateOfSignup: new Date(this.todayDate).toISOString() ,
          dateOfBirth: new Date(this.todayDate).toISOString() ,
          gender: 'nil',
          type: 'organization',
        })
        .subscribe({
          next: result => {
            alert("Organization registered successfully");
            setTimeout(() => {
              // window.location.href = 'https://shange.up.railway.app/login';
              // window.location.href = environment.loginUrl;
              window.location.href = env.loginUrl;
              localStorage.clear()
            }, 1000);
          },
          error: (err) => {

            if (err.status === 400 && err.error && err.error.result) {

              alert(err.error.result);
              localStorage.clear();
            } else {

              alert('An error occurred. Please try again later.');
              localStorage.clear();
            }
          },
        });


      },
      (error) => {
        console.error("Error logging in:", error);
      }

    );


  }


  onSubmit1() {
    console.log('Submit');
    this.createNewUserSub = this.userService
      .signUpOrganisation({
        ...this.signUpForm.value,
        dateOfSignup: new Date(this.todayDate).toISOString() ,
        dateOfBirth: new Date(this.todayDate).toISOString() ,
        gender: 'nil',
        type: 'organization',
      })
      .subscribe({
        next: result => {
          alert("Organization registered successfully");
          setTimeout(() => {
            window.location.href = 'https://shange.up.railway.app/login';

          }, 1000);
        },
        error: (err) => {

          if (err.status === 400 && err.error && err.error.result) {

            alert(err.error.result);
          } else {

            alert('An error occurred. Please try again later.');
          }
        },
      });
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  onCheckID() {
    console.log('Check ID');
  }
}
