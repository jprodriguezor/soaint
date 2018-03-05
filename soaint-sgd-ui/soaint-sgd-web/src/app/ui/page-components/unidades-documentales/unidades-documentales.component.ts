import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-unidades-documentales',
  templateUrl: './unidades-documentales.component.html',
  styleUrls: ['./unidades-documentales.component.css']
})
export class UnidadesDocumentalesComponent implements OnInit {

  solicitudActual:Object = null;

  constructor() { }

  ngOnInit() {
    console.log('Hello Documents...!');
  }

}
