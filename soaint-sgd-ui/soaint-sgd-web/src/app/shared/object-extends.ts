
export class  ObjectHelper {

  static  similar( obj1:Object,obj2:Object): boolean{

    if(obj1 === undefined)
       return obj2 === undefined;

   return Object.keys(obj1).every( key => {

      if(obj2[key] === undefined ){

         return obj1[key] === undefined;
      }

      if(typeof obj1[key] === 'object' && typeof  obj2[key] == 'object')
      return  ObjectHelper.similar(obj1[key],obj2[key]);

      if( typeof obj1[key] !== 'function') {

       let x= (obj1[key].toString().trim() === obj2[key].toString().trim());

       return x;

      }

      return true;
    });

  }
}
