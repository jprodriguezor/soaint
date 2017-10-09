import {Component, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from "@angular/forms";
import {Observable} from "rxjs/Observable";
import {ConstanteDTO} from "../../../../../domain/constanteDTO";
import {ProduccionDocumentalApiService} from "app/infrastructure/api/produccion-documental.api";

@Component({
  selector: 'pd-gestionar-produccion',
  templateUrl: './gestionar-produccion.component.html'
})

export class PDGestionarProduccionComponent implements OnInit{

  form: FormGroup;
  listaDocumentos: Array<{ sede: string, dependencia: string, rol: string, funcionario: string }> = [];
  sedesAdministrativas$ : Observable<ConstanteDTO[]>;
  dependencias$ : Observable<ConstanteDTO[]>;
  roles$ : Observable<ConstanteDTO[]>;
  funcionarios$ : Observable<ConstanteDTO[]>;


  constructor(private _produccionDocumentalApi : ProduccionDocumentalApiService, private formBuilder: FormBuilder){}


  initForm() {
      this.form = this.formBuilder.group({
          'sede': [{value: null}],
          'dependencia': [{value: null}],
          'rol': [{value: null}],
          'funcionario': [{value: null}]
      });
  }

  ngOnInit(): void {
    this.sedesAdministrativas$ = this._produccionDocumentalApi.getSedes({});
    this.dependencias$ = this._produccionDocumentalApi.getDependencias({});

      this.initForm();
  }
}

