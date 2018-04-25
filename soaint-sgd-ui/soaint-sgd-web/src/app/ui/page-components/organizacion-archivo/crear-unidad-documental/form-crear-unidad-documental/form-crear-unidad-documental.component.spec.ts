import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FormCrearUnidadDocumentalComponent } from './form-crear-unidad-documental.component';

describe('FormCrearUnidadDocumentalComponent', () => {
  let component: FormCrearUnidadDocumentalComponent;
  let fixture: ComponentFixture<FormCrearUnidadDocumentalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FormCrearUnidadDocumentalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FormCrearUnidadDocumentalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
