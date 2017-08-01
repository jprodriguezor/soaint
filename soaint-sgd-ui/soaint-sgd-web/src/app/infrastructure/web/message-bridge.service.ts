import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class MessageBridgeService {

  private subject = new Subject<any>();

  constructor() { }

  public sendMessage<T>(message: T): void {
    this.subject.next(message);
  }

  public clearMessage(): void {
    this.subject.next();
  }

  public getMessage(): Observable<any> {
    return this.subject.asObservable();
  }

}

export enum MessageType {
  LOGIN_DONE, LOGOUT_DONE
}
