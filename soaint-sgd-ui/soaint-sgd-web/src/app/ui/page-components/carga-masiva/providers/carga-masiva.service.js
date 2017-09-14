"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
var core_1 = require("@angular/core");
var http_1 = require("@angular/http");
var CargaMasivaService = (function () {
    function CargaMasivaService(router, http) {
        this.router = router;
        this.http = http;
        this.host = 'http://192.168.1.81:28080/Massive-Loader';
    }
    // Subir documento para carga masiva
    CargaMasivaService.prototype.uploadFile = function (files, postData) {
        var headers = new http_1.Headers();
        var formData = new FormData();
        formData.append('file', files[0], files[0].name);
        if (postData !== '' && postData !== undefined && postData !== null) {
            for (var property in postData) {
                if (postData.hasOwnProperty(property)) {
                    formData.append(property, postData[property]);
                }
            }
        }
        return this.http.post(this.host + '/upload', formData, { headers: headers })
            .toPromise().then(function (res) { return res.json(); })
            .catch(this.handleError);
    };
    // Obtener todos los registros de cargas masivas realizadas
    CargaMasivaService.prototype.getRecords = function () {
        return this.http.get(this.host + '/listadocargamasiva').map(function (res) { return res.json(); })
            .catch(this.handleError);
    };
    // Obtener el ultimo registro de carga masiva
    CargaMasivaService.prototype.getLastRecord = function () {
        return this.http.get(this.host + '/estadocargamasiva').map(function (res) { return res.json(); })
            .catch(this.handleError);
    };
    // Obtener detalles de un registro de carga masiva específico
    CargaMasivaService.prototype.getRecord = function (id) {
        if (id == 'last' || isNaN(id)) {
            return this.getLastRecord();
        }
        return this.getRecordById(id);
    };
    CargaMasivaService.prototype.getRecordById = function (id) {
        return this.http.get(this.host + "/estadocargamasiva/" + id).map(function (res) { return res.json(); })
            .catch(this.handleError);
    };
    CargaMasivaService.prototype.handleError = function (error) {
        console.error('An error occurred', error); // for demo purposes only
        return Promise.reject(error.message || error);
    };
    return CargaMasivaService;
}());
CargaMasivaService = __decorate([
    core_1.Injectable()
], CargaMasivaService);
exports.CargaMasivaService = CargaMasivaService;
