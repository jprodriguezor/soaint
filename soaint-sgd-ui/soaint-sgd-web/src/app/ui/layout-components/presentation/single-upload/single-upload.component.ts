import {ChangeDetectorRef, Component, EventEmitter, OnInit, Output} from '@angular/core';
import {environment} from '../../../../../environments/environment';
import {ApiBase} from '../../../../infrastructure/api/api-base';

@Component({
  selector: 'app-single-upload',
  templateUrl: './single-upload.component.html'
})
export class SingleUploadComponent implements OnInit {

  @Output() fileUploaded: EventEmitter<any> = new EventEmitter();

  constructor(private _http: ApiBase, private _changeDetector: ChangeDetectorRef) { }

  ngOnInit() {
  }

  customUploader(event) {
    const formData = new FormData();
    formData.append('file[]', event.files[0], event.files[0].name);
    formData.append('tipoComunicacion', '1');
    formData.append('fileName', '1');
    this._http.sendFile(environment.digitalizar_doc_upload_endpoint, formData, []).subscribe(response => {
        this.fileUploaded.emit({id: response.ecmIds[0], file: event.files[0]});
    });
  }

  onUpload(event) {
    this.fileUploaded.emit(event.files[0]);
  }

  onErrorUpload(event) {
    console.log(event);
  }

  onClear(event) {
    // this._changeDetector.detectChanges();
    // this.status = UploadStatus.CLEAN;
  }

  onSelect(event) {
    this.customUploader(event);
  }

}
