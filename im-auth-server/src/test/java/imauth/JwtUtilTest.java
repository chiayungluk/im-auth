package imauth;

import com.auth0.jwt.exceptions.JWTVerificationException;
import io.chaiyung.imauth.server.JwtUtil;
import io.chiayungluk.imauth.common.UserDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    @Test
    void verifyValidJWTToken() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImpvaG4yIiwiaWQiOiI2MzgyYTIwOWY5MWM0M2FhMDdkYzJkYjUiLCJpYXQiOjE2NzA0OTgyNTAsImV4cCI6MzE1NTI3MDQ5ODI1MH0.FncQ4jn3dsGJpuGyCcW-ZzUDNfdiJkSmgGGtWscuplQ";
        String secret = "sdfjsf";
        UserDto userDto = JwtUtil.verifyJWTToken(token, secret);
        assertEquals("john2", userDto.getUsername());
        assertEquals("6382a209f91c43aa07dc2db5", userDto.getId());
    }

    @Test
    void verifyInvalidJwtToken() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.sfjsldjfsfsdfasfasf.invalid";
        String secret = "sdfjsf";
        assertThrows(JWTVerificationException.class, () -> JwtUtil.verifyJWTToken(token, secret));
    }
}