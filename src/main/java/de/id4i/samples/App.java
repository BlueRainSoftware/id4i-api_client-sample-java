package de.id4i.samples;

import de.id4i.ApiClient;
import de.id4i.ApiException;
import de.id4i.Configuration;
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
    private String jwt;

    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

    private String createAccessToken() {
        String jwt = Jwts.builder()
            .setSubject("<your application key>")
            .setExpiration(new Date(System.currentTimeMillis() + 120000))
            .setIssuedAt(new Date())
            .setHeaderParam(Header.TYPE, "API")
            .signWith(
                SignatureAlgorithm.HS512,
                "<your api key secret>")
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























