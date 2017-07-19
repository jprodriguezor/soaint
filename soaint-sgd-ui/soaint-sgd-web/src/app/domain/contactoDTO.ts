export interface ContactoDTO {
  ideContacto: number,
  nroViaGeneradora: string,
  nroPlaca: string,
  codTipoVia: string,
  codPrefijoCuadrant: string,
  codPostal: string,
  direccion: string,
  celular: string,
  telFijo: string,
  extension: string,
  corrElectronico: string,
  codPais: string,
  codDepartamento: string,
  codMunicipio: string,
  provEstado: string,
  principal?: string
}
