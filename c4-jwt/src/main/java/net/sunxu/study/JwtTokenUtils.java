package net.sunxu.study;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class JwtTokenUtils {

    private AtomicLong nextTokenId = new AtomicLong();

    private ConcurrentMap<String, Long> unUsedTokenIds = new ConcurrentHashMap<>();

    @Value("${jwt.secret}")
    private String secret;

    @PostConstruct
    private void init() {
        // 下面需要用到secret 的地方都需要secret 是base64 编码的
        secret = new String(Base64.getEncoder().encode(secret.getBytes()));
    }

    public String createJwt(String userName) {
        SignatureAlgorithm alg = SignatureAlgorithm.HS256;
        var claims = new HashMap<String, Object>();
        claims.put("userName", userName);

        String tokenId = Long.valueOf(nextTokenId.addAndGet(1)).toString();
        unUsedTokenIds.put(tokenId, System.currentTimeMillis());

        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setId(tokenId)
                .signWith(alg, secret);
        return builder.compact();
    }

    public String getUserNameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return (String) claims.get("userName");
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
            if (unUsedTokenIds.containsKey(claims.getId())) {
                unUsedTokenIds.remove(claims.getId());
                return true;
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
        return false;
    }
}
