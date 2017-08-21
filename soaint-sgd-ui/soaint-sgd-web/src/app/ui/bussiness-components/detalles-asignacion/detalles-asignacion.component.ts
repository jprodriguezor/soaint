import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-detalles-asignacion',
  templateUrl: './detalles-asignacion.component.html',
  styleUrls: ['./detalles-asignacion.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class DetallesAsignacionComponent implements OnInit {

  tabIndex: number = 0;

  constructor(private _changeDetectorRef: ChangeDetectorRef) {
  }

  ngOnInit() {

  }

  refreshView() {
    this._changeDetectorRef.detectChanges();
  }

}
