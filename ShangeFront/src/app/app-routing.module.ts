import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainPageComponent } from './components/main-page.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { SignUpVolunteerComponent } from './components/sign-up-volunteer.component';
import { SignUpOrganisationComponent } from './components/sign-up-organisation.component';
import { EventRequestComponent } from './components/event-request.component';
import { EventAlterComponent } from './components/event-alter.component';

const routes: Routes = [

  {path: '', component: MainPageComponent, title: 'SHANGE Website'},
  {path: 'Home', component: MainPageComponent, title: 'SHANGE Website'},
  {path: 'SignUpVolunteer', component: SignUpVolunteerComponent, title: 'SignUp Volunteer'},
  {path: 'SignUpOrganisation', component: SignUpOrganisationComponent, title: 'SignUp Organisation'},
  {path: 'RequestEvent', component: EventRequestComponent, title: 'Request Event'},
  {path: 'RequestAlter', component: EventAlterComponent, title: 'Request Modify Event'},
  // {path: 'login', component: MyLoginComponent, title: 'Ecommerce Website'},
  // {path: 'mainOrganization', component: MyOrganizationComponent, title: 'Product List'},
  // {path: 'mainVolunteer', component: MyVolunteerComponent, title: 'Product List'},
  // // { path: 'category/:category', component: CategoryComponent, data: { title: 'Product List' } },
  // {path: 'logout', component: MyLogoutComponent, title: 'Check Out'},
  // {path: '', redirectTo: '/', pathMatch: 'full' },
  // {path: '**', redirectTo: '/', pathMatch: 'full'}
  {path: '**', component: NotFoundComponent},

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
