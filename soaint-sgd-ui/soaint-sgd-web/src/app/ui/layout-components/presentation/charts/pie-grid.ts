import {Component} from '@angular/core';

@Component({
  selector: 'app-pie-grid-chart',
  template: `
    <ngx-charts-pie-grid
      [view]='view'
      [scheme]='colorScheme'
      [results]='single'
      (select)='onSelect($event)'>
    </ngx-charts-pie-grid>
  `
})
export class PieGridChartComponent {
  single: any[];
  multi: any[];

  view: any[] = [800, 400];

  // options
  showXAxis = true;
  showYAxis = true;
  gradient = false;
  showLegend = true;
  showXAxisLabel = true;
  xAxisLabel = 'Country';
  showYAxisLabel = true;
  yAxisLabel = 'Population';

  colorScheme = {
    domain: ['#31CCEC', '#F2C037', '#21BA45', '#bdbdbd']
  };

  // line, area
  autoScale = true;

  constructor() {
    Object.assign(this, {single})
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

