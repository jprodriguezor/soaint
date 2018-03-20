import {AbstractControl, FormControl, FormGroup} from "@angular/forms";

export  class ExtendValidators {

  static requiredIf(control: string, value: any,otherControl:string): (fr: AbstractControl) => void {

    return (fr: AbstractControl) => {


      if(fr.get(control) === null || fr.get(otherControl) === null)
        return null;

      let errors = fr.get(otherControl).errors;

      if(errors !== null){

        delete errors.requiredIf;

        if(Object.keys(errors).length == 0)
          errors = null;
      }


         if (fr.get(control).value == value && (fr.get(otherControl).value == null || fr.get(otherControl).value.length == 0)){

              if(errors === null)
                 errors = {};

              errors.requiredIf = true;
         }

      fr.get(otherControl).setErrors(errors);

      return null;

    }
  }
}
