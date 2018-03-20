export  type HookHandler = (...parameters)=>any;
type HookItem = {hook:string,callbacks:Array<{priority:number,fn:HookHandler}>};

export  class ViewFilterHook {

 private static hooks:HookItem[] = [];

  static applyFilter(hook:string,defaultValue,... parameters):any{

   let hookFilters = ViewFilterHook.getFilter(hook);

     if(!hookFilters)
       return defaultValue;

     return   hookFilters.callbacks.reduce((acumulator,value:{priority:number,fn:HookHandler}) => {

        parameters.unshift(acumulator);

        //  args.unshift(acumulator);

         return   value.fn.apply(null,parameters);

       },defaultValue);

  }

  static  addFilter(hook:string,cb:HookHandler,priority = 10 ){

    let hk = ViewFilterHook.getFilter(hook);

    if(hk === undefined){

      ViewFilterHook.hooks.push({hook:hook,callbacks:[{priority:priority,fn:cb}]});

      return;
    }

    hk.callbacks.push({priority:priority,fn:cb});

    hk.callbacks.sort((a,b) => a.priority-b.priority);

  };

  static removeFilter(hook:string,index?:number){

    if(index === undefined){

      let i:number = ViewFilterHook.hooks.findIndex( h => h.hook == hook);

      if(i!== -1)
       ViewFilterHook.hooks.splice(i,1);

    }

   let hk = ViewFilterHook.getFilter(hook);

    if(hk === undefined)
      return;

    hk.callbacks.splice(index,1);
  }

  private static existFilter(hook:string){

    return ViewFilterHook.hooks.some( h => h.hook === hook);
  }

  private static getFilter(hook:string){

    return ViewFilterHook.hooks.find( h => h.hook === hook);
  }
}
