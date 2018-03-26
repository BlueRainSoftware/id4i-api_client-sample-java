package de.id4i.samples.java.common;


import de.id4i.ApiClient;
import de.id4i.auth.ApiKeyAuth;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import org.junit.Test;

import java.nio.charset.Charset;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class JwtTokenTest {
    @Test
    public void jwtTokenIsInitialized() {
        ApiClient apiClient = new ApiClient();
        Id4iApiUtils.refreshToken(apiClient, "test-api-key", "my secret");
        ApiKeyAuth authorization = (ApiKeyAuth) apiClient.getAuthentication("Authorization");

        Jwt<Header, Claims> jwt = Jwts.parser()
            .setSigningKey("my secret".getBytes(Charset.forName("utf-8"))).parse(authorization.getApiKey());

        assertThat(jwt.getHeader().getType(), is("API"));
        assertThat(jwt.getBody().getSubject(), is("test-api-key"));
        assertTrue("Expiration date must be in the future",
            jwt.getBody().getExpiration().getTime() > System.currentTimeMillis());
    }
}
