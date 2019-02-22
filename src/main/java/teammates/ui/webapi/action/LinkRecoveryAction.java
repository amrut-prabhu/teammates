package teammates.ui.webapi.action;

import teammates.common.util.Const;
import teammates.common.util.EmailWrapper;
import teammates.common.util.RecaptchaVerifier;
import teammates.logic.api.EmailGenerator;
import teammates.ui.webapi.output.EmailRestoreResponseData;

/**
 * Action specifically created for confirming email and sending recovery link.
 */
public class LinkRecoveryAction extends Action {

    @Override
    protected AuthType getMinAuthLevel() {
        return AuthType.PUBLIC;
    }

    @Override
    public void checkSpecificAccessControl() {
        // no specific access control needed.
    }

    @Override
    public ActionResult execute() {
        String requestedEmail = getNonNullRequestParamValue(Const.ParamsNames.RECOVERY_EMAIL);
        String userRecaptchaResponse = getNonNullRequestParamValue(Const.ParamsNames.USER_RECAPTCHA_RESPONSE);

        if (!RecaptchaVerifier.isVerificationSuccessful(userRecaptchaResponse)) {
            return new JsonResult(new EmailRestoreResponseData(false, "reCAPTCHA verification was unsuccessful."));
        }

        boolean hasStudentsWithRestoreEmail = !logic.getAllStudentForEmail(requestedEmail).isEmpty();

        if (hasStudentsWithRestoreEmail) {
            EmailWrapper email = new EmailGenerator().generateLinkRecoveryEmail(requestedEmail);
            emailSender.sendEmail(email);
            return new JsonResult(new EmailRestoreResponseData(true,
                    "The recovery links for your feedback sessions have been sent to the specified email."));
        } else {
            return new JsonResult(new EmailRestoreResponseData(false,
                    "No student is registered under email: " + requestedEmail));
        }
    }
}
