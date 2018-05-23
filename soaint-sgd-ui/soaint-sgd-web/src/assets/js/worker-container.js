
if(Worker !== undefined || localStorage !== undefined ){

  var worker = new Worker('assets/workers/worker.js');


  function getContants(){

    var obj = [];

    for(var i = 0; i < localStorage.length; i++){

      var key = localStorage.key(i);

      response =  JSON.parse(localStorage.getItem(key))[0].response;

       if(response.constantes )
       obj = obj.concat(response.constantes);
    }

     return obj;
  }

  worker.addEventListener("message",() => {

      localStorage.clear();
  },false);
  worker.postMessage(getContants());
}



