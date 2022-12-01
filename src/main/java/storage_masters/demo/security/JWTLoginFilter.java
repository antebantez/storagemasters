package storage_masters.demo.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    /**
     *
     * @param request The request to be handled
     * @param response - The response to be handled
     * @return - Returns a UsernamePasswordAuthenticationFilter object
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException
    {
        var username = request.getHeader("username");
        var password = request.getHeader("password");


        var authentication = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authentication);
    }

    /**
     *
     * @param request - The request to be handled
     * @param response - The response to be handled
     * @param chain -  The filterchain that loch
     * @param authResult - This is an Authentication object
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult)
            throws IOException, ServletException
    {
        try {
            //JWT token is created
            var algorithm = Algorithm.HMAC256("test");
            var token = JWT.create()
                    .withIssuer("auth0")
                    .withSubject(authResult.getName())
                    .sign(algorithm);

            response.addHeader("Access-Control-Expose-Headers", "Authorization");
            response.addHeader("Authorization", "Bearer " + token);

        } catch (JWTCreationException exception){
            exception.printStackTrace();
        }
    }

}
