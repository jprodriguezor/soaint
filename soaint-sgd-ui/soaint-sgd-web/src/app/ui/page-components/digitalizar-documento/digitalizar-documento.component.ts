import {
  ChangeDetectorRef, SimpleChange, Component, OnInit, ViewChild, ViewEncapsulation, ViewContainerRef,
  ComponentFactoryResolver
} from '@angular/core';
import {ApiBase} from '../../../infrastructure/api/api-base';
import {environment} from '../../../../environments/environment';
import {State as RootState} from '../../../infrastructure/redux-store/redux-reducers';
import {Store} from '@ngrx/store';
import {correspondenciaEntrada} from '../../../infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-selectors';
import {CompleteTaskAction} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-actions';
import {getActiveTask} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {Subscription} from 'rxjs/Subscription';


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
export class DigitalizarDocumentoComponent implements OnInit {

  uploadFile: any;
  task: any;
  url: string;
  status: UploadStatus;
  previewWasRefreshed = false;
  uploadUrl: string;

  activeTaskUnsubscriber: Subscription;

  @ViewChild('uploader') uploader;
  @ViewChild('viewer') viewer;


  constructor(private changeDetection: ChangeDetectorRef, private _api: ApiBase, private _store: Store<RootState>) {
  }

  ngOnInit() {
    this.uploadUrl =  environment.digitalizar_doc_upload_endpoint;
    this.activeTaskUnsubscriber = this._store.select(getActiveTask).subscribe(activeTask => {
      this.task = activeTask;
    });
  }

  showUploadButton() {
    return this.status === UploadStatus.CLEAN;
  }

  customUploader(event) {
    const formData = new FormData();
    formData.append('file[]', event.files[0], event.files[0].name);
    this._store.select(correspondenciaEntrada).take(1).subscribe((value) => {
      this._api.sendFile( this.uploadUrl, formData, [value.tipoComunicacion.codigo , value.numeroRadicado ]).subscribe(response => {
          this._store.dispatch(new CompleteTaskAction({
            idProceso: this.task.idProceso,
            idDespliegue: this.task.idDespliegue,
            idTarea: this.task.idTarea,
            parametros: {
              ideEcm: response.ecmIds[0]
            }
          }));
      });
    });

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
    this.uploadFile = event.files[0];
    this.status = UploadStatus.UPLOADED;
  }

  onClear(event) {
    this.changeDetection.detectChanges();
    this.status = UploadStatus.CLEAN;
  }

  onSelect(event) {

    this.previewWasRefreshed = false;
    this.uploadFile = event.files[0];

    if (this.uploader.files.length === 2) {
      this.uploader.remove(0);
    } else if (this.uploader.files.length > 2) {
      let index = 0;
      while (this.uploader.files.length !== 1) {
        if (this.uploader.files[index] !== this.uploadFile) {
          this.uploader.remove(index);
          index --;
        } else if ( this.uploader.files[index].lastModified !== this.uploadFile.lastModified) {
          this.uploader.remove(index);
          index --;
        } else {
          index++;
          if (index === this.uploader.files.length) { break; }
        }
      }
    }
    this.changeDetection.detectChanges();
    this.status = UploadStatus.LOADED;
  }

}
