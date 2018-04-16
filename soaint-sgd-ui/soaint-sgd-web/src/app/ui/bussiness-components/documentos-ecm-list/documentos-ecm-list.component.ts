import {Component, EventEmitter, Input, OnChanges, OnInit, Output, ViewChild, ViewEncapsulation, AfterViewInit, ChangeDetectorRef} from '@angular/core';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import 'rxjs/add/operator/single';
import {ComunicacionOficialDTO} from '../../../domain/comunicacionOficialDTO';
import {ApiBase} from '../../../infrastructure/api/api-base';
import {environment} from '../../../../environments/environment';
import {LoadingService} from '../../../infrastructure/utils/loading.service';
import {FileUpload} from 'primeng/primeng';
import { VersionDocumentoDTO } from '../../page-components/produccion-documental/models/DocumentoDTO';
import { DocumentoEcmDTO } from '../../../domain/documentoEcmDTO';
import {Sandbox as TaskSandBox} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import { MessagingService, ProduccionDocumentalApiService } from '../../../infrastructure/__api.include';
import { TareaDTO } from '../../../domain/tareaDTO';
import {Subscription} from 'rxjs/Subscription';
import {getActiveTask} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import { PushNotificationAction } from '../../../infrastructure/state-management/notifications-state/notifications-actions';
import {Observable} from 'rxjs/Observable';


@Component({
  selector: 'app-documentos-ecm-list',
  templateUrl: './documentos-ecm-list.component.html',
  styleUrls:['./documentos-ecm-list.css']
})
export class DocumentosECMListComponent implements OnInit, OnChanges, AfterViewInit {


  @Input()
  versionar = false;

  @Input()
  idDocumentECM: string;

  docSrc = '';
  isLoading = false;

  documentsList: Array<any>;
  uploadUrl: String;
  editable = true;
  loading = false;

  selectedFile = '';

  task: TareaDTO;
  activeTaskUnsubscriber: Subscription;

  constructor(
    private _store: Store<State>,
    private _api: ApiBase,
    public loadingService: LoadingService,
    private _changeDetectorRef: ChangeDetectorRef,
    private _taskSandBox: TaskSandBox,
    private _produccionDocumentalApi: ProduccionDocumentalApiService,
    private messagingService: MessagingService
  ) {
  }

  ngOnInit() {
    this.activeTaskUnsubscriber = this._store.select(getActiveTask).subscribe(activeTask => {
      this.task = activeTask;
    });
    this.loadDocumentos();
  }

  ngAfterViewInit() {
     this.loadDocumentos();
  }

  ngOnChanges(): void {
    this.loadDocumentos();
  }

  loadDocumentos() {
    this.loading = true;
    if (this.idDocumentECM !== undefined) {
      const endpoint = `${environment.obtenerDocumento_asociados_endpoint}` + '/' + this.idDocumentECM;
      this._api.list(endpoint).subscribe(response => {
        this.documentsList = [];
        if (response.codMensaje === '0000') {
          this.documentsList = response.documentoDTOList;
        }
        this.loading = false;
      });
    }
  }

  setDataDocument(data: any) {
    this.idDocumentECM = data.comunicacion;
    this.versionar = data.versionar;
  }

  showDocument(idDocumento: string) {
    this.loadingService.presentLoading();
    this.docSrc = environment.obtenerDocumento + idDocumento;
  }

  hideDocument() {
    this.docSrc = '';
  }


  docLoaded() {
    this.loadingService.dismissLoading();
  }

  selectFile(uploader: FileUpload) {
    uploader.showUploadButton = true;
    uploader.styleClass = 'doc-selected';
  }

  clearFileList(uploader: FileUpload) {
    uploader.showUploadButton = false;
    uploader.styleClass = '';
  }

  onUpload() {

  }

  uploadHandler(event: any, uploader: FileUpload ) {
    if (event.files.length === 0) { return false; }
    const nv: VersionDocumentoDTO = {
      id: this.idDocumentECM,
      nombre: event.files[0].name,
      tipo: 'pdf',
      version: '',
      contenido: '',
      file: event.files[0],
      size: event.files[0].size
    };
    this.uploadVersionDocumento(nv)
    .subscribe(doc => {
      const indexDocumentoAnterior = this.documentsList.findIndex(item => item.idDocumento === this.idDocumentECM);
      this.documentsList[indexDocumentoAnterior] = doc;
      this.documentsList = [...this.documentsList];
      uploader.clear();
      // uploader.basicFileInput.nativeElement.value = '';
      uploader.showUploadButton = true;
      uploader.styleClass = 'doc-selected';
      uploader.disabled = false;
      this._changeDetectorRef.detectChanges();
    });
  }

  uploadVersionDocumento(doc: VersionDocumentoDTO): Observable<VersionDocumentoDTO> {
    const formData = new FormData();
    formData.append('documento', doc.file, doc.nombre);
    if (doc.id) {
        formData.append('idDocumento', doc.id);
    }
    formData.append('selector', 'PD');
    formData.append('nombreDocumento', doc.nombre);
    formData.append('tipoDocumento', doc.tipo);
    formData.append('sede', this.task.variables.nombreSede);
    formData.append('dependencia', this.task.variables.nombreDependencia);
    formData.append('nroRadicado', this.task.variables && this.task.variables.numeroRadicado || null);
    const documentoVersionado = this._produccionDocumentalApi.subirVersionDocumento(formData).map(
    resp => {
      if ('0000' === resp.codMensaje) {
        this._store.dispatch(new PushNotificationAction({severity: 'success', summary: resp.mensaje}))
        return resp.documentoDTOList[0];
      } else {
        this._store.dispatch(new PushNotificationAction({severity: 'error', summary: resp.mensaje}));
        return doc;
      }
    },
    error => {
        this._store.dispatch(new PushNotificationAction({severity: 'error', summary: error}))
        return doc;
    }
  );
  return documentoVersionado;
}
}
