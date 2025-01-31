package quiz_peach.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenService {

    private static final String SECRET_KEY = "your_secret_key";

    public String generateToken(String username) {
        JwtBuilder builder = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes());
        return builder.compact();
    }

    // New method to extract and validate the token
    public String getUsernameFromRequest(HttpServletRequest request) {
        // Get the Authorization header
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Check if the header has the Bearer token
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
            if (validateToken(token)) {
                return extractUsername(token);
            }
        }
        return null; // Or throw an exception as needed
    }

    // Validate the token
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY.getBytes()).parseClaimsJws(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    // Extract username from token
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Extract claims from token
    private Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY.getBytes()).parseClaimsJws(token).getBody();
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}