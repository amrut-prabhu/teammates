package teammates.common.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.JsonObject;

import teammates.common.exception.TeammatesException;

/**
 * Used to handle the verification of the user's recaptcha response.
 */
public class RecaptchaVerifier {

    /** The Google reCAPTCHA API URL to verify the response token. */
    public static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    /** The shared secret key between the TEAMMATES site and reCAPTCHA. */
    public static final String SECRET_KEY = Config.CAPTCHA_SECRET_KEY;

    private static final Logger log = Logger.getLogger();

    /**
     * Returns true if the {@code recaptchaResponse} token is valid.
     * @param recaptchaResponse The user's captcha response from the client side
     * @return
     */
    public static boolean isVerificationSuccessful(String recaptchaResponse) {
        if (recaptchaResponse == null || recaptchaResponse.isEmpty()) {
            return false;
        }

        HttpsURLConnection connection = null;
        try {
            URL apiUrl = new URL(VERIFY_URL);
            connection = (HttpsURLConnection) apiUrl.openConnection();

            String postParams = new StringBuilder()
                    .append("secret=").append(SECRET_KEY)
                    .append("&response=").append(recaptchaResponse)
                    .toString();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Send post request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();

            log.info("Sent reCAPTCHA verification 'POST' request to URL : " + VERIFY_URL
                    + " with parameters : " + postParams);

            int responseCode = connection.getResponseCode();
            if (responseCode == 401 || responseCode == -1) {
                log.severe("reCAPTCHA verification API response code : " + responseCode);
                return false;
            }

            // Get response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuffer response = new StringBuffer();

            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            // Parse JSON response and return "success" value
            JsonObject jsonObject = JsonUtils.parse(response.toString()).getAsJsonObject();
            return Boolean.parseBoolean(jsonObject.get("success").toString());
        } catch (Exception e) {
            log.severe("Unexpected error while sending emails: " + TeammatesException.toStringWithStackTrace(e));
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
