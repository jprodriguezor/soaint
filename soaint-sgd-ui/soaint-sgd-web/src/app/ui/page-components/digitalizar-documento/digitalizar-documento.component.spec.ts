import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DigitalizarDocumentoComponent } from './digitalizar-documento.component';

describe('DigitalizarDocumentoComponent', () => {
  let component: DigitalizarDocumentoComponent;
  let fixture: ComponentFixture<DigitalizarDocumentoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DigitalizarDocumentoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DigitalizarDocumentoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
