import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NoTramitarCreacionUdComponent } from './no-tramitar-creacion-ud.component';

describe('NoTramitarCreacionUdComponent', () => {
  let component: NoTramitarCreacionUdComponent;
  let fixture: ComponentFixture<NoTramitarCreacionUdComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NoTramitarCreacionUdComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NoTramitarCreacionUdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
