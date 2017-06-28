import {Component, Input} from '@angular/core'

@Component({
  selector: 'app-gauge-chart',
  template: `
    <ngx-charts-gauge
      [view]="view"
      [scheme]="colorScheme"
      [results]="data"
      [min]="min"
      [max]="max"
      [angleSpan]="angleSpan"
      [startAngle]="startAngle"
      [units]="units"
      [bigSegments]="bigSegments"
      [smallSegments]="smallSegments"
    >
    </ngx-charts-gauge>
  `
})
export class GaugeChartComponent {
  view: any[] = [750, 350];
  @Input() data: any[];
  @Input() max: number = 10;
  @Input() min: number = 1;
  @Input() units: string = 'Tareas';
  @Input() bigSegments: number = 10;
  @Input() smallSegments: number = 5;
  @Input() angleSpan: number = 240;
  @Input() startAngle: number = -120;
  colorScheme = {
    domain: ['#31CCEC', '#F2C037', '#21BA45', '#bdbdbd']
  };

  constructor() {
  }
}


const single = [
  {
    'name': 'Reservadas',
    'value': 1
  },
  {
    'name': 'En proceso',
    'value': 1
  },
  {
    'name': 'Completadas',
    'value': 0
  },
  {
    'name': 'Candeladas',
    'value': 0
  }
];

