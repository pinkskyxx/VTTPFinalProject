import { Component, inject } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { MyServiceService } from '../my-service.service';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { environment as env } from '../../environments/environment';

@Component({
  selector: 'app-sign-up-volunteer',
  templateUrl: './sign-up-volunteer.component.html',
  styleUrl: './sign-up-volunteer.component.css'
})
export class SignUpVolunteerComponent {

  private fb = inject(FormBuilder);
  private userService = inject(MyServiceService);
  private router = inject(Router);
  readonly loginUrl =  env.loginUrl;

  signUpForm!: FormGroup;
  todayDate = Date.now(); // Corrected to call Date.now as a function
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
      dateOfBirth: ['', [Validators.required, this.dateOfBirthValidator]], // Assign custom validator here
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

  // dateOfBirthValidator(control: FormControl): ValidationErrors | null {
  //   const selectedDate = new Date(control.value);
  //   const currentDate = new Date();
  //   currentDate.setHours(0, 0, 0, 0); // Set hours to 0 for accurate comparison
  //   if (selectedDate > currentDate) {
  //     return { dateOfBirthInvalid: true };
  //   }
  //   return null;
  // }

 dateOfBirthValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    // Ensure the value is in the expected format
    if (value && value.year && value.month && value.day) {
      const selectedDate = new Date(value.year, value.month - 1, value.day);
      const currentDate = new Date();
      currentDate.setHours(0, 0, 0, 0); // Normalize current date to start of day

      if (selectedDate > currentDate) {
        // Selected date is in the future
        return { dateOfBirthInvalid: true };
      }
    }
    return null;
  }

  onSubmit() {
    console.log('Submit');
    console.log(this.signUpForm.value.dateOfBirth);
    console.log('Submit');
    const formatDate = (date: string | number | Date) => {
      const d = new Date(date),
            year = d.getFullYear(),
            month = `${d.getMonth() + 1}`.padStart(2, '0'),
            day = `${d.getDate()}`.padStart(2, '0');

      return `${year}-${month}-${day}`;
    };
    const { year, month, day } = this.signUpForm.value.dateOfBirth;
    const dateOfBirth = new Date(year, month - 1, day);
    const dateOfBirthString = formatDate(dateOfBirth);

    console.log(dateOfBirthString);

    this.userService.login("", "")
    .subscribe(
      (data) => {
        // Extract the JWT token from the response and store it in localStorage
        localStorage.setItem('token', data.jwt);

        this.createNewUserSub = this.userService.signUpVolunteer({
          ...this.signUpForm.value,
          dateOfSignup: new Date(this.todayDate).toISOString() ,
          dateOfBirth: dateOfBirthString,
          type: 'volunteer',
        })
        .subscribe({
          next: result => {
            alert("User registered successfully");
            setTimeout(() => {
              // window.location.href = 'https://shange.up.railway.app/login';
              window.location.href = env.loginUrl;
              localStorage.clear();
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
    console.log(this.signUpForm.value.dateOfBirth);
    console.log('Submit');
    const formatDate = (date: string | number | Date) => {
      const d = new Date(date),
            year = d.getFullYear(),
            month = `${d.getMonth() + 1}`.padStart(2, '0'),
            day = `${d.getDate()}`.padStart(2, '0');

      return `${year}-${month}-${day}`;
    };
    const { year, month, day } = this.signUpForm.value.dateOfBirth;
    const dateOfBirth = new Date(year, month - 1, day);
    const dateOfBirthString = formatDate(dateOfBirth);

    console.log(dateOfBirthString);
    this.createNewUserSub = this.userService.signUpVolunteer({
        ...this.signUpForm.value,
        dateOfSignup: new Date(this.todayDate).toISOString() ,
        dateOfBirth: dateOfBirthString,
        type: 'volunteer',
      })
      .subscribe({
        next: result => {
          alert("User registered successfully");
          setTimeout(() => {
            window.location.href = 'https://shange.up.railway.app/login';
            // window.location.href = environment.loginUrl;

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

}
