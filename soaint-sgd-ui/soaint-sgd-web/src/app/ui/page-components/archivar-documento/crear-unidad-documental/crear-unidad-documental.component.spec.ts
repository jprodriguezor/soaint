import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CrearUnidadDocumentalComponent } from './crear-unidad-documental.component';

describe('CrearUnidadDocumentalComponent', () => {
  let component: CrearUnidadDocumentalComponent;
  let fixture: ComponentFixture<CrearUnidadDocumentalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CrearUnidadDocumentalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CrearUnidadDocumentalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
