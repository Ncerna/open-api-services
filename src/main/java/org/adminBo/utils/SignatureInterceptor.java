package org.adminBo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.adminBo.wrapper.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Component
public class SignatureInterceptor implements HandlerInterceptor {

    @Value("${security.signature.secret}")
    private String secretKey;

    private final ObjectMapper objectMapper =
            new ObjectMapper();

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {
       // return  true;

        String signature = request.getHeader("Signature");
        if (signature == null || signature.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            ApiResponse<Object> apiResponse = ApiResponse.error(401,"Missing signature" );
            response.getWriter().write(
                    objectMapper.writeValueAsString(  apiResponse )
            );
            return false;
        }
        String body = request.getReader()
                .lines().reduce("", String::concat);
        String generatedSignature = generateHmac(body);
        if (!generatedSignature.equals(signature)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            ApiResponse<Object> apiResponse =
                    ApiResponse.error( 401, "Invalid signature" );
            response.getWriter().write( objectMapper.writeValueAsString( apiResponse  ) );
            return false;
        }
        return true;
    }

    private String generateHmac(String data) throws Exception {
        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec =
                new SecretKeySpec( secretKey.getBytes( StandardCharsets.UTF_8 ),"HmacSHA256" );
        sha256Hmac.init(secretKeySpec);
        byte[] hash = sha256Hmac.doFinal( data.getBytes(  StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append( String.format("%02x", b) );
        }
        return hexString.toString();
    }
}
