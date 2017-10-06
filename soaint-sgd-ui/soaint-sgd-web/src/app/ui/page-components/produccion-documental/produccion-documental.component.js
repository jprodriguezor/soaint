"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
var core_1 = require("@angular/core");
var ProduccionDocumentalComponent = (function () {
    function ProduccionDocumentalComponent(activatedRoute) {
        this.activatedRoute = activatedRoute;
        this.step = 1;
    }
    ProduccionDocumentalComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.activatedRoute.queryParams.subscribe(function (params) {
            _this.step = params.hasOwnProperty('step') ? params['step'] : 1;
            console.log(_this.step);
        });
    };
    return ProduccionDocumentalComponent;
}());
ProduccionDocumentalComponent = __decorate([
    core_1.Component({
        selector: 'produccion-documental',
        templateUrl: './produccion-documental.component.html',
        styleUrls: ['produccion-documental.component.css'],
    })
], ProduccionDocumentalComponent);
exports.ProduccionDocumentalComponent = ProduccionDocumentalComponent;
