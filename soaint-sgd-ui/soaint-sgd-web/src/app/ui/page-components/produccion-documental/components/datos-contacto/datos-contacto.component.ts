import {ChangeDetectorRef, Component, Input, OnDestroy, OnInit, Output, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {Subscription} from 'rxjs/Subscription';
import {PdMessageService} from '../../providers/PdMessageService';
import {TareaDTO} from '../../../../../domain/tareaDTO';
import {StatusDTO} from '../../models/StatusDTO';
import {DestinatarioDTO} from '../../../../../domain/destinatarioDTO';
import {ProduccionDocumentalApiService} from '../../../../../infrastructure/api/produccion-documental.api';
import {AgentDTO} from '../../../../../domain/agentDTO';
import {Sandbox as DependenciaSandbox} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {getArrayData as dependenciaGrupoArrayData} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-selectors';
import {LoadAction as DependenciaLoadAction} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-actions';
import {getArrayData as sedeAdministrativaArrayData} from 'app/infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-selectors';
import {LoadAction as SedeAdministrativaLoadAction} from 'app/infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-actions';

import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';

@Component({
  selector: 'pd-datos-contacto',
  templateUrl: 'datos-contacto.component.html'
})

export class PDDatosContactoComponent implements OnInit, OnDestroy {
  form: FormGroup;
  tipoComunicacionSelected: ConstanteDTO;
  subscription: Subscription;

  validations: any = {};

  remitenteExterno: AgentDTO;
  defaultDestinatarioInterno: any;
  listaDestinatariosInternos: DestinatarioDTO[] = [];

  @ViewChild('datosDestinatarioExterno') datosDestinatarioExterno;
  @ViewChild('destinatarioInterno') destinatarioInterno;
  @Input() taskData: TareaDTO;

  canInsert = false;
  responseToRem = false;
  hasNumberRadicado = false;

  constructor(private formBuilder: FormBuilder, private _store: Store<State>,
              private _changeDetectorRef: ChangeDetectorRef,
              private _produccionDocumentalApi: ProduccionDocumentalApiService,
              private pdMessageService: PdMessageService,
              private _dependenciaSandbox: DependenciaSandbox) {

    this.subscription = this.pdMessageService.getMessage().subscribe(tipoComunicacion => {

      this.tipoComunicacionSelected = tipoComunicacion;
      this.responseToRem = false;
      this.form.get('responderRemitente').setValue(false);

      if (this.taskData.variables.numeroRadicado) {

        this._produccionDocumentalApi.obtenerContactosDestinatarioExterno({
          nroRadicado: this.taskData.variables.numeroRadicado
        }).subscribe(contacto => {

          this.hasNumberRadicado = false;

          if (contacto.codTipoRemite === 'EXT' && this.tipoComunicacionSelected && this.tipoComunicacionSelected.codigo === 'SE') {
            this.hasNumberRadicado = true;
          } else if (contacto.codTipoRemite === 'INT' && this.tipoComunicacionSelected && this.tipoComunicacionSelected.codigo === 'SI') {
            this.hasNumberRadicado = true;
          }

          if (this.tipoComunicacionSelected && this.tipoComunicacionSelected.codigo === 'SE') {
            this.remitenteExterno = contacto;
            this.datosDestinatarioExterno.getDestinatarioDefault(this.remitenteExterno);
          } else if (this.tipoComunicacionSelected && this.tipoComunicacionSelected.codigo === 'SI') {

            this._store.dispatch(new SedeAdministrativaLoadAction());
            this._store.dispatch(new DependenciaLoadAction({codigo: contacto.codDependencia}));

            const sede$ = this._store.select(sedeAdministrativaArrayData)
              .map(sedes => sedes.find((element) => element.codigo === contacto.codSede));

            const dependencia$ = this._store.select(dependenciaGrupoArrayData)
              .map(dependencias => dependencias.find((element) => element.codigo === contacto.codDependencia));

            Observable.combineLatest(sede$, dependencia$, (sede, depedencia) => this.defaultDestinatarioInterno = {
              sede, depedencia
            }).do(_ => console.log('ESTE ES EL TIPO', this.defaultDestinatarioInterno)).subscribe();
          }

        });
      }
    });

    this.initForm();
  }

  ngOnInit(): void {
    this.form.get('responderRemitente').valueChanges.subscribe(responderRemitente => {
      this.responseToRem = responderRemitente;

    });
  }

  updateStatus(currentStatus: StatusDTO) {
    this.form.get('responderRemitente').setValue(currentStatus.datosContacto.responderRemitente);
    this.form.get('distribucion').setValue(currentStatus.datosContacto.distribucion);
    if (currentStatus.datosGenerales.tipoComunicacion.codigo === 'SI') {
      this.listaDestinatariosInternos = [...currentStatus.datosContacto.listaDestinatarios];
    } else if (currentStatus.datosGenerales.tipoComunicacion.codigo === 'SE') {
      this.remitenteExterno = currentStatus.datosContacto.remitenteExterno;
    }
    this.refreshView();
  }

  initForm() {
    this.form = this.formBuilder.group({
      'responderRemitente': [null],
      'distribucion': [null],
    });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  refreshView() {
    this._changeDetectorRef.detectChanges();
  }

}

