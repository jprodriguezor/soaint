
export class  ObjectHelper {

  static  similar( obj1:Object,obj2:Object): boolean{

   return Object.keys(obj1).every( key => {

      if(obj2[key] === undefined ){

         return obj1[key] === undefined;
      }

      if(typeof obj1[key] === 'object' && typeof  obj2[key] == 'object')
      return  ObjectHelper.similar(obj1[key],obj2[key]);

      if( typeof obj1[key] !== 'function') {

      //  console.log(obj1[key].toString()+" length:"+obj1[key].toString().length+'====================='+obj2[key].toString()+" length:"+obj2[key].toString().length);

       let x= (obj1[key].toString().trim() === obj2[key].toString().trim());

      // console.log(x);

       return x;

      }

      return true;
    });

  }
}
