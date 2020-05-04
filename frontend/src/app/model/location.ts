import {User} from "./user";

export interface Location {
    lat: number,
    lng: number,
    date: string,
    user: User
}
