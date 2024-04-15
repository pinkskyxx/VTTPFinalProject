export interface userRegister {
  userName: string;
  password: string;
  fullName: string;
  address: string;
  dateOfBirth: Date;
  email: string;
  phoneNo: string;
  dateOfSignup: Date;
  gender: string;
  type: string;
}

export interface mapRequest {

  address: string;
  locX: number;
  locY: number;
  myid: string;
  title: string;
  description: string
  eventDate: Date;
  requestDate: Date;
  password: string;
  status: boolean;
  reply: string;
}

export interface mapRequestModify {

  id: string
  address: string;
  locX: number;
  locY: number;
  myid: string;
  title: string;
  description: string
  eventDate: Date;
  requestDate: Date;
  password: string;
  status: boolean;
  reply: string;
}

export interface EventDetails {

  strEventId: string;
  strUsername: string;
  strEventtype: string;
  strTitle: string;
  strDescription: string;
  strPhysical: string;
  strOnline: string;
  strNorth: string;
  strSouth: string;
  strEast: string;
  strWest: string;
  strCentral: string;
  strSampleFood: string;
  strSampleFoodSite1: string;
  strSampleFoodSite2: string;
  strQuiz: string;
  strImage: string;
  strFile: string;
  strEventFrom: string;
  strEventTo: string;
  strTimeFrom: string;
  strTimeTo: string;
  strVolunteer: string;
}



