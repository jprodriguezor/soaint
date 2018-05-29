package co.com.foundation.sgd.infrastructure;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

public class KeyManagerTest {

    @Test
    public void getInstance() {
        assertThat(KeyManager.getInstance()).isNotNull();
    }

    @Test
    public void issueToken() {

        // given
        String LOGIN = "LOGIN";
        String ISSUER = "ISSUER";
        KeyManager keyManager = KeyManager.getInstance();

        // when
        String token = keyManager.issueToken(LOGIN, ISSUER);

        // then
        Jwt jwt = Jwts.parser().setSigningKey(keyManager.getKey()).parseClaimsJws(token);

        Map<String, String> body = (Map<String, String>) jwt.getBody();

        assertThat(body).contains(
                entry("sub", LOGIN),
                entry("iss", ISSUER)
        );

        assertThat(jwt.getHeader().get("alg"))
                .isEqualTo(SignatureAlgorithm.HS512.getValue());
    }

    @Test
    public void getKey() {
        assertThat(KeyManager.getInstance().getKey()).isNotNull();
    }
}