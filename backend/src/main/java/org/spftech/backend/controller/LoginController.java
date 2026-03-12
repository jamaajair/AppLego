package org.spftech.backend.controller;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Value("${security.jwt.secret}")
    private String secret;

    /**
     * Authenticates a user and generates a signed JWT token
     *
     * @param requires the login request containing the username
     * @return a TokenResponse containing the generated JWT and metadata
     * @throws Exception if an error occurs during token creation or signing
     *
     * Note: This implementation is for development/testing purposes only and we will have to deploy Users tables in the database and implement proper authentication and password handling for production use.
     */
    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest req) throws Exception {

        String userId = req.username();
        List<String> groups = groupsFor(userId);

        Instant now = Instant.now();
        Instant exp = now.plus(4, ChronoUnit.HOURS);

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .issuer("spftech-poc")
                .subject(userId)
                .issueTime(Date.from(now))
                .expirationTime(Date.from(exp))
                .claim("groups", groups)
                .build();

        SignedJWT jwt = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);
        jwt.sign(new MACSigner(keyBytes()));

        return new TokenResponse(jwt.serialize(), "Bearer", 4 * 3600);
    }

    /**
     * Determines the groups associated with a given user.
     *
     * @param userId the user identifier
     * @return the list of groups assigned to the user
     *
     */
    private List<String> groupsFor(String userId) {
        if (userId == null) return List.of("EMPLOYEES");
        return switch (userId) {
            case "admin" -> List.of("ADMIN");
            case "supervisor" -> List.of("SUPERVISORS");
            default -> List.of("EMPLOYEES");
        };
    }

    /**
     * Returns the signing key as a byte array.
     *
     * If the configured secret is Base64-encoded, it is decoded.
     * Otherwise, the raw UTF-8 bytes of the string are used.
     *
     * @return the secret key bytes
     */
    private byte[] keyBytes() {
        String s = secret == null ? "" : secret.strip();
        try {
            return Base64.getDecoder().decode(s);
        } catch (IllegalArgumentException e) {
            return s.getBytes(StandardCharsets.UTF_8);
        }
    }

    /**
     * Returns information about the currently authenticated user.
     *
     * @param auth the authenticated JWT token injected by Spring Security
     * @return a map containing user ID, groups, issuer, and expiration timestamp
     */
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public Map<String, Object> me(JwtAuthenticationToken auth) {
        var jwt = auth.getToken();
        return Map.of(
                "userId", jwt.getSubject(),
                "groups", jwt.getClaimAsStringList("groups"),
                "issuer", jwt.getClaimAsString("iss"),
                "expiresAt", jwt.getExpiresAt()
        );
    }

    public record LoginRequest(String username) {}
    public record TokenResponse(String access_token, String token_type, long expires_in) {}
}
