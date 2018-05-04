import { Pipe, PipeTransform } from '@angular/core';

@Pipe({name: 'toActiveString'})
export class ToActiveString implements PipeTransform {
  transform(value, args?) {
    return (value) ? 'Inactivo' : 'Activo';
  }
}
