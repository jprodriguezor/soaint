import {
  ChangeDetectorRef, SimpleChange, Component, OnInit, ViewChild, ViewEncapsulation, ViewContainerRef,
  ComponentFactoryResolver
} from '@angular/core';
import {ApiBase} from '../../../infrastructure/api/api-base';
import {environment} from '../../../../environments/environment';


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
  url: string;
  status: UploadStatus;
  previewWasRefreshed = false;
  uploadUrl: string;

  @ViewChild('uploader') uploader;
  @ViewChild('viewer') viewer;


  constructor(private changeDetection: ChangeDetectorRef, private _api: ApiBase) {
  }

  ngOnInit() {
    this.uploadUrl =  environment.digitalizar_doc_upload_endpoint;
  }

  showUploadButton() {
    return this.status === UploadStatus.CLEAN;
  }

  customUploader(event) {
    const formData = new FormData();
    formData.append('file', event.files[0], event.files[0].name);
    this._api.sendFile( this.uploadUrl, formData).subscribe();
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