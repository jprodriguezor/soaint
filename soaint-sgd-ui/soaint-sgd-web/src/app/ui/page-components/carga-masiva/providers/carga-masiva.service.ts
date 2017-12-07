import {Injectable} from '@angular/core';
import {Headers, Http, Request, RequestMethod, RequestOptions} from '@angular/http';

import {Router} from '@angular/router';
import {ResultUploadDTO} from '../domain/ResultUploadDTO';
import {CargaMasivaDTO} from '../domain/CargaMasivaDTO';
import {Observable} from 'rxjs/Observable';
import {CargaMasivaList} from '../domain/CargaMasivaList';
import {ApiBase} from 'app/infrastructure/api/api-base';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {Store} from '@ngrx/store';
import {environment} from 'environments/environment';
import {State} from '../../../../infrastructure/redux-store/redux-reducers';
import {HttpHandler} from '../../../../infrastructure/utils/http-handler';
import {RequestArgs} from '@angular/http/src/interfaces';

@Injectable()
export class CargaMasivaService {

  private token$: Observable<string>;

  constructor(private router: Router,
              private http: Http,
              private _api: ApiBase,
              protected _http: HttpHandler,
              private _store: Store<RootState>) {
    this.token$ = _store.select(s => s.auth.token);
  }

  uploadFileRequest(url: RequestArgs) {
    return this.token$.take(1).switchMap(token => {
      const options = new RequestOptions();
      options.headers = new Headers();
      if (token !== null) {
        // options.headers.append('Content-Type', 'multipart/form-data');
        // options.headers.append('Accept', 'application/json');
        options.headers.append('Authorization', 'Bearer ' + token);
      } else {
        // options.headers.append('Accept', 'application/json');
        // options.headers.append('Content-Type', 'multipart/form-data');
      }
      const req: Request = new Request(<RequestArgs>url);
      req.headers = options.headers;
      const request$ = this.http.request(req, options);
      return this._http.handleResponse(request$, token);
    });
  }

  // Subir documento para carga masiva
  uploadFile(files: File[], postData: any): Observable<ResultUploadDTO> {
    const formData: FormData = new FormData();
    formData.append('file', files[0], files[0].name);

    if (postData !== '' && postData !== undefined && postData !== null) {
      for (const property in postData) {
        if (postData.hasOwnProperty(property)) {
          formData.append(property, postData[property]);
        }
      }
    }

    const endpoint_cargar_fichero = `${environment.carga_masiva_endpoint_upload}/${postData.codigoSede}/${postData.codigoDependencia}/${postData.funcRadica}`;

    return this.uploadFileRequest({url: endpoint_cargar_fichero, body: formData, method: RequestMethod.Post})
      .map((res: any): ResultUploadDTO => {
        const respuesta = new ResultUploadDTO();
        respuesta.error = res.error;
        respuesta.success = res.success;
        return respuesta;
      });
  }

  // Obtener todos los registros de cargas masivas realizadas
  getRecords(): Observable<CargaMasivaList[]> {

    return this._api.list(environment.carga_masiva_endpoint_listar).map(res => res.cargaMasiva);
  }

  // Obtener el ultimo registro de carga masiva
  getLastRecord(): Observable<CargaMasivaDTO> {

    return this._api.list(environment.carga_masiva_endpoint_estado).map(res => res.correspondencia);
  }

  // Obtener detalles de un registro de carga masiva específico
  getRecord(id: any): Observable<CargaMasivaDTO> {

    if (id === 'last' || isNaN(id)) {
      return this.getLastRecord();
    }

    return this.getRecordById(id);
  }


  getRecordById(id: any): Observable<CargaMasivaDTO> {

    return this._api.list(`${environment.carga_masiva_endpoint_estado}/${id}`).map(res => res.correspondencia);
  }


  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }

}
