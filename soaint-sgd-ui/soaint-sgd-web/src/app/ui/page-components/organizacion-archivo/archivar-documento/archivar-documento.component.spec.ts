import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ArchivarDocumentoComponent } from './archivar-documento.component';

describe('ArchivarDocumentoComponent', () => {
  let component: ArchivarDocumentoComponent;
  let fixture: ComponentFixture<ArchivarDocumentoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ArchivarDocumentoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ArchivarDocumentoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
