package fithub.app.service.impl;

import fithub.app.base.Code;
import fithub.app.base.exception.handler.AppleOAuthException;
import fithub.app.service.AppleService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AppleServiceImpl implements AppleService {

    Logger logger = LoggerFactory.getLogger(AppleServiceImpl.class);

    @Override
    public String userIdFromApple(String identityToken) {
        StringBuffer result = new StringBuffer();
        try {
            URL url = new URL("https://appleid.apple.com/auth/keys");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";

            while ((line = br.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            throw new AppleOAuthException(Code.FAILED_TO_VALIDATE_APPLE_LOGIN);
        }

        JSONObject availableObject = null;
        try {
            JSONParser parser = new JSONParser();
            JSONObject apiInfo = (JSONObject) parser.parse(result.toString());
            JSONArray keys = (JSONArray)apiInfo.get("keys");

            String[] decodeArr = identityToken.split("\\.");

            logger.info("decode Arr의 값 : {}", decodeArr);
            String header = new String(Base64.getDecoder().decode(decodeArr[0]));

            JSONObject headerJson = (JSONObject) parser.parse(header);

            logger.info("identity token의 전자서명 정보 : {}",headerJson);

            Object kid = headerJson.get("kid");
            Object alg = headerJson.get("alg");

            for (int i = 0; i < keys.size(); i++){
                JSONObject appleObject = (JSONObject) keys.get(i);
                Object appleKid = appleObject.get("kid");
                Object appleAlg = appleObject.get("alg");

                if (Objects.equals(appleKid, kid) && Objects.equals(appleAlg, alg)){
                    availableObject = appleObject;
                    break;
                }
            }

            if (ObjectUtils.isEmpty(availableObject)){
                throw new AppleOAuthException(Code.FAILED_TO_FIND_AVALIABLE_RSA);
            }

        }
        catch (ParseException e){
            e.printStackTrace();
        }

        PublicKey publicKey = this.getPublicKey(availableObject);

        Claims userInfo = Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(identityToken).getBody();

        logger.info("파싱된 유저의 정보 : {}", userInfo);

        String userId = userInfo.get("sub", String.class);

        return userId;
    }

    public PublicKey getPublicKey(JSONObject object) {

        logger.info("전자서명을 위한 공개키 재료 : {}", object);

        String nStr = object.get("n").toString();
        String eStr = object.get("e").toString();

        byte[] nBytes = Base64.getUrlDecoder().decode(nStr);
        byte[] eBytes = Base64.getUrlDecoder().decode(eStr);

        BigInteger n = new BigInteger(1, nBytes);
        BigInteger e = new BigInteger(1, eBytes);

        try {
            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance(object.get("kty").toString());
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            return publicKey;
        } catch (Exception exception) {
            throw new AppleOAuthException(Code.FAILED_TO_FIND_AVALIABLE_RSA);
        }
    }
}

