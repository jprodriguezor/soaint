import { SerieDTO } from 'app/domain/serieDTO';
import { SubserieDTO } from 'app/domain/subserieDTO';

export interface ContenidoDependenciaTrdDTO {
    idOrgAdm: string;
    idOrgOfc: string;
    codSerie: string;
    nomSerie: string;
    codSubSerie: string;
    nomSubSerie: string;
    retArchivoGestion: string;
    retArchivoCentral: string;
    procedimiento: string;
    diposicionFinal: string;
    listaSerie: SerieDTO[];
    listaSubSerie: SubserieDTO[];

}
