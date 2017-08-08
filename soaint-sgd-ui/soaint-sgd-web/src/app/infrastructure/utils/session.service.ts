import { Injectable } from '@angular/core';
import { LocalStorageService } from 'angular-2-local-storage/dist';

@Injectable()
export class SessionService {

  constructor(private _lss: LocalStorageService) {
  }

  public restoreStatus<T>(key: WebModel, payload: T): T {

    const restoredPayload: T = this._lss.get<T>(key.toString());

    if (restoredPayload === null) {
      this._lss.set(key.toString(), payload);
    }

    return this._lss.get<T>(key.toString());
  }

  public save<T>(key: WebModel, payload: T): void {
    this._lss.set(key.toString(), payload);
  }

  public retrieve<T>(key: WebModel): T {
    return this._lss.get<T>(key.toString())
  }

  public logout(): void {
    this._lss.clearAll();
  }

}

export enum WebModel {
  LOGIN, SECURITY_TOKEN
}
