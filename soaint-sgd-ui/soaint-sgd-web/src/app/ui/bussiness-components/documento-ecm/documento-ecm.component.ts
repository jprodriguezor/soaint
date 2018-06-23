import {ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {ApiBase} from '../../../infrastructure/api/api-base';
import {environment} from '../../../../environments/environment';
import {State as RootState} from '../../../infrastructure/redux-store/redux-reducers';
import {Store} from '@ngrx/store';
import {getActiveTask} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {Subscription} from 'rxjs/Subscription';
import {Sandbox as AsignacionSandbox} from '../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-sandbox';
import {PushNotificationAction} from '../../../infrastructure/state-management/notifications-state/notifications-actions';
import {FAIL_ADJUNTAR_PRINCIPAL, SUCCESS_ADJUNTAR_DOCUMENTO, FAIL_ADJUNTAR_ANEXOS} from '../../../shared/lang/es';
import {isNullOrUndefined} from 'util';
import {Sandbox as DependenciaSandbox} from '../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import { CorrespondenciaDTO } from '../../../domain/correspondenciaDTO';
import { ComunicacionOficialDTO } from '../../../domain/comunicacionOficialDTO';
import { isArray } from 'rxjs/util/isArray';
import * as codigos from '../../../shared/bussiness-properties/radicacion-properties';

enum UploadStatus {
  CLEAN = 0,
  LOADED = 1,
  UPLOADING = 2,
  UPLOADED = 3,
}

@Component({
  selector: 'documento-ecm',
  templateUrl: './documento-ecm.component.html',
  styleUrls: ['./documento-ecm.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class DocumentoEcmComponent implements OnInit, OnDestroy {

  uploadFiles: any[] = [];
  task: any;
  sede: string;
  codSede: string;
  depedencia: string;
  codDepedencia: string;
  url: string;
  status: UploadStatus;
  previewWasRefreshed = false;
  uploadUrl: string;
  uploadDisabled = false;
  principalFile: string;
  principalFileId = null;
  activeTaskUnsubscriber: Subscription;

  @ViewChild('uploader') uploader;
  @ViewChild('viewer') viewer;

  documentIdEcm: string;


  constructor(private changeDetection: ChangeDetectorRef,
              private _api: ApiBase,
              private _asignacionSandBox: AsignacionSandbox, private _dependenciaSandbox: DependenciaSandbox,
              private _store: Store<RootState>) {
  }

  ngOnInit() {
    this.uploadUrl = environment.pd_gestion_documental.subirAnexo;
    this.activeTaskUnsubscriber = this._store.select(getActiveTask).subscribe(activeTask => {
      this.task = activeTask;
      this.codSede = activeTask.variables.codigoSede;
      this.codDepedencia = activeTask.variables.codDependencia;
      this._dependenciaSandbox.loadDependencies({}).subscribe((results) => {
        const dependencia = results.dependencias.find((element) => element.codigo === this.codDepedencia);
        this.depedencia = dependencia.nombre;
        this.codDepedencia = dependencia.codigo;
        this.sede = dependencia.nomSede;
        this.codSede = dependencia.codSede;
      });
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

    if (isNullOrUndefined(this.principalFile)) {

      this._store.dispatch(new PushNotificationAction({
        severity: 'warn',
        summary: FAIL_ADJUNTAR_PRINCIPAL
      }));   

    } else {
      let _dependencia;
      formData.append('sede',this.sede);
      formData.append('codigoSede', this.codSede);
      formData.append('dependencia', this.depedencia);
      formData.append('codigoDependencia', this.codDepedencia);
      this._api.sendFile(this.uploadUrl, formData, []).subscribe(response => {
        const data = response;
        if (isArray(data)) {
          if (data.length === 0) {
            this._store.dispatch(new PushNotificationAction({
              severity: 'error', summary: 'NO ADJUNTO, NO PUEDE ADJUNTAR EL DOCUMENTO'
            }));
          } else {
            this._store.dispatch(new PushNotificationAction({
              severity: 'success', summary: SUCCESS_ADJUNTAR_DOCUMENTO
            }));
            this.uploadDisabled = true;
            this.principalFileId = data[0];
            this.changeDetection.detectChanges();
          }
        } else {
              this._store.dispatch(new PushNotificationAction({
                severity: 'error', summary: data.mensaje
              }));
          }
      });
    }

  }

  preview(file) {
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
    this.status = UploadStatus.UPLOADED;
  }

  onClear(event) {
    this.changeDetection.detectChanges();
    this.status = UploadStatus.CLEAN;
  }

  onSelect(event) {
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
