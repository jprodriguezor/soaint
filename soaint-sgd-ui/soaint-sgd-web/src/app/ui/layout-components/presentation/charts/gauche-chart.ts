import {Component} from '@angular/core'

@Component({
  selector: 'app-gauge-chart',
  template: `
    <ngx-charts-gauge
      [view]="view"
      [scheme]="colorScheme"
      [results]="data"
      [min]="0"
      [max]="100"
      [angleSpan]="240"
      [startAngle]="-120"
      [units]="'alerts'"
      [bigSegments]="10"
      [smallSegments]="5"
      (select)="onSelect($event)">
    </ngx-charts-gauge>
  `
})
export class GaugeChartComponent {
  view: any[] = [600, 300];
  data: any[];

  colorScheme = {
    domain: ['#31CCEC', '#F2C037', '#21BA45', '#bdbdbd']
  };

  constructor() {
    this.data = single;
  }

  onSelect(event) {
    console.log(event);
  }
}


const single = [
  {
    'name': 'Reservadas',
    'value': 3
  },
  {
    'name': 'En proceso',
    'value': 3
  },
  {
    'name': 'Completadas',
    'value': 1
  },
  {
    'name': 'Canceladas',
    'value': 0
  }
];

