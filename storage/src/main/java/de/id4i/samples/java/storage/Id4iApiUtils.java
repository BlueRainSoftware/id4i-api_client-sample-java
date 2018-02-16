package de.id4i.samples.java.storage;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import de.id4i.ApiClient;
import de.id4i.ApiException;
import de.id4i.api.model.ApiError;
import de.id4i.auth.ApiKeyAuth;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.Charset;
import java.util.Date;

/**
 * Contains common utilities required in both producer and reseller applications.
 * In your application, you will probably have something similar established
 * as cross cutting functionality as well.
 */
public final class Id4iApiUtils {

    private static final String AUTHORIZATION_HEADER_BEARER_PREFIX = "Bearer";
    public static final String BASE_PATH = "https://id4i-sandbox.herokuapp.com/";
    private static Gson gson = new Gson();

    /**
     * Create a JWT access token valid for 30 seconds from the given API Key
     * ID and the signing secret and updates the given api client with it.
     *
     * @param applicationKey The application key from ID4i as shown at https://backend.id4i.de/#/apikeys/
     * @param secret         The signing secret given when creating the api key
     */
    public static void refreshToken(ApiClient client, String applicationKey, String secret) {
        byte[] secretKey = secret.getBytes(Charset.forName("utf-8"));
        String jwt = Jwts.builder()
            .setSubject(applicationKey)
            .setExpiration(new Date(System.currentTimeMillis() + 30_000))
            .setIssuedAt(new Date())
            .setHeaderParam(Header.TYPE, "API")
            .signWith(
                SignatureAlgorithm.HS512,
                secretKey)
            .compact();

        ApiKeyAuth authorization = (ApiKeyAuth) client.getAuthentication("Authorization");
        authorization.setApiKey(jwt);
        authorization.setApiKeyPrefix(AUTHORIZATION_HEADER_BEARER_PREFIX);
    }

    /**
     * Tries to retrieve the API Error representation from an {@link ApiException}.
     *
     * @param apiException
     * @return the {@link ApiError} instance from the exception representation or a new one
     * of type UNKNOWN containing the exception message if it does contain something different
     */
    public static ApiError deserialize(ApiException apiException) {
        try {
            return gson.fromJson(apiException.getResponseBody(), ApiError.class);
        } catch (JsonParseException jsonParseException) {
            ApiError apiError = new ApiError();
            apiError.setCode(ApiError.CodeEnum.UNKNOWN);
            apiError.setMessage(jsonParseException.getMessage());
            return apiError;
        }
    }
}
