const  endpoint = "http://192.168.3.242:28080/correspondencia-business-services/services/constantes-web-api/constantes/A";

self.addEventListener("message",(event) => {

  var data = event.data;

 fetch(endpoint).then(response => {


    if(response.ok){

    return response.json();
    }
  })
    .then(res => {
    var constantes =  res.constantes;

       changeDetected = constantes.some( co =>  {

      if(co.codPadre == null)
        return false;

      var found = data.find( d => d.codigo == co.codigo);

      if(found === undefined)
        return false;

      return found.nombre != co.nombre || found.codPadre != co.codPadre;

    });

    if(changeDetected)
      self.postMessage(changeDetected);

    self.close();

  });


},false);


