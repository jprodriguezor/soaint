import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class EventsService {

  private subject = new Subject<any>();

  constructor() { }

  public sendMessage<T>(message: T): void {
    this.subject.next(message);
  }

  public clear(): void {
    this.subject.next();
  }

  public onMessage(): Observable<any> {
    return this.subject.asObservable();
  }

}

export enum MessageType {
  LOGIN_DONE, LOGOUT_DONE
}
