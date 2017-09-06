import {Injectable} from '@angular/core';
import {Headers, Http, RequestOptions} from '@angular/http';

import 'rxjs/add/operator/toPromise';
import {Router} from '@angular/router';
import {ResultUploadDTO} from '../domain/ResultUploadDTO';
import {CargaMasivaDTO} from '../domain/CargaMasivaDTO';
import {Observable} from 'rxjs';

@Injectable()
export class CargaMasivaService {

  private host = 'http://heroes.local/Massive-Loader';

  constructor(private router: Router, private http: Http) {
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
      .toPromise().then(response => response.json() as ResultUploadDTO)
      .catch(this.handleError);

  }

  // Obtener todos los registros de cargas masivas realizadas
  getRecords(): Observable<CargaMasivaDTO[]> {
    return this.http.get(this.host + '/listadocargamasiva').map(res => res.json())
      .catch(this.handleError);
  }

  // Obtener el ultimo registro de carga masiva
  getLastRecord(): Promise<CargaMasivaDTO> {


    return this.http.get(this.host + '/estadocargamasiva').toPromise().then().catch(this.handleError);
  }

  // Obtener detalles de un registro de carga masiva específico
  getDetails(id: string): Promise<CargaMasivaDTO> {


    return this.http.get(`${this.host}/estadocargamasiva/${id}`).toPromise().then().catch(this.handleError);
  }


  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }

}
