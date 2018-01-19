package de.id4i.samples.client.java;

import com.squareup.okhttp.Call;
import de.id4i.ApiCallback;
import de.id4i.ApiClient;
import de.id4i.ApiException;
import de.id4i.api.MetaInformationApi;
import de.id4i.api.model.AppInfoPresentation;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Hello ID4i!
 */
public class App {

    private static final String ENV_API_KEY = "ID4I_API_KEY";
    private static final String ENV_API_KEY_SECRET = "ID4I_API_KEY_SECRET";

    private String jwt;

    public static void main(String[] args) throws IOException, ApiException {
        App app = new App();
        app.start();
    }

    private String createAccessToken() {
        String subject = System.getenv(ENV_API_KEY);
        String secret = System.getenv(ENV_API_KEY_SECRET);

        if (subject == null || secret == null) {
            throw new IllegalStateException(
                "Could not find API key and secret to create JWT. Are the environment variables "
                    + ENV_API_KEY + " and "
                    + ENV_API_KEY_SECRET + " set?");
        }
        String jwt = Jwts.builder()
            .setSubject(subject)
            .setExpiration(new Date(System.currentTimeMillis() + 120000))
            .setIssuedAt(new Date())
            .setHeaderParam(Header.TYPE, "API")
            .signWith(
                SignatureAlgorithm.HS512,
                secret)
            .compact();

        return jwt;
    }

    private void start() throws IOException, ApiException {
        jwt = createAccessToken();

        System.out.println("Created access token " + jwt);

        ApiClient myCustomApiClient = new ApiClient();
        myCustomApiClient.setUserAgent("id4i-client-sample");
        MetaInformationApi apiInstance = new MetaInformationApi();
        apiInstance.setApiClient(myCustomApiClient);

        String authorization = "Bearer " + jwt;
        String acceptLanguage = "en";
        callApiInfo(apiInstance, authorization, acceptLanguage);


        // You can also try the async call.
        // Please note that the application will take some time to close in that case.
        // callApiInfoAsync(apiInstance, authorization, acceptLanguage);
    }

    private void callApiInfo(MetaInformationApi apiInstance, String authorization, String acceptLanguage) {

        try {
            AppInfoPresentation result = apiInstance.applicationInfo(authorization, acceptLanguage);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling MetaInformationApi#applicationInfo");
            e.printStackTrace();
        }
    }

    private void callApiInfoAsync(MetaInformationApi apiInstance, String authorization, String acceptLanguage) throws ApiException {
        ApiCallback<AppInfoPresentation> callback = createApiInfoCallback();
        apiInstance.applicationInfoAsync(authorization, acceptLanguage, callback);
    }

    private ApiCallback<AppInfoPresentation> createApiInfoCallback() {
        return new ApiCallback<AppInfoPresentation>() {

            public void onFailure(ApiException e, int i, Map<String, List<String>> map) {
                System.err.println("Exception when calling MetaInformationApi#applicationInfo");
            }

            public void onSuccess(AppInfoPresentation appInfoPresentation, int i, Map<String, List<String>> map) {
                System.out.println(appInfoPresentation);
            }

            public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {
                System.out.println("received " + bytesRead + " bytes");
                if (done) System.out.println("done");
            }

            public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {
                System.out.println("received " + bytesWritten + " of " + contentLength);
                if (done) System.out.println("done");
            }
        };
    }


}























