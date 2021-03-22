package com.windvalley.guli.common.base.util;

import com.windvalley.guli.common.base.entry.JWTInfo;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.util.StringUtils;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Slf4j
public class JWTUtils {
    private static final int expir = 1800 * 1000;
    private static final String jwtSecret = PropertiesUtil.getProperty("jwt.secret", "");

    private static Key getKeyInstance() {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] bytes = DatatypeConverter.parseBase64Binary(jwtSecret);
        return new SecretKeySpec(bytes, signatureAlgorithm.getJcaName());
    }

    public static String generatorJWT(JWTInfo jwtInfo) {
        JwtBuilder jwtBuilder = Jwts.builder();

        //jwt头部分
        jwtBuilder.setHeaderParam("alg", "hs256");      //签名算法
        jwtBuilder.setHeaderParam("typ", "JWT");        //令牌类型

        //jwt有效载荷
        //默认字段
        jwtBuilder.setId(UUID.randomUUID().toString());     //身份标识
        jwtBuilder.setSubject("windvalley.user");           //令牌主题
        jwtBuilder.setIssuedAt(new Date());                 //签发时间
        jwtBuilder.setExpiration(DateTime.now().plusSeconds(expir).toDate()); //过期时间

        //私有字段
        jwtBuilder.claim("id", jwtInfo.getId());
        jwtBuilder.claim("nickName", jwtInfo.getNickName());
        jwtBuilder.claim("avatar", jwtInfo.getAvatar());

        //签名哈希
        jwtBuilder.signWith(SignatureAlgorithm.HS256, getKeyInstance());

        //将三部分连接起来
        String token = jwtBuilder.compact();

        return token;
    }

    public static boolean checkJWTToken(String token) {
        if (StringUtils.isEmpty(token) == true) {
            return false;
        }

        try {
            Jwts.parser().setSigningKey(getKeyInstance()).parseClaimsJws(token);
        } catch (Exception e) {
            log.error("获取用户token信息失败:" + e.getMessage());
            return false;
        }

        return true;
    }

    public static boolean checkJWTToken(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("token");

        return checkJWTToken(token);
    }

    public static JWTInfo getMemberInfoByToken(HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("token");

        if (checkJWTToken(token) == true){
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(getKeyInstance()).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            JWTInfo jwtInfo = new JWTInfo();
            jwtInfo.setAvatar(claims.get("avatar").toString());
            jwtInfo.setNickName(claims.get("nickName").toString());
            jwtInfo.setId(claims.get("id").toString());
            return jwtInfo;
        }

        return null;
    }
}