import {DocumentoEcmDTO} from '../../../../domain/documentoEcmDTO';

export class DocumentVersionUploaded {
    constructor(public ecmDocument: DocumentoEcmDTO) { }
}

export class DocumentVersionRemoved {
    constructor(public ecmDocument: DocumentoEcmDTO) { }
}
