
export class  ObjectHelper {

  static  similar( obj1:Object,obj2:Object): boolean{

    if(obj1 === undefined)
       return obj2 === undefined;

    let a = Object.keys(obj1).length >= Object.keys(obj2).length ? obj1 : obj2;
    let b = Object.keys(obj1).length < Object.keys(obj2).length ? obj1 : obj2;

   return Object.keys(a).every( key => {

      if(a[key] === undefined ){

         return b[key] === undefined;
      }

     if(typeof a[key] === 'object'){

       if(typeof b[key] !== 'object')
         return false;

       return  ObjectHelper.similar(a[key],b[key]);

     }

      if( typeof a[key] !== 'function') {

        return a[key].toString().trim() === b[key].toString().trim();
      }

       return typeof  b[key] !== 'function';
    });

  }
}
