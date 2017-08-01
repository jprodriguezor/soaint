import {Pipe, PipeTransform} from '@angular/core';


@Pipe({name: 'dropdownSingleItem'})
export class DropdownSingleItemPipe implements PipeTransform {
  transform(value, args?) {
    // ES6 array destructuring
    if (value) {
      return {label: value.nombre, value: value};
    }

  }
}
