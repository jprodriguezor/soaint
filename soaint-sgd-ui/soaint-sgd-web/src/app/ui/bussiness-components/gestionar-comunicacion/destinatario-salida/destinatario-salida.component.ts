import {Store} from '@ngrx/store';
import {State as RootState} from '../../../../infrastructure/redux-store/redux-reducers';
import {Component, OnInit} from '@angular/core';



@Component({
  selector: 'app-destinatario-salida',
  templateUrl: './destinatario-salida.component.html',
  styleUrls: ['./destinatario-salida.component.css']
})
export class DestinatarioSalidaComponent implements OnInit {

  constructor(private _store: Store<RootState>) {
  }

  ngOnInit() {


  }

}
