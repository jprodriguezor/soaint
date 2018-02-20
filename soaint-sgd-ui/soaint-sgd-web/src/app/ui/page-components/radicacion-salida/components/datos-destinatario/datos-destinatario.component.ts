import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {TareaDTO} from '../../../../../domain/tareaDTO';

@Component({
  selector: 'rs-datos-destinatario',
  templateUrl: './datos-destinatario.component.html',
  styleUrls: ['./datos-destinatario.component.css']
})
export class DatosDestinatarioComponent implements OnInit {

  @Input() taskData: TareaDTO;
  principal: Boolean = false;

  @ViewChild('destinatarioInterno') destinatarioInterno;
  @ViewChild('destinatarioExterno') destinatarioExterno;

  constructor() {
    console.log('CONSTRUCTOR...');
  }

  ngOnInit() {
    console.log('ON INIT...');
  }

}
