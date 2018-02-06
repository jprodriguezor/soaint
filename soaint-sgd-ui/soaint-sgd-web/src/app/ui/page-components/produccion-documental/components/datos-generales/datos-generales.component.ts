import {ChangeDetectorRef, Component, Input, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {ProduccionDocumentalApiService} from 'app/infrastructure/api/produccion-documental.api';
import {VALIDATION_MESSAGES} from 'app/shared/validation-messages';
import {getAuthenticatedFuncionario} from 'app/infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import {FuncionarioDTO} from 'app/domain/funcionarioDTO';
import {PdMessageService} from '../../providers/PdMessageService';
import {TareaDTO} from 'app/domain/tareaDTO';
import {TipoDocumento, VersionDocumento, VersionDocumentoDTO} from '../../models/DocumentoDTO';
import {AnexoDTO} from '../../models/DocumentoDTO';

@Component({
  selector: 'pd-datos-generales',
  templateUrl: './datos-generales.component.html'
})

export class PDDatosGeneralesComponent implements OnInit {

  form: FormGroup;

  validations: any = {};

  @Input()
  taskData: TareaDTO;

  funcionarioLog: FuncionarioDTO;

  tiposComunicacion$: Observable<ConstanteDTO[]>;
  tiposAnexo$: Observable<ConstanteDTO[]>;
  tiposPlantilla$: Observable<ConstanteDTO[]>;

  currentVersionObject : VersionDocumentoDTO;

  producirDocumento = {
    editarPlantillaVisible : false,
    currentVersionEditable : true,
    currentVersionVisible : false,
    currentVersionConfirmed : false,
    newVersionName : '',
    newVersionIndex : -1
  };
  listaVersionesDocumento: VersionDocumentoDTO[] = [];

  fechaCreacion = new Date();

  tipoPlantillaSelected: ConstanteDTO;

  listaAnexos: AnexoDTO[] = [];
  fileContent: {id: number; file: Blob };

  constructor(private _store: Store<State>,
              private _produccionDocumentalApi: ProduccionDocumentalApiService,
              private formBuilder: FormBuilder,
              private _changeDetectorRef: ChangeDetectorRef,
              private pdMessageService: PdMessageService) {
    this.initForm();
  }


    initForm() {
        this.form = this.formBuilder.group({
          'tipoComunicacion': [null, Validators.required],
          'tipoPlantilla': [null, Validators.required],
          'elaborarDocumento': [null],
          'soporte': 'electronico',
          'tipoAnexo': [null],
          'descripcion': [null],
        });
    }

    eliminarVersion(i) {
      this.removeFromList(i, 'listaVersionesDocumento');
      if (i === this.producirDocumento.newVersionIndex) {
        this.producirDocumento.newVersionIndex = -1;
      }
      this.producirDocumento.newVersionIndex--;
    }

    editarCurrentVersion() {
      this.producirDocumento.editarPlantillaVisible = false;
      this.currentVersionObject = null;
    }

    mostrarVersion(i) {
        if (i !== this.producirDocumento.newVersionIndex) {
          this.producirDocumento.currentVersionEditable = false;
        }
        this.currentVersionObject = this.listaVersionesDocumento[i];
        this.producirDocumento.editarPlantillaVisible = true;
    }

    generarVersion() {
        const versiones = this.listaVersionesDocumento;
        this.form.get('tipoPlantilla').reset();
        this.currentVersionObject.calculateSize();

        this.producirDocumento.newVersionIndex = versiones.push(this.currentVersionObject) - 1;
        this.listaVersionesDocumento = [...versiones];

        this.producirDocumento.editarPlantillaVisible = false;
        this.hideVersionesDocumentoDialog();
        this.refreshView();
    }

    tipoComunicacionChange(event) {
        this.pdMessageService.sendMessage(event.value);
    }

    tipoPlanillaChange(event) {
        this.tipoPlantillaSelected = event.value;
        this.producirDocumento.newVersionName = event.value.nombre + "_";
        this._produccionDocumentalApi.getTipoPlantilla({codigo:this.tipoPlantillaSelected.nombre.toLowerCase()}).subscribe(
          result =>
            this.currentVersionObject = new VersionDocumento(null, this.producirDocumento.newVersionName, result, 0, 'HTML'),
          error => console.log("Error :: " + error)
        );
    }

    hideVersionesDocumentoDialog() {
        this.producirDocumento.currentVersionConfirmed = false;
        this.producirDocumento.currentVersionVisible = false;
    }

    agregarAnexo() {
        const anexos = this.listaAnexos;
        const anexo: AnexoDTO = {
          id : (new Date()).getTime().toString(),
          soporte: this.form.get('soporte').value,
          tipo: this.form.get('tipoAnexo').value,
          descripcion: this.form.get('descripcion').value,
          file: this.fileContent
        };
        anexos.push(anexo);
        this.listaAnexos = [...anexos];
        this.refreshView();
    }

    removeFromList(i, listname: string) {
      const list = [...this[listname]];
      list.splice(i, 1);
      this[listname] = list;
      // this._changeDetectorRef.detectChanges();
    }

    ngOnInit(): void {
      this._store.select(getAuthenticatedFuncionario).subscribe((funcionario) => {
        this.funcionarioLog = funcionario;
      });
      this.tiposComunicacion$ = this._produccionDocumentalApi.getTiposComunicacionSalida({});
      this.tiposAnexo$ = this._produccionDocumentalApi.getTiposAnexo({});
      this.tiposPlantilla$ = this._produccionDocumentalApi.getTiposPlantilla({});
      this.listenForErrors();
    }


    listenForErrors() {
      this.bindToValidationErrorsOf('tipoPlantilla');
      this.bindToValidationErrorsOf('tipoComunicacion');
    }

    listenForBlurEvents(control: string) {
      const ac = this.form.get(control);
      if (ac.touched && ac.invalid) {
        const error_keys = Object.keys(ac.errors);
        const last_error_key = error_keys[error_keys.length - 1];
        this.validations[control] = VALIDATION_MESSAGES[last_error_key];
      }
    }

    bindToValidationErrorsOf(control: string) {
      const ac = this.form.get(control);
      ac.valueChanges.subscribe(value => {
        if ((ac.touched || ac.dirty) && ac.errors) {
          const error_keys = Object.keys(ac.errors);
          const last_error_key = error_keys[error_keys.length - 1];
          this.validations[control] = VALIDATION_MESSAGES[last_error_key];
        } else {
          delete this.validations[control];
        }
      });
    }

    visualizarDocumentos(index) {
      const file = this.listaAnexos[index].file;
      if (file === undefined || !file.id) {
        alert('Failed to open PDF.');
      } else {
        window.open(URL.createObjectURL(file.file), '_blank');
      }
    }

    onFileUploaded(event) {
        this.fileContent = event;
    }


    refreshView() {
      this._changeDetectorRef.detectChanges();
    }
}

