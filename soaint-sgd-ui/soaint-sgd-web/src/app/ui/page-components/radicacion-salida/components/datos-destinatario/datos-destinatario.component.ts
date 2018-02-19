import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {TareaDTO} from '../../../../../domain/tareaDTO';

@Component({
  selector: 'rs-datos-destinatario',
  templateUrl: './datos-destinatario.component.html',
  styleUrls: ['./datos-destinatario.component.css']
})
export class DatosDestinatarioComponent implements OnInit {

  @Input() taskData: TareaDTO;
  principal = false;

  @ViewChild('destinatarioExterno') destinatarioExterno;
  @ViewChild('destinatarioInterno') destinatarioInterno;

  constructor() {
    console.log('CONSTRUCTOR...');
  }

  ngOnInit() {
    console.log('ON INIT...');
  }

  changePrincipalParent(event) {
    this.principal = event;
  }

}
