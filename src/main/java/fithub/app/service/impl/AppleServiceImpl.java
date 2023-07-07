package fithub.app.service.impl;

import fithub.app.exception.common.ErrorCode;
import fithub.app.exception.handler.AppleOAuthException;
import fithub.app.service.AppleService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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

    @Override
    public String userIdFromApple(String identityToken) {
        /**
         * 1. apple로 부터 공개키 3개 가져옴
         * 2. 내가 클라에서 가져온 token String과 비교해서 써야할 공개키 확인 (kid,alg 값 같은 것)
         * 3. 그 공개키 재료들로 공개키 만들고, 이 공개키로 JWT토큰 부분의 바디 부분의 decode하면 유저 정보
         */
        StringBuffer result = new StringBuffer();
        try {
            String appleApiUrl = "https://appleid.apple.com/auth/keys";

            URL url = new URL(appleApiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";

            while ((line = br.readLine()) != null) {
                result.append(line);
            }
        }catch (IOException e) {
            throw new AppleOAuthException(ErrorCode.FAILED_TO_VALIDATE_APPLE_LOGIN);
        }

        JSONObject availableObject = null;
        try {
            JSONParser parser = new JSONParser();
            JSONObject apiInfo = (JSONObject) parser.parse(result.toString());
            JSONArray keys = (JSONArray)apiInfo.get("keys");

            String[] decodeArr = identityToken.split("\\.");
            String header = new String(Base64.getDecoder().decode(decodeArr[0]));

            JSONObject headerJson = (JSONObject) parser.parse(header);
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
                throw new AppleOAuthException(ErrorCode.FAILED_TO_FIND_AVALIABLE_RSA);
            }

        }
        catch (ParseException e){
            e.printStackTrace();
        }

        PublicKey publicKey = this.getPublicKey(availableObject);

        Claims userInfo = Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(identityToken).getBody();

        JSONObject userObject = null;

        try {
            JSONParser parser = new JSONParser();
            userObject = (JSONObject) parser.parse(userInfo.toString());
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

//        Object appleAlg =  userObject.get("sub");
//        String userId = appleAlg
        return "temp";
    }

    public PublicKey getPublicKey(JSONObject object){
        String nStr = object.get("n").toString();
        String eStr = object.get("e").toString();

        byte[] nBytes = Base64.getUrlDecoder().decode(nStr.substring(1, nStr.length() - 1));
        byte[] eBytes = Base64.getUrlDecoder().decode(eStr.substring(1, eStr.length() - 1));

        BigInteger n = new BigInteger(1, nBytes);
        BigInteger e = new BigInteger(1, eBytes);

        try {
            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            return publicKey;
        } catch (Exception exception) {
            throw new AppleOAuthException(ErrorCode.FAILED_TO_FIND_AVALIABLE_RSA);
        }
    }
}

