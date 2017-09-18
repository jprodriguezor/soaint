"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
var core_1 = require("@angular/core");
var carga_masiva_service_1 = require("../providers/carga-masiva.service");
var UploadStatus;
(function (UploadStatus) {
    UploadStatus[UploadStatus["CLEAN"] = 0] = "CLEAN";
    UploadStatus[UploadStatus["LOADED"] = 1] = "LOADED";
    UploadStatus[UploadStatus["UPLOADING"] = 2] = "UPLOADING";
    UploadStatus[UploadStatus["UPLOADED"] = 3] = "UPLOADED";
})(UploadStatus || (UploadStatus = {}));
var CargaMasivaUploaderComponent = (function () {
    function CargaMasivaUploaderComponent(changeDetection, cmService) {
        this.changeDetection = changeDetection;
        this.cmService = cmService;
        this.previewWasRefreshed = false;
    }
    CargaMasivaUploaderComponent.prototype.showUploadButton = function () {
        return this.status === UploadStatus.CLEAN;
    };
    CargaMasivaUploaderComponent.prototype.uploadFileAction = function (event) {
        var _this = this;
        this.cmService.uploadFile(event.files, { codigoSede: 1040, codigoDependencia: 10401040 })
            .then(function (result) {
            _this.resultUpload = result;
            _this.changeDetection.detectChanges();
        });
    };
    CargaMasivaUploaderComponent.prototype.onUpload = function (event) {
        this.uploadFile = event.files[0];
        this.status = UploadStatus.UPLOADED;
    };
    CargaMasivaUploaderComponent.prototype.onClear = function (event) {
        this.changeDetection.detectChanges();
        this.status = UploadStatus.CLEAN;
    };
    CargaMasivaUploaderComponent.prototype.onSelect = function (event) {
        this.previewWasRefreshed = false;
        this.uploadFile = event.files[0];
        if (this.uploader.files.length === 2) {
            this.uploader.remove(0);
        }
        else if (this.uploader.files.length > 2) {
            var index = 0;
            while (this.uploader.files.length !== 1) {
                if (this.uploader.files[index] !== this.uploadFile) {
                    this.uploader.remove(index);
                    index--;
                }
                else if (this.uploader.files[index].lastModified !== this.uploadFile.lastModified) {
                    this.uploader.remove(index);
                    index--;
                }
                else {
                    index++;
                    if (index === this.uploader.files.length) {
                        break;
                    }
                }
            }
        }
        this.changeDetection.detectChanges();
        this.status = UploadStatus.LOADED;
    };
    return CargaMasivaUploaderComponent;
}());
__decorate([
    core_1.ViewChild('uploader')
], CargaMasivaUploaderComponent.prototype, "uploader", void 0);
CargaMasivaUploaderComponent = __decorate([
    core_1.Component({
        selector: 'cm-uploader',
        templateUrl: 'cm-uploader.component.html',
        styleUrls: ['../carga-masiva.component.css'],
        providers: [carga_masiva_service_1.CargaMasivaService]
    })
], CargaMasivaUploaderComponent);
exports.CargaMasivaUploaderComponent = CargaMasivaUploaderComponent;
