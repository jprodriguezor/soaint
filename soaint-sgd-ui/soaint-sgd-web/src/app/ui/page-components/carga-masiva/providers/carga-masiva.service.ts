import { Injectable } from '@angular/core';
import { Headers, Http, RequestOptions } from '@angular/http';

import 'rxjs/add/operator/toPromise';
import {Router} from "@angular/router";
import {ResultUploadDTO} from "../domain/ResultUploadDTO";

@Injectable()
export class CargaMasivaService {

  private host = 'http://heroes.local/document/upload';

  constructor(private router: Router, private http: Http) { }

  uploadFile(files: File[], postData : any) : Promise<ResultUploadDTO> {
    let headers = new Headers();
    let formData:FormData = new FormData();
    formData.append('file', files[0], files[0].name);

    if(postData !=="" && postData !== undefined && postData !==null){
      for (let property in postData) {
        if (postData.hasOwnProperty(property)) {
          formData.append(property, postData[property]);
        }
      }
    }

    return this.http.post(this.host, formData, {headers: headers})
      .toPromise().then(response => response.json() as ResultUploadDTO)
      .catch(this.handleError);

  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }

  getHost() : string {
     return this.host;
  }

}
