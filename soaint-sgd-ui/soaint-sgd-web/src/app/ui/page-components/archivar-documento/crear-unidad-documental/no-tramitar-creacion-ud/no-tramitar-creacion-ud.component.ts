
import {Component, Input, OnChanges, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {SolicitudCreacionUDDto} from "../../../../../domain/solicitudCreacionUDDto";
import {ApiBase} from "../../../../../infrastructure/api/api-base";
import {Observable} from "rxjs/Observable";

@Component({
  selector: 'app-no-tramitar-creacion-ud',
  templateUrl: './no-tramitar-creacion-ud.component.html',
})
export class NoTramitarCreacionUdComponent implements OnChanges {

  form:FormGroup;

  @Input() solicitud:SolicitudCreacionUDDto;

  constructor(private fb:FormBuilder,private _apiService:ApiBase) {

    this.form = fb.group({
      "identificador":[{value:null,disabled:true}],
      "nombre":[{value:null,disabled:true}],
      "descriptor1":[{value:null,disabled:true}],
      "descriptor2":[{value:null,disabled:true}],
      "motivo":null,
      "observaciones":null
    });
  }


ngOnChanges(){ console.log()

   this.form.setValue({
     'identificador':this.solicitud.identificadorUD,
     'nombre':this.solicitud.nombreUD,
     'descriptor1':this.solicitud.descriptor1,
     'descriptor2':this.solicitud.descriptor2,
     'motivo' : null,
     'observaciones': null,
   });
}

sendRequest(){

  this._apiService.post("",{});

}



}
