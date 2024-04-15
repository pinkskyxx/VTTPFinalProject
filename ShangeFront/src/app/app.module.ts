import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MenuBarComponent } from './components/menu-bar.component';
import { MainPageComponent } from './components/main-page.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { SignUpOrganisationComponent } from './components/sign-up-organisation.component';
import { SignUpVolunteerComponent } from './components/sign-up-volunteer.component';
import { EventRequestComponent } from './components/event-request.component';
import { MyServiceService } from './my-service.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { NgbDatepickerModule, NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { EventRequestDisplayComponent } from './components/event-request-display.component';
import { StateManageService } from './state-manage.service';
import { EventAlterComponent } from './components/event-alter.component';
import { EventAlterDisplayComponent } from './components/event-alter-display.component';

@NgModule({
  declarations: [
    AppComponent,
    MenuBarComponent,
    MainPageComponent,
    NotFoundComponent,
    SignUpOrganisationComponent,
    SignUpVolunteerComponent,

    EventRequestComponent,
      EventRequestDisplayComponent,
      EventAlterComponent,
      EventAlterDisplayComponent
  ],
  imports: [
    BrowserModule,
    FormsModule, ReactiveFormsModule,  HttpClientModule, NgbModule, NgbDatepickerModule,
    AppRoutingModule
  ],
  providers: [MyServiceService,StateManageService],
  bootstrap: [AppComponent]
})
export class AppModule { }
