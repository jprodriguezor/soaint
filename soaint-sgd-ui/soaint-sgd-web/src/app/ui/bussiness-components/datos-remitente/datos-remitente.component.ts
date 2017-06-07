import { Component, OnInit } from '@angular/core';
import {SelectItem} from 'primeng/primeng';

@Component({
  selector: 'app-datos-remitente',
  templateUrl: './datos-remitente.component.html',
  styleUrls: ['./datos-remitente.component.css']
})
export class DatosRemitenteComponent implements OnInit {

  selected: any;

  personsType: SelectItem[];

  constructor() {
  }

  ngOnInit(): void {
    this.personsType = [];
    this.personsType.push({
      label: 'PERSONA JURIDICA',
      value: {id: 1, name: 'PERSONA JURIDICA'}
    });
    this.personsType.push({
      label: 'PERSONA NATURAL',
      value: {id: 2, name: 'PERSONA NATURAL'}
    });
  }


}
