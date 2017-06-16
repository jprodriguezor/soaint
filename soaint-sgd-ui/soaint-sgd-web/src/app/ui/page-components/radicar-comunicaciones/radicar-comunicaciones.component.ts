import {Component, OnInit, ViewChild} from '@angular/core';

@Component({
  selector: 'app-radicar-comunicaciones',
  templateUrl: './radicar-comunicaciones.component.html'
})
export class RadicarComunicacionesComponent implements OnInit {

  @ViewChild('datosGenerales') datosGenerales;

  constructor() {
  }

  ngOnInit() {
  }

  printForm() {
    console.log(this.datosGenerales.form);
  }

}

