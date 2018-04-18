import {Pipe, PipeTransform} from '@angular/core';


@Pipe({name: 'dropdownItem'})
export class DropdownItemPipe implements PipeTransform {
  transform(value, args?) {
    // ES6 array destructuring
    if (value) {
      return value.map(item => {
        return {label: item.nombre, value: item};
      });
    }
  }
}

@Pipe({name: 'dropdownItemSingle'})
export class DropdownItemPipeSingle implements PipeTransform {
  transform(value, args?) {
    // ES6 array destructuring
    if (value) {
      return value.map(item => {
        return {label: item.nombre, value: item.codigo};
      });
    }
    return [];
  }
}

@Pipe({name: 'dropdownItemFullName'})
export class DropdownItemPipeFullName implements PipeTransform {
  transform(value, args?) {
    // ES6 array destructuring
    if (value) {
      return value.map(item => {
        return {
          label: item.nombre + ' ' + (item.valApellido1 ? item.valApellido1 : '') + ' ' + (item.valApellido2 ? item.valApellido2 : ''),
          value: item
        };
      });
    }
  }
}

@Pipe({name: 'dropdownItemSerie'})
export class DropdownItemPipeSerie implements PipeTransform {
  transform(value, args?) {
    // ES6 array destructuring
    console.log(value);
    if (value) {
      return value.map(item => {
        return {
          label: item.nombreSerie,
          value: item.codigoSerie
        };
      });
    }
  }
}

@Pipe({name: 'dropdownItemSubserie'})
export class DropdownItemPipeSubserie implements PipeTransform {
  transform(value, args?) {
    // ES6 array destructuring
    if (value) {
      return value.map(item => {
        return {
          label: item.nombreSubSerie,
          value: item.codigoSubSerie
        };
      });
    }
  }
}
