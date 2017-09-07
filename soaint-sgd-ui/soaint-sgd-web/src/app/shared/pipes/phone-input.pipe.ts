import {Pipe, PipeTransform} from '@angular/core';

@Pipe({name: 'countryPhone'})
export class CountryPhonePipe implements PipeTransform {

  transform(value: string): string {
    if (!value) {
      return;
    }

    let cleaned = value.replace(/\D/g, '');
    if (cleaned.length > 12) {
      cleaned = cleaned.substr(0, 12);
    }
    let formated = '';
    const localPhone = cleaned.substr(cleaned.length - 7);

    formated = localPhone;
    // don't show braces for empty value
    if (cleaned.length > 7) {
      const country = cleaned.substr(0, 2);

      if (cleaned.length > 9) {
        const areaCode = cleaned.substr(2, cleaned.length - 9);
        formated = '+' + country + ' (' + areaCode + ') ' + localPhone;
      } else {
        formated = '+' + country + ' ' + localPhone;
      }
    }

    return formated;

  }

}
