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
      "descripto1":[{value:null,disabled:true}],
      "descripto2":[{value:null,disabled:true}],
      "motivo":null,
      "observaciones":null
    });
  }


ngOnChanges(){

   this.form.setValue({'identificador':this.solicitud.identificadorUD});
   this.form.setValue({'nombre':this.solicitud.nombreUD});
   this.form.setValue({'descriptor1':this.solicitud.descriptor1});
   this.form.setValue({'descriptor2':this.solicitud.descriptor2});

}

sendRequest(){

  this._apiService.post("",{});

}



}
