import {Component, Input, OnInit, Injectable} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Observable} from 'rxjs/Observable';
import 'rxjs/operator/filter';
import 'rxjs/add/operator/zip';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {Sandbox as ConstanteSandbox} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';
import {Sandbox as DependenciaGrupoSandbox} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {LoadAction as DependenciaGrupoLoadAction} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-actions';
import {DESTINATARIO_PRINCIPAL} from '../../../shared/bussiness-properties/radicacion-properties';
import {ConfirmationService} from 'primeng/components/common/api';
import {OrganigramaDTO} from '../../../domain/organigramaDTO';
import {Subscription} from 'rxjs/Subscription';
import {PushNotificationAction} from '../../../infrastructure/state-management/notifications-state/notifications-actions';
import {WARN_DESTINATARIOS_REPETIDOS} from '../../../shared/lang/es';
import { getDestinatarioPrincial } from '../../../infrastructure/state-management/constanteDTO-state/constanteDTO-selectors';
import { sedeDestinatarioEntradaSelector, tipoDestinatarioEntradaSelector } from '../../../infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-selectors';
import { ConstanteApiService } from '../../../infrastructure/api/constantes.api';
import { DependenciaApiService } from '../../../infrastructure/api/dependencia.api';
import { DependenciaDTO } from '../../../domain/dependenciaDTO';

@Injectable()
export class DatosDestinatarioStateService {

    form: FormGroup;

    selectedagenteDestinatario: any;
    canInsert = false;
    agentesDestinatario: Array<{ tipoDestinatario: ConstanteDTO, sedeAdministrativa: ConstanteDTO, dependenciaGrupo: ConstanteDTO, ideAgente?: number }> = [];
    idAgenteDevolucion: number;

    sedeAdministrativaInput$: Observable<ConstanteDTO[]> = Observable.empty();
    tipoDestinatarioInput$: Observable<ConstanteDTO[]> = Observable.empty();
    dependenciaGrupoInput$: Observable<ConstanteDTO[]> = Observable.empty();

    ListadoDependencias: DependenciaDTO[] = null;

    disabled = true;

    constructor(private _store: Store<State>,
                private confirmationService: ConfirmationService,
                private _dependenciaGrupoSandbox: DependenciaGrupoSandbox,
                private formBuilder: FormBuilder,
                private constanteService: ConstanteApiService,
                private _dependenciaService: DependenciaApiService
              ) {
    }

    Init(): void {
      this.initForm();
      this.LoadData();
      this.Subcripciones();
    }

    initForm() {
        this.form = this.formBuilder.group({
          'tipoDestinatario': [{value: null, disabled: !this.disabled}],
          'sedeAdministrativa': [{value: null, disabled: !this.disabled}],
          'dependenciaGrupo': [{value: null, disabled: !this.disabled}],
          'destinatarioPrincipal': [{value: null, disabled: !this.disabled}, Validators.required],
        });

      if (!this.disabled) {
        this.form.disable();
      }

    }

    LoadData() {
      // buscar nombre grupo dependencia y sede
      this._dependenciaService.Listar({}).subscribe(result => {
        this.agentesDestinatario = this.agentesDestinatario
        .reduce((_listado, _grupo) => {
          const dependencia = result.find(_item => _item.codigo === _grupo.dependenciaGrupo.codigo && _item.codSede === _grupo.sedeAdministrativa.codigo);
          _grupo.sedeAdministrativa.nombre = dependencia.nomSede;
          _grupo.dependenciaGrupo.nombre = dependencia.nombre;
          _listado.push(_grupo);
          return _listado
        }, []);
      });

      // buscar nombre tipo destinatario
      this.tipoDestinatarioInput$ = this.constanteService.Listar({key: 'tipoDestinatario'});
      this.tipoDestinatarioInput$.subscribe( result => {
        this.agentesDestinatario = this.agentesDestinatario
        .reduce((_listado, _tipo) => {
          if (_tipo.tipoDestinatario.codigo === DESTINATARIO_PRINCIPAL) {
            this.form.get('destinatarioPrincipal').setValue({
              tipoDestinatario: _tipo.tipoDestinatario,
              sedeAdministrativa: _tipo.sedeAdministrativa,
              dependenciaGrupo: _tipo.dependenciaGrupo
            });
          }
          const destinatario = result.find(_item => _item.codigo === _tipo.tipoDestinatario.codigo);
          _tipo.tipoDestinatario.nombre = destinatario.nombre;
          _listado.push(_tipo);
          return _listado
        }, []);
      });
      this.sedeAdministrativaInput$ = this._store.select(sedeDestinatarioEntradaSelector);

      this._store.select(getDestinatarioPrincial)
      .subscribe(result => {
        this.form.value.tipoDestinatario = result;
      });
     }

    Subcripciones() {

      Observable.combineLatest(
        this.form.get('tipoDestinatario').valueChanges,
        this.form.get('sedeAdministrativa').valueChanges,
        this.form.get('dependenciaGrupo').valueChanges
      ).do(() => this.canInsert = false)
        .filter(([tipo, sede, grupo]) => tipo && sede && grupo)
        .zip(([tipo, sede, grupo]) => {
          return {tipo: tipo, sede: sede, grupo: grupo}
        })
        .subscribe(value => {
          this.canInsert = true
        });

    }

    LoadDependencias(sede: any) {
      sede = sede.value;
      const grupoControl = this.form.get('dependenciaGrupo');
      grupoControl.disable();

        if (this.disabled && sede) {
          grupoControl.enable();
          this.form.get('dependenciaGrupo').reset();
          this.dependenciaGrupoInput$ = this._dependenciaGrupoSandbox.loadData({codigo: sede.id})
          .map(_map => _map.organigrama);
        } else {
          grupoControl.disable();
        }
    }

    addAgentesDestinatario() {
      const tipo = this.form.get('tipoDestinatario');
      const sede = this.form.get('sedeAdministrativa');
      const grupo = this.form.get('dependenciaGrupo');

      if (this.agentesDestinatario.filter(value => value.sedeAdministrativa.codigo === sede.value.codigo && value.dependenciaGrupo.codigo === grupo.value.codigo).length > 0) {
        return  this._store.dispatch(new PushNotificationAction({
          severity: 'warn',
          summary: WARN_DESTINATARIOS_REPETIDOS
        }));
      }

      if (tipo.value.codigo === DESTINATARIO_PRINCIPAL && this.agentesDestinatario.filter(value => value.tipoDestinatario.codigo === DESTINATARIO_PRINCIPAL).length > 0) {
        return this.confirmSubstitucionDestinatarioPrincipal();
      }

      const insertVal = [
        {
          tipoDestinatario: tipo.value,
          sedeAdministrativa: sede.value,
          dependenciaGrupo: grupo.value
        }
      ];

      if (tipo.value.codigo === DESTINATARIO_PRINCIPAL) {
        this.form.get('destinatarioPrincipal').setValue({
          tipoDestinatario: tipo.value,
          sedeAdministrativa: sede.value,
          dependenciaGrupo: grupo.value
        });
      }

      this.agentesDestinatario = [
        ...insertVal,
        ...this.agentesDestinatario.filter(
          value => value.sedeAdministrativa !== sede.value || value.dependenciaGrupo !== grupo.value
        )
      ];

      tipo.reset();
      sede.reset();
      grupo.reset();
    }

    substitudeAgenteDestinatario() {

      const tipo = this.form.get('tipoDestinatario');
      const sede = this.form.get('sedeAdministrativa');
      const grupo = this.form.get('dependenciaGrupo');

      const insertVal = [
        {
          tipoDestinatario: tipo.value,
          sedeAdministrativa: sede.value,
          dependenciaGrupo: grupo.value
        }
      ];

      this.agentesDestinatario = [
        ...insertVal,
        ...this.agentesDestinatario.filter(
          value => value.tipoDestinatario.codigo !== DESTINATARIO_PRINCIPAL
        )
      ];

      tipo.reset();
      sede.reset();
      grupo.reset();
    }

    deleteAgentesDestinatario(index) {

      if (this.agentesDestinatario[index].tipoDestinatario.codigo === DESTINATARIO_PRINCIPAL) {
        this.form.get('destinatarioPrincipal').setValue(null);
      }

      const agente = [...this.agentesDestinatario];
      agente.splice(index, 1);
      this.agentesDestinatario = agente;
    }

    EsAgenteDevolucion(index: any) {
      const valido = (
        !this.agentesDestinatario[index].ideAgente  || //nuevos agentes
        this.agentesDestinatario[index].ideAgente == this.idAgenteDevolucion);
      return valido;
    }

    confirmSubstitucionDestinatarioPrincipal() {
      this.confirmationService.confirm({
        message: '<p style="text-align: center"> Está seguro desea substituir el destinatario principal?</p>',
        accept: () => {
          this.substitudeAgenteDestinatario();
        }
      });
    }

    deleteDestinatarioIqualRemitente(sedeRemitente: OrganigramaDTO) {
      const before =  this.agentesDestinatario.length;
      const filtered = this.agentesDestinatario.filter(value => value.sedeAdministrativa.codigo !== sedeRemitente.codigo);
      if (before > filtered.length) {
        this.agentesDestinatario = [...filtered];
        this.form.get('destinatarioPrincipal').setValue(null);
      }
    }

}
