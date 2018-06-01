import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InformacionEnvioComponent } from './informacion-envio.component';

describe('InformacionEnvioComponent', () => {
  let component: InformacionEnvioComponent;
  let fixture: ComponentFixture<InformacionEnvioComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InformacionEnvioComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InformacionEnvioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
