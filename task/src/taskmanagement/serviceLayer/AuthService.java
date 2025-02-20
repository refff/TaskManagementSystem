package taskmanagement.serviceLayer;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.List;

@Service
public class AuthService {

    private final JwtEncoder jwtEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(JwtEncoder jwtEncoder, AuthenticationManager authenticationManager) {
        this.jwtEncoder = jwtEncoder;
        this.authenticationManager = authenticationManager;
    }

    public String authAndCreateToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null) {
            throw new BadCredentialsException("Full authentication is required");
        }

        String encoded = authHeader.substring(6);
        String decoded = new String(Base64.getDecoder().decode(encoded));
        String[] credentials = decoded.split(":");

        Authentication authToken = new UsernamePasswordAuthenticationToken(credentials[0], credentials[1]);
        Authentication auth = authenticationManager.authenticate(authToken);

        List<String> authorities = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .subject(auth.getName())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(60, ChronoUnit.MINUTES))
                .claim("scope", authorities)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
    }
}
