import {Component, ViewChild} from '@angular/core';

@Component({
    selector: 'carga-masiva',
    templateUrl: './carga-masiva.component.html',
    styleUrls: ['./carga-masiva.component.css']
})

export class CargaMasivaComponent {

    @ViewChild('records') records;

    onDocUploaded(event) : void {
        this.records.getRegistros();
    }
}
