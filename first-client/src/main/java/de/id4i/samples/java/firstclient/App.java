package de.id4i.samples.java.firstclient;

import com.google.gson.Gson;
import de.id4i.*;
import de.id4i.api.MetaInformationApi;
import de.id4i.api.model.ApiError;
import de.id4i.api.model.AppInfoPresentation;
import de.id4i.auth.ApiKeyAuth;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.IOException;
import java.nio.charset.Charset;
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

    String createAccessToken(String subject, String secret) {
        if (subject == null || secret == null) {
            throw new IllegalStateException(
                "Could not find API key and secret to create JWT. Are the environment variables "
                    + ENV_API_KEY + " and "
                    + ENV_API_KEY_SECRET + " set?");
        }
        byte[] secretKey = secret.getBytes(Charset.forName("utf-8"));
        String jwt = Jwts.builder()
            .setSubject(subject)
            .setExpiration(new Date(System.currentTimeMillis() + 120000))
            .setIssuedAt(new Date())
            .setHeaderParam(Header.TYPE, "API")
            .signWith(
                SignatureAlgorithm.HS512,
                secretKey)
            .compact();

        return jwt;
    }

    private void start() throws IOException, ApiException {
        String subject = System.getenv(ENV_API_KEY);
        String secret = System.getenv(ENV_API_KEY_SECRET);

        jwt = createAccessToken(subject, secret);

        System.out.println("Created access token " + jwt);



        MetaInformationApi apiInstance = new MetaInformationApi();

        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setUserAgent("id4i-client-sample");
        ApiKeyAuth authorization = (ApiKeyAuth) defaultClient.getAuthentication("Authorization");
        authorization.setApiKey(jwt);
        authorization.setApiKeyPrefix("Bearer");

        defaultClient.addDefaultHeader("Accept-Language", "en");
        defaultClient.addDefaultHeader("Accept", "application/json");

        callApiInfo(apiInstance);

        // You can also try the async call.
        // Please note that the application will take some time to close in that case.
        // callApiInfoAsync(apiInstance);
    }

    private void callApiInfo(MetaInformationApi apiInstance) {

        try {
            ApiResponse<AppInfoPresentation> result = apiInstance.applicationInfoWithHttpInfo();
            System.out.println(result.getData());
        } catch (ApiException e) {
            // The response body contains a serizalized ApiError. As opposed tp
            // business types, errors are not deserialized automatically.
            // If you want to deserialize the API Error that was returned, you need
            // something like the following.
            ApiError apiError = new Gson().fromJson(e.getResponseBody(),ApiError.class);
            System.out.println(apiError);
        }
    }

    private void callApiInfoAsync(MetaInformationApi apiInstance) throws ApiException {
        ApiCallback<AppInfoPresentation> callback = createApiInfoCallback();
        apiInstance.applicationInfoAsync(callback);
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























