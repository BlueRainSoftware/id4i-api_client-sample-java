package de.id4i.samples.client.java;

import de.id4i.ApiException;
import de.id4i.api.MetaInformationApi;
import de.id4i.api.model.AppInfoPresentation;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * Hello ID4i!
 */
public class App {

    private static final String ENV_API_KEY = "ID4I_API_KEY";
    private static final String ENV_API_KEY_SECRET = "ID4I_API_KEY_SECRET";

    private String jwt;

    public static void main(String[] args) {
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

    private void start() {
        jwt = createAccessToken();
        System.out.println("Created access token " + jwt);

        MetaInformationApi apiInstance = new MetaInformationApi();
        String authorization = "Bearer " + jwt; // String | Authorization JWT Bearer Token as returned from /login
        String acceptLanguage = "de"; // String | Requested language
        try {
            AppInfoPresentation result = apiInstance.applicationInfo(authorization, acceptLanguage);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling MetaInformationApi#applicationInfo");
            e.printStackTrace();
        }

    }


}























