import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import {
  QuestionTypesSessionEditModule,
} from '../../../components/question-types/question-types-session-edit/question-types-session-edit.module';
import { TemplateQuestionModalComponent } from './template-question-modal.component';

describe('TemplateQuestionModalComponent', () => {
  let component: TemplateQuestionModalComponent;
  let fixture: ComponentFixture<TemplateQuestionModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        CommonModule,
        FormsModule,
        QuestionTypesSessionEditModule,
      ],
      declarations: [
        TemplateQuestionModalComponent,
      ],
      providers: [
        NgbActiveModal,
      ],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TemplateQuestionModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
