package de.id4i.samples;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * Hello ID4i!
 */
public class App
{
    private String jwt;

    private String createAccessToken() {
        String jwt = Jwts.builder()
            .setSubject("e94b006-d1d9-11e7-8941-cec278b6b50a")
            .setExpiration(new Date(System.currentTimeMillis() + 120000))
            .setIssuedAt(new Date())
            .setHeaderParam(Header.TYPE, "API")
            .signWith(
                SignatureAlgorithm.HS512,
                "afP/MCNXY8Jm4/scvYAJ9wDwuWiICXLHE0UEuXrw6zg")
            .compact();

        return jwt;
    }

    private void start() {
        jwt = createAccessToken();
        System.out.println( "Created access token " + jwt );

        
    }

    public static void main( String[] args )
    {
        App app = new App();
        app.start();
    }



}























