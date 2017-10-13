import {Component, Input, OnInit, Output, ViewChild, EventEmitter} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {VALIDATION_MESSAGES} from '../../../shared/validation-messages';
import {Sandbox as AsignacionSandbox} from '../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-sandbox';
import {OrganigramaDTO} from '../../../domain/organigramaDTO';
import {AgentDTO} from '../../../domain/agentDTO';
import {Sandbox as TaskSandbox} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';

@Component({
  selector: 'app-gestionar-comunicacion',
  templateUrl: './gestionar-comunicacion.component.html',
  styleUrls: ['./gestionar-comunicacion.component.scss']
})
export class GestionarComunicacionComponent implements OnInit {

  procesosSuggestions: [{ nombre: string, id: number }];

  form: FormGroup;

  validations = {};

  @Input()
  remitente: AgentDTO;

  @Input()
  task: any;

  @Output()
  onDevolverTriggered = new EventEmitter<any>();

  @Output()
  onRedireccionarTriggered = new EventEmitter<any>();

  procesoSeguir: number;

  rejectDialogVisible: boolean = false;

  justificationDialogVisible: boolean = false;

  hideCheckBox: boolean = true;

  @ViewChild('popupjustificaciones') popupjustificaciones;

  @ViewChild('popupReject') popupReject;

  constructor(private formBuilder: FormBuilder,
              private _asignacionSandbox: AsignacionSandbox,
              private _tareaSandbox: TaskSandbox) {
    this.initForm();
    this.listenForErrors();
  }

  ngOnInit() {
    this.procesosSuggestions = [{
      nombre: 'Archivar documento',
      id: 1
    }, {
      nombre: 'Devolver',
      id: 2
    }, {
      nombre: 'Documento de apoyo',
      id: 3
    }, {
      nombre: 'Producir documento',
      id: 4
    }, {
      nombre: 'Redireccionar',
      id: 5
    }];
  }

  gestionarProceso() {
    console.log(this.form.get('proceso').value);
    console.log(this.form.get('responseToRem').value);
    switch (this.form.get('proceso').value.id) {
      case 1 : {
        this.procesoSeguir = 2;
        this.completeTask();
        break;
      }
      case 2 : {
        this.rejectDialogVisible = true;
        break;
      }
      case 3 : {
        this.procesoSeguir = 3;
        this.completeTask();
        break;
      }
      case 4 : {
        this.procesoSeguir = 4;
        this.completeTask();
        break;
      }
      case 5 : {
        this.procesoSeguir = 0;
        this.justificationDialogVisible = true;
        break;
      }
    }
  }

  sendReject() {
    this.procesoSeguir = this.popupReject.form.get('causalDevolucion').value.id === 1 ? 5 : 6;
    this.completeTask();
    this.onDevolverTriggered.emit();

  }

  completeTask() {
    this._tareaSandbox.completeTaskDispatch(this.getTaskToCompletePayload());
  }

  getTaskToCompletePayload() {
    return {
      idProceso: this.task.idProceso,
      idDespliegue: this.task.idDespliegue,
      idTarea: this.task.idTarea,
      parametros: {
        procesoSeguir: this.procesoSeguir
      }
    }
  }

  redirectComunications(justificationValues: { justificacion: string, sedeAdministrativa: OrganigramaDTO, dependenciaGrupo: OrganigramaDTO }) {
    this.onRedireccionarTriggered.emit({
      justificationValues: justificationValues,
      taskToCompletePayload: this.getTaskToCompletePayload()
    });
    this.justificationDialogVisible = false;
  }

  onChange() {
    this.form.get('responseToRem').disable();
    this.hideCheckBox = true;
    if (this.form.get('proceso').value.id === 4) {
      this.hideCheckBox = false;
      this.form.get('responseToRem').enable();
    }
  }


  createAgentes(justificationValues: { justificacion: string, sedeAdministrativa: OrganigramaDTO, dependenciaGrupo: OrganigramaDTO }): AgentDTO[] {
    let agentes: AgentDTO[] = [];
    let agente = this.remitente;
    console.log(this.remitente);
    agente.ideAgente = this.task.variables.idAgente;
    agente.codSede = justificationValues.sedeAdministrativa.codigo;
    agente.codDependencia = justificationValues.dependenciaGrupo.codigo;
    delete agente['_$visited'];
    agentes.push(agente);
    return agentes;
  }

  initForm() {
    this.form = this.formBuilder.group({
      'proceso': [{value: null, disabled: false}, Validators.required],
      'responseToRem': [{value: false, disabled: true}, Validators.required]
    });
  }

  listenForErrors() {
    this.bindToValidationErrorsOf('responseToRem');
    this.bindToValidationErrorsOf('proceso');
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

  listenForBlurEvents(control: string) {
    const ac = this.form.get(control);
    if (ac.touched && ac.invalid) {
      const error_keys = Object.keys(ac.errors);
      const last_error_key = error_keys[error_keys.length - 1];
      this.validations[control] = VALIDATION_MESSAGES[last_error_key];
    }
  }

  hideRejectDialog() {
    this.rejectDialogVisible = false;
  }

  hideJustificationPopup() {
    this.justificationDialogVisible = false;
  }

  sendRedirect() {
    this.popupjustificaciones.redirectComunications();
  }

}
