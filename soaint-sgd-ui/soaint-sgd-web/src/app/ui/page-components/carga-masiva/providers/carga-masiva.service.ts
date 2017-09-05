/**
 * Created by Ernesto on 2017-09-05.
 */
import { Injectable } from '@angular/core';
import { Headers, Http, RequestOptions } from '@angular/http';

import 'rxjs/add/operator/toPromise';

@Injectable()
export class CargaMasivaService {

  private host = 'http://192.168.1.81:28080';

  constructor(private http: Http) { }

}
