import {Component, NgModule} from '@angular/core';

@Component({
  selector: 'app-pie-chart',
  styleUrls: ['./pie-chart.scss'],
  template: `
    <ngx-charts-advanced-pie-chart
      class="pie-chart"
      [view]="view"
      [scheme]="colorScheme"
      [results]="single"
      [gradient]="gradient"
      (select)="onSelect($event)">
    </ngx-charts-advanced-pie-chart>
  `
})
export class PieChartComponent {
  single: any[];
  multi: any[];

  gradient: any;

  view: any[] = [700, 400];

  colorScheme = {
    domain: ['#5AA454', '#A10A28', '#C7B42C', '#AAAAAA']
  };

  constructor() {
    Object.assign(this, {single, multi})
  }

  onSelect(event) {
    console.log(event);
  }

}


const single = [
  {
    'name': 'Germany',
    'value': 8940000
  },
  {
    'name': 'USA',
    'value': 5000000
  },
  {
    'name': 'France',
    'value': 7200000
  }
];

const multi = [
  {
    'name': 'Germany',
    'series': [
      {
        'name': '2010',
        'value': 7300000
      },
      {
        'name': '2011',
        'value': 8940000
      }
    ]
  },

  {
    'name': 'USA',
    'series': [
      {
        'name': '2010',
        'value': 7870000
      },
      {
        'name': '2011',
        'value': 8270000
      }
    ]
  },

  {
    'name': 'France',
    'series': [
      {
        'name': '2010',
        'value': 5000002
      },
      {
        'name': '2011',
        'value': 5800000
      }
    ]
  }
];

