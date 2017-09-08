import { Component,ChangeDetectorRef, ViewChild } from '@angular/core';

import {CargaMasivaService} from '../providers/carga-masiva.service';
import {ResultUploadDTO} from "../domain/ResultUploadDTO";

enum UploadStatus {
  CLEAN = 0,
  LOADED = 1,
  UPLOADING = 2,
  UPLOADED = 3,
}

@Component({
  selector: 'cm-uploader',
  templateUrl: './cm-uploader.component.html',
  styleUrls: ['../carga-masiva.component.css'],
  providers: [CargaMasivaService]
})

export class CargaMasivaUploaderComponent {

  uploadFile: any;
  url: string;
  status: UploadStatus;
  previewWasRefreshed = false;
  resultUpload: ResultUploadDTO;

  @ViewChild('uploader') uploader;

  constructor(private changeDetection: ChangeDetectorRef, private cmService: CargaMasivaService) {}

  showUploadButton() {
    return this.status === UploadStatus.CLEAN;
  }

  uploadFileAction (event) : void {
      this.cmService.uploadFile(event.files, {codigoSede:1040, codigoDependencia:10401040})
        .then(result => {
            this.resultUpload = result;
            this.changeDetection.detectChanges();
        });
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
