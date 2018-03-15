import {Component, EventEmitter, Input, OnChanges, OnInit, Output, ViewChild, ViewEncapsulation, AfterViewInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import 'rxjs/add/operator/single';
import {ComunicacionOficialDTO} from '../../../domain/comunicacionOficialDTO';
import {ApiBase} from '../../../infrastructure/api/api-base';
import {environment} from '../../../../environments/environment';
import {LoadingService} from '../../../infrastructure/utils/loading.service';
import {FileUpload} from 'primeng/primeng';


@Component({
  selector: 'app-documentos-ecm-list',
  templateUrl: './documentos-ecm-list.component.html'
})
export class DocumentosECMListComponent implements OnInit, OnChanges, AfterViewInit {


  @Input()
  versionar = false;

  @Input()
  idDocumentECM: string;

  docSrc = '';
  isLoading = false;

  documentsList: any;
  uploadUrl: String;
  editable = true;

  selectedFile = '';

  constructor(
    private _store: Store<State>,
    private _api: ApiBase,
    public loadingService: LoadingService
  ) {
  }

  ngOnInit() {
    this.loadDocumentos();
  }

  ngAfterViewInit() {
     this.loadDocumentos();
  }

  ngOnChanges(): void {
    this.loadDocumentos();
  }

  loadDocumentos() {
    if (this.idDocumentECM !== undefined) {
      // const idDocumentECM = this.comunicacion.ppdDocumentoList[0].ideEcm;
      // console.log('ID del ecm');
      // console.log(this.comunicacion.ppdDocumentoList);
      const endpoint = `${environment.obtenerDocumento_asociados_endpoint}` + '/' + this.idDocumentECM;
      this._api.list(endpoint).subscribe(response => {
        this.documentsList = [];
        if (response.codMensaje === '0000') {
          this.documentsList = response.documentoDTOList;
        }
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

  uploadHandler(event: any) {
    // const formData = new FormData();
    // for (const file of event.files) {
    //   formData.append('files', file, file.name);
    // }
    // let _dependencia;
    // this._asignacionSandBox.obtnerDependenciasPorCodigos(this.correspondencia.codDependencia).switchMap((result) => {
    //       _dependencia = result[0];

    //       const listRef = ["-1"];
    //       this.comunicacion.referidoList.forEach((data) => {
    //         listRef.push(data.nroRadRef);
    //       });

    //       return this._api.sendFile(
    //         this.uploadUrl, formData, [this.correspondencia.codTipoCmc, this.correspondencia.nroRadicado,listRef,
    //           this.principalFile, result.dependencias[0].nomSede, result.dependencias[0].nombre]);
    //     }
    //   ).subscribe(response => {
    //     const data = response;
    //     console.log(response);
    //     if (isArray(data)) {
    //       if (data.length === 0) {
    //         this._store.dispatch(new PushNotificationAction({
    //           severity: 'error', summary: 'NO ADJUNTO, NO PUEDE ADJUNTAR EL DOCUMENTO'
    //         }));
    //       } else {
    //         this._store.dispatch(new CompleteTaskAction({
    //           idProceso: this.task.idProceso, idDespliegue: this.task.idDespliegue,
    //           idTarea: this.task.idTarea, parametros: {ideEcm: data[0]}
    //         }));
    //         this._store.dispatch(new PushNotificationAction({
    //           severity: 'success', summary: SUCCESS_ADJUNTAR_DOCUMENTO
    //         }));
    //         this.uploadDisabled = true;
    //         this.principalFileId = data[0];
    //       }
    //     } else {
    //       switch (data.codMensaje) {
    //         case '1111':
    //           this._store.dispatch(new PushNotificationAction({
    //             severity: 'error', summary: 'DOCUMENTO DUPLICADO, NO PUEDE ADJUNTAR EL DOCUMENTO'
    //           }));
    //           this.uploadDisabled = true;
    //           break;
    //         case '3333':
    //           this._store.dispatch(new PushNotificationAction({
    //             severity: 'error', summary: 'ACCESO DENEGADO, NO PUEDE SUBIR EL DOCUMENTO'
    //           }));
    //           break;
    //         case '4444':
    //           this._store.dispatch(new PushNotificationAction({
    //             severity: 'error',
    //             summary: 'LA SEDE ' + _dependencia.nomSede + ' NO SE ENCUENTRA EN EL REPOSITORIO DOCUEMENTAL'
    //           }));
    //           break;
    //         case '4445':
    //           this._store.dispatch(new PushNotificationAction({
    //             severity: 'error',
    //             summary: 'LA DEPENDENCIA ' + _dependencia.nombre + ' NO SE ENCUENTRA EN EL REPOSITORIO DOCUEMENTAL'
    //           }));
    //           break;
    //         default:
    //           this._store.dispatch(new PushNotificationAction({
    //             severity: 'error', summary: 'UPSSS!!! HA OCURRIDO UN ERROR'
    //           }));
    //       }
    //     }
    //   });
    // }
  }

}
