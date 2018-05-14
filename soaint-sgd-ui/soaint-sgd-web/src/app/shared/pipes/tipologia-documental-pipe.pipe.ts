import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'tipologiaDocumentalPipe'
})
export class TipologiaDocumentalPipePipe implements PipeTransform {

  transform(value: any, args?: any): any {
    if (value) {
      return value.map(item => {
        return {label: item.nombre, value: item.id};
      });
    }
  }

}
