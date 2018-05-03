import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FormEnvioComponent } from './form-envio.component';

describe('FormEnvioComponent', () => {
  let component: FormEnvioComponent;
  let fixture: ComponentFixture<FormEnvioComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FormEnvioComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FormEnvioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
