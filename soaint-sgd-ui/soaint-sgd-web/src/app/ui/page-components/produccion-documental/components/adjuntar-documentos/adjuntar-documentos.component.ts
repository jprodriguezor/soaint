import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {DocumentoDTO} from '../../../../../domain/documentoDTO';
import {ApiBase} from '../../../../../infrastructure/api/api-base';

enum UploadStatus {
  CLEAN = 0,
  LOADED = 1,
  UPLOADING = 2,
  UPLOADED = 3,
}

@Component({
  selector: 'pd-adjuntar-documentos',
  templateUrl: './adjuntar-documentos.component.html',
  styleUrls: ['./adjuntar-documentos.component.css']
})
export class AdjuntarDocumentosComponent implements OnInit {

  primaryFile: any;
  selectedFiles: String[];
  uploadFiles: Array<any>;
  task: any;
  url: string;
  status: UploadStatus;
  previewWasRefreshed = false;
  uploadUrl: string;
  dialogPrimaryDoc = false;

  docs: Observable<DocumentoDTO[]> = new Observable<DocumentoDTO[]>();
  docSelected: DocumentoDTO;

  constructor(private changeDetection: ChangeDetectorRef, private _api: ApiBase) {
  }

  ngOnInit() {
  }

  customUploader(event) {
    console.log(event);
    console.log(event.files);
    this.uploadFiles = event.files;
    if (this.uploadFiles.length !== 0 ) {
      const formData = new FormData();
      this.uploadFiles.forEach((file, index) => {
        formData.append('file[]', file, file.name);
      });
      this._api.sendFile( this.uploadUrl, formData, ['asd' , 'asd' ]).subscribe(response => {
        console.log(response);
      });
    }

  }

  onUpload(event) {
    console.log(event);
    console.log(event.files);
    this.uploadFiles = event.files[0];
    this.status = UploadStatus.UPLOADED;
  }

  onClear(event) {
    console.log(event);
    this.changeDetection.detectChanges();
    this.status = UploadStatus.CLEAN;
  }

  onSelect(event) {
    console.log(event);
  }

  hideDialog() {
    this.dialogPrimaryDoc = false;
  }

}
