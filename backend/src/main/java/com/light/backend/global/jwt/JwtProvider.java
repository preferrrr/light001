package com.light.backend.global.jwt;

import com.light.backend.member.domain.MemberRole;
import com.light.backend.member.exception.InvalidRefreshTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import static com.light.backend.global.exception.code.JwtExceptionCode.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;
    private static final long ACCESS_TOKEN_VALID_TIME = 60 * 60 * 1000L;
    private static final long REFRESH_TOKEN_VALID_TIME = 24 * 14 * 60 * 60 * 1000L;
    private static final String ACCESS_TOKEN_PREFIX = "Bearer ";
    private static final String AUTHORIZATION = "Authorization";
    private static final String ROLE = "role";
    private static final String VALUE = "value";

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createAccessToken(String id, MemberRole role) {
        Claims claims = Jwts.claims().setSubject(id);
        claims.put(ROLE, role.getValue());
        Date now = new Date();
        Date expiration = new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME);

        return ACCESS_TOKEN_PREFIX +
                Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(now)
                        .setExpiration(expiration)
                        .signWith(SignatureAlgorithm.HS256, secretKey)
                        .compact();
    }

    public String createRefreshToken(String value) {
        Claims claims = Jwts.claims();
        claims.put(VALUE, value);
        Date now = new Date();
        Date expiration = new Date(now.getTime() + REFRESH_TOKEN_VALID_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getToken(HttpServletRequest request) {
        if (request.getHeader(AUTHORIZATION) != null)
            return request.getHeader(AUTHORIZATION).substring(ACCESS_TOKEN_PREFIX.length());

        return null;
    }

    public Authentication getAuthentication(String token) {
        return new UsernamePasswordAuthenticationToken(
                getId(token),
                null,
                List.of(new SimpleGrantedAuthority(getRole(token)))
        );
    }

    public String getId(String token) {
        return getClaimsFromToken(token).getBody().getSubject();
    }

    public String getRole(String token) {
        return (String) getClaimsFromToken(token).getBody().get(ROLE);
    }

    public String getValue(String token) {
        return (String) getClaimsFromToken(token).getBody().get(VALUE);
    }

    private Jws<Claims> getClaimsFromToken(String token) throws JwtException {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
    }

    public boolean isTokenValid(String token) {
        try {
            return !getClaimsFromToken(token).getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            throw new CustomJwtException(EXPIRED);
        } catch (MalformedJwtException e) {
            throw new CustomJwtException(MALFORMED);
        } catch (SignatureException e) {
            throw new CustomJwtException(SIGNATURE);
        } catch (UnsupportedJwtException e) {
            throw new CustomJwtException(UNSUPPORTED);
        } catch (IllegalArgumentException e) {
            throw new CustomJwtException(ILLEGAL_ARGUMENT);
        } catch (JwtException e) {
            throw new CustomJwtException(INVALID_JWT);
        }
    }

    public boolean verifyRefreshToken(String token) {
        try {
            return !getClaimsFromToken(token).getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            throw new InvalidRefreshTokenException(EXPIRED);
        } catch (MalformedJwtException e) {
            throw new InvalidRefreshTokenException(MALFORMED);
        } catch (SignatureException e) {
            throw new InvalidRefreshTokenException(SIGNATURE);
        } catch (UnsupportedJwtException e) {
            throw new InvalidRefreshTokenException(UNSUPPORTED);
        } catch (IllegalArgumentException e) {
            throw new InvalidRefreshTokenException(ILLEGAL_ARGUMENT);
        } catch (JwtException e) {
            throw new InvalidRefreshTokenException(INVALID_JWT);
        }
    }

}