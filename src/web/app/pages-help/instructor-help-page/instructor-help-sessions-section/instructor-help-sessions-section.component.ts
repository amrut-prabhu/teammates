import { DOCUMENT } from '@angular/common';
import { Component, Inject, Input, OnInit } from '@angular/core';
import { PageScrollService } from 'ngx-page-scroll-core';

import { TemplateSession } from '../../../../services/feedback-sessions.service';
import {
  Course,
  FeedbackSessionPublishStatus,
  FeedbackSessionSubmissionStatus,
  ResponseVisibleSetting,
  SessionVisibleSetting,
} from '../../../../types/api-output';
import {
  SessionEditFormMode, SessionEditFormModel,
} from '../../../components/session-edit-form/session-edit-form-model';
import { InstructorHelpSectionComponent } from '../instructor-help-section.component';

/**
 * Sessions Section of the Instructor Help Page.
 */
@Component({
  selector: 'tm-instructor-help-sessions-section',
  templateUrl: './instructor-help-sessions-section.component.html',
  styleUrls: ['./instructor-help-sessions-section.component.scss'],
})
export class InstructorHelpSessionsSectionComponent extends InstructorHelpSectionComponent implements OnInit {

  // enum
  SessionEditFormMode: typeof SessionEditFormMode = SessionEditFormMode;

  readonly exampleSessionEditFormModel: SessionEditFormModel = {
    courseId: 'CS2103T',
    timeZone: 'UTC',
    courseName: 'Software Engineering',
    feedbackSessionName: 'Feedback for Project',
    instructions: 'This is where you type the instructions for the session',

    submissionStartTime: { hour: 10, minute: 0 },
    submissionStartDate: { year: 2020, month: 3, day: 13 },
    submissionEndTime: { hour: 12, minute: 0 },
    submissionEndDate: { year: 2020, month: 3, day: 13 },
    gracePeriod: 0,

    sessionVisibleSetting: SessionVisibleSetting.AT_OPEN,
    customSessionVisibleTime: { hour: 9, minute: 0 },
    customSessionVisibleDate: { year: 2020, month: 3, day: 13 },

    responseVisibleSetting: ResponseVisibleSetting.CUSTOM,
    customResponseVisibleTime: { hour: 13, minute: 0 },
    customResponseVisibleDate: { year: 2020, month: 3, day: 13 },

    submissionStatus: FeedbackSessionSubmissionStatus.OPEN,
    publishStatus: FeedbackSessionPublishStatus.NOT_PUBLISHED,

    isClosingEmailEnabled: true,
    isPublishedEmailEnabled: true,

    templateSessionName: 'Example session',

    isSaving: false,
    isEditable: false,
    hasVisibleSettingsPanelExpanded: true,
    hasEmailSettingsPanelExpanded: true,
  };

  readonly exampleCourseCandidates: Course[] = [
    {
      courseId: 'CS2103T',
      courseName: 'Software Engineering',
      timeZone: 'UTC',
      creationTimestamp: 0,
      deletionTimestamp: 0,
    },
  ];

  readonly exampleTemplateSessions: TemplateSession[] = [
    {
      name: 'Example session',
      questions: [],
    },
  ];

  @Input() isPeerEvalTipsCollapsed: boolean = false;
  isNewFeedbackSessionCollapsed: boolean = false;
  isAddQuestionsCollapsed: boolean = false;
  isPreviewCollapsed: boolean = false;
  isCannotSubmitCollapsed: boolean = false;
  isViewResultsCollapsed: boolean = false;
  isViewAllResponsesCollapsed: boolean = false;
  isAddCommentCollapsed: boolean = false;
  isEditDelCommentCollapsed: boolean = false;
  isSearchCollapsed: boolean = false;
  isViewDeletedCollapsed: boolean = false;
  isRestoreSessionCollapsed: boolean = false;
  isDelSessionCollapsed: boolean = false;
  isRestoreDelAllCollapsed: boolean = false;

  constructor(private pageScrollService: PageScrollService,
              @Inject(DOCUMENT) private document: any) {
    super();
  }

  /**
   * Scrolls to an HTML element with a given target id.
   */
  jumpTo(target: string): boolean {
    this.pageScrollService.scroll({
      document: this.document,
      scrollTarget: `#${target}`,
      scrollOffset: 70,
    });
    return false;
  }
}
