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
import {PushNotificationAction} from '../../../infrastructure/state-management/notifications-state/notifications-actions';
import {FAIL_ADJUNTAR_PRINCIPAL, SUCCESS_ADJUNTAR_DOCUMENTO} from '../../../shared/lang/es';
import {isNullOrUndefined} from 'util';
import {Sandbox as DependenciaSandbox} from '../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';

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
  principalFile: string;
  correspondencia: CorrespondenciaDTO;

  activeTaskUnsubscriber: Subscription;

  @ViewChild('uploader') uploader;
  @ViewChild('viewer') viewer;


  constructor(private changeDetection: ChangeDetectorRef,
              private _api: ApiBase,
              private _asignacionSandBox: AsignacionSandbox, private _dependenciaSandbox: DependenciaSandbox,
              private _store: Store<RootState>) {
  }

  ngOnInit() {
    this.uploadUrl = environment.pd_adjuntar_documento_endpoint;
    this.activeTaskUnsubscriber = this._store.select(getActiveTask).subscribe(activeTask => {
      this.task = activeTask;
      this.codSede = activeTask.variables.codigoSede;
      this.codDepedencia = activeTask.variables.codDependencia;
      this._dependenciaSandbox.loadDependencies({}).subscribe((results) => {
        this.depedencia  = results.dependencias.find((element) => element.codigo === this.codDepedencia).nombre;
        this.sede  = results.dependencias.find((element) => element.codSede === this.codSede).nomSede;
        console.log(this.depedencia);
        console.log(this.sede);
      });
    });
  }

  showUploadButton() {
    return this.status === UploadStatus.CLEAN;
  }

  customUploader(event) {
    if (isNullOrUndefined(this.principalFile)) {
      this._store.dispatch(new PushNotificationAction({
        severity: 'warn',
        summary: FAIL_ADJUNTAR_PRINCIPAL
      }));
    } else {
      const formData = new FormData();
      for (const file of event.files) {
        formData.append('files', file, file.name);
      }
      this._api.sendFile(this.uploadUrl, formData, [this.sede, this.depedencia, this.principalFile]).subscribe(response => {
        console.log(response);
        this._store.dispatch(new CompleteTaskAction({
          idProceso: this.task.idProceso,
          idDespliegue: this.task.idDespliegue,
          idTarea: this.task.idTarea,
          parametros: {
            ideEcm: response[0]
          }
        }));
        this._store.dispatch(new PushNotificationAction({
          severity: 'success',
          summary: SUCCESS_ADJUNTAR_DOCUMENTO
        }));
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
    // this.uploadFiles = event.files;
    this.status = UploadStatus.UPLOADED;
  }

  onClear(event) {
    this.changeDetection.detectChanges();
    this.status = UploadStatus.CLEAN;
  }

  onSelect(event) {

    console.log(event.files);

    this.previewWasRefreshed = false;

    for (const file of event.files) {
      this.uploadFiles.push(file);
    }
    console.log(this.uploadFiles);

    /*if (this.uploader.files.length === 2) {
      this.uploader.remove(0);
    } else if (this.uploader.files.length > 2) {
      let index = 0;
      while (this.uploader.files.length !== 1) {
        if (this.uploader.files[index] !== this.uploadFile) {
          this.uploader.remove(index);
          index--;
        } else if (this.uploader.files[index].lastModified !== this.uploadFile.lastModified) {
          this.uploader.remove(index);
          index--;
        } else {
          index++;
          if (index === this.uploader.files.length) {
            break;
          }
        }
      }
    }*/
    this.changeDetection.detectChanges();
    this.status = UploadStatus.LOADED;
  }

  ngOnDestroy() {
    this.activeTaskUnsubscriber.unsubscribe();
  }

}
