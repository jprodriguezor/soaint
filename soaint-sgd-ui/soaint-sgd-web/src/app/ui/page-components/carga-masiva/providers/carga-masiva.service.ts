import {Injectable} from '@angular/core';
import {Headers, Http, RequestOptions} from '@angular/http';

import {Router} from '@angular/router';
import {ResultUploadDTO} from '../domain/ResultUploadDTO';
import {CargaMasivaDTO} from '../domain/CargaMasivaDTO';
import {Observable} from 'rxjs/Observable';
import {CargaMasivaList} from "../domain/CargaMasivaList";
import {ApiBase} from "../../../../infrastructure/api/api-base";
import {State as RootState} from '../../../../infrastructure/redux-store/redux-reducers';
import {Store} from '@ngrx/store';
import {environment} from "../../../../../environments/environment";

@Injectable()
export class CargaMasivaService {

  private host = 'http://192.168.1.81:28080/soaint-sgd-web-api-gateway/apis/carga-masiva-gateway-api';

  constructor(private router: Router, private http: Http, private _api: ApiBase, private _store: Store<RootState>) {
  }

  // Subir documento para carga masiva
  uploadFile(files: File[], postData: any): Promise<ResultUploadDTO> {
      const headers = new Headers();
      const formData: FormData = new FormData();
      formData.append('file', files[0], files[0].name);

      if (postData !== '' && postData !== undefined && postData !== null) {
          for (const property in postData) {
              if (postData.hasOwnProperty(property)) {
                formData.append(property, postData[property]);
              }
          }
      }

      return this.http.post(this.host + '/upload', formData, {headers: headers})
          .toPromise().then(res => res.json() as ResultUploadDTO)
          .catch(this.handleError);

  }

  // Obtener todos los registros de cargas masivas realizadas
  getRecords(): Observable<CargaMasivaList[]> {

      return this._api.list(`${environment.carga_masiva_endpoint_listar}`).map(res => res.cargaMasiva);
  }

  // Obtener el ultimo registro de carga masiva
  getLastRecord(): Observable<CargaMasivaDTO> {

      return this._api.list(`${environment.carga_masiva_endpoint_estado}`).map(res => res.correspondencia);
  }

  // Obtener detalles de un registro de carga masiva específico
  getRecord(id: any): Observable<CargaMasivaDTO> {

      if (id == 'last' || isNaN(id)) {
          return this.getLastRecord();
      }

      return this.getRecordById(id);
  }


  getRecordById(id: any) : Observable<CargaMasivaDTO> {

      return this._api.list(`${environment.carga_masiva_endpoint_estado}/${id}`).map(res => res.correspondencia);
  }


  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }

}
