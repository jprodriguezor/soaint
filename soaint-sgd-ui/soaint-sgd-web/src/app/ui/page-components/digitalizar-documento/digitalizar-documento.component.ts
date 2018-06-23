import {ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {ApiBase} from '../../../infrastructure/api/api-base';
import {environment} from '../../../../environments/environment';
import {State as RootState} from '../../../infrastructure/redux-store/redux-reducers';
import {Store} from '@ngrx/store';
import {CompleteTaskAction} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-actions';
import {getActiveTask} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {Subscription} from 'rxjs/Subscription';
import {Sandbox as AsignacionSandbox} from '../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-sandbox';
import {CorrespondenciaDTO} from '../../../domain/correspondenciaDTO';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/switchMap';
import {FAIL_ADJUNTAR_PRINCIPAL, SUCCESS_ADJUNTAR_DOCUMENTO, FAIL_ADJUNTAR_ANEXOS} from '../../../shared/lang/es';
import {PushNotificationAction} from '../../../infrastructure/state-management/notifications-state/notifications-actions';
import {isArray, isNullOrUndefined} from 'util';
import {ComunicacionOficialDTO} from '../../../domain/comunicacionOficialDTO';
import {empty} from 'rxjs/Observer';
import * as codigos from '../../../shared/bussiness-properties/radicacion-properties';
import {FileUpload} from "primeng/primeng";

enum UploadStatus {
  CLEAN = 0,
  LOADED = 1,
  UPLOADING = 2,
  UPLOADED = 3,
}

@Component({
  selector: 'app-digitalizar-documento',
  templateUrl: './digitalizar-documento.component.html',
  styleUrls: ['./digitalizar-documento.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class DigitalizarDocumentoComponent implements OnInit, OnDestroy {

  uploadFiles: any[] = [];
  task: any;
  url: string;
  status: UploadStatus;
  previewWasRefreshed = false;
  uploadUrl: string;
  uploadDisabled = false;
  principalFile: string;
  principalFileId = null;
  correspondencia: CorrespondenciaDTO;
  comunicacion: ComunicacionOficialDTO = {};

  tipoSoporteElectronico = false;

  activeTaskUnsubscriber: Subscription;

  @ViewChild('uploader') uploader;
  @ViewChild('viewer') viewer;

  constructor(private changeDetection: ChangeDetectorRef,
              private _api: ApiBase,
              private _asignacionSandBox: AsignacionSandbox,
              private _store: Store<RootState>) {
  }

  ngOnInit() {
    this.uploadUrl = environment.digitalizar_doc_upload_endpoint;
    this.activeTaskUnsubscriber = this._store.select(getActiveTask).subscribe(activeTask => {
      this.task = activeTask;
      if (this.task) {
        this._asignacionSandBox.obtenerComunicacionPorNroRadicado(this.task.variables.numeroRadicado).subscribe((comunicacion) => {
          this.comunicacion = comunicacion;
          this.correspondencia = comunicacion.correspondencia;
        });
      }
    });
  }

  showUploadButton() {
    return this.status === UploadStatus.CLEAN;
  }

  customUploader(event) {

    const formData = new FormData();
    for (const file of event.files) {
      formData.append('files', file, file.name);
    }

    this.comunicacion.anexoList.forEach(value => {
      if (value.codTipoSoporte === 'TP-SOPE') {
        this.tipoSoporteElectronico = true;
      }
    });

    if (isNullOrUndefined(this.principalFile)) {

      this._store.dispatch(new PushNotificationAction({
        severity: 'warn',
        summary: FAIL_ADJUNTAR_PRINCIPAL
      }));

    } else if (!this.tipoSoporteElectronico && event.files.length > 1) {

      this._store.dispatch(new PushNotificationAction({
        severity: 'warn',
        summary: FAIL_ADJUNTAR_ANEXOS
      }));

    } else {
      let _dependencia;
      this._asignacionSandBox.obtnerDependenciasPorCodigos(this.correspondencia.codDependencia).switchMap((result) => {
          _dependencia = result.dependencias[0];
          let _agente = this.comunicacion.agenteList.find(a => a.codTipAgent === codigos.TIPO_AGENTE_REMITENTE);
          formData.append('tipoComunicacion', this.correspondencia.codTipoCmc);
          formData.append('nroRadicado', this.correspondencia.nroRadicado);
          formData.append('principalFileName', this.principalFile);
          if(_dependencia) {
            formData.append('sede', _dependencia.nomSede);
            formData.append('codigoSede', _dependencia.codSede);
            formData.append('dependencia', _dependencia.nombre);
            formData.append('codigoDependencia', _dependencia.codigo);
          }
          if(_agente)
            formData.append('nombreRemitente', _agente.nombre);
          return this._api.sendFile(
            this.uploadUrl, formData, []);
        }
      ).subscribe(response => {
        const data = response;
        console.log(response);
        if (isArray(data)) {
          if (data.length === 0) {
            this._store.dispatch(new PushNotificationAction({
              severity: 'error', summary: 'NO ADJUNTO, NO PUEDE ADJUNTAR EL DOCUMENTO'
            }));
          } else {
            this._store.dispatch(new CompleteTaskAction({
              idProceso: this.task.idProceso, idDespliegue: this.task.idDespliegue,
              idTarea: this.task.idTarea, parametros: {ideEcm: data[0]}
            }));
            this._store.dispatch(new PushNotificationAction({
              severity: 'success', summary: SUCCESS_ADJUNTAR_DOCUMENTO
            }));
            this.uploadDisabled = true;
            this.principalFileId = data[0];
            this.changeDetection.detectChanges();
          }
        } else {
          switch (data.codMensaje) {
            case '1111':
              this._store.dispatch(new PushNotificationAction({
                severity: 'error', summary: 'DOCUMENTO DUPLICADO, NO PUEDE ADJUNTAR EL DOCUMENTO'
              }));
              // (<FileUpload>(this.uploader)).disabled = true;
              // this.uploadDisabled = true;
              break;
            case '3333':
              this._store.dispatch(new PushNotificationAction({
                severity: 'error', summary: 'ACCESO DENEGADO, NO PUEDE SUBIR EL DOCUMENTO'
              }));
              break;
            case '4444':
              this._store.dispatch(new PushNotificationAction({
                severity: 'error',
                summary: 'LA SEDE ' + _dependencia.nomSede + ' NO SE ENCUENTRA EN EL REPOSITORIO DOCUEMENTAL'
              }));
              break;
            case '4445':
              this._store.dispatch(new PushNotificationAction({
                severity: 'error',
                summary: 'LA DEPENDENCIA ' + _dependencia.nombre + ' NO SE ENCUENTRA EN EL REPOSITORIO DOCUEMENTAL'
              }));
              break;
            default:
              this._store.dispatch(new PushNotificationAction({
                severity: 'error', summary: data.mensaje
              }));
          }
        }
      });
    }

  }

  preview(file) {
    console.log(this.principalFile);
    const self = this;
    const reader = new FileReader();
    reader.addEventListener('load', () => {
      console.log(reader.result);
      self.url = reader.result;
      self.previewWasRefreshed = true;
      self.changeDetection.detectChanges();
    }, false);
    reader.readAsArrayBuffer(file);
  }

  onUpload(event) {
    // this.uploadFiles = event.files;
    this.status = UploadStatus.UPLOADED;
  }

  onClear(event) {

    this.principalFile = undefined;

    this.changeDetection.detectChanges();
    this.status = UploadStatus.CLEAN;
    this.uploadDisabled = false;
    console.log('DOCUMENTO PRINCIPAL ELIMINADO...');
/*    if (null !== this.principalFileId) {
      const deleteUrl = environment.digitalizar_doc_upload_endpoint + '/eliminarprincipal/' + this.principalFileId;
      this._api.post(deleteUrl, {}).subscribe(data => {
        if (data.ok) {
          this.principalFileId = null;
          this._store.dispatch(new PushNotificationAction({
            severity: 'success', summary: 'DOCUMENTOS ELIMINADOS CORRECTAMENTE'
          }));
        }
      });
    }*/
  }

  onSelect(event) {

    if(this.principalFile === undefined){

      this.principalFile = this.uploader.files[0].name;
    }

    this.previewWasRefreshed = false;
    for (const file of event.files) {
      this.uploadFiles.push(file);
    }
    this.changeDetection.detectChanges();
    this.status = UploadStatus.LOADED;
  }

  ngOnDestroy() {
    this.activeTaskUnsubscriber.unsubscribe();
  }

}
