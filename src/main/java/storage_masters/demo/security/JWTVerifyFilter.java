package storage_masters.demo.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import storage_masters.demo.services.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * - This filter will run every time we send a request and look for a token
 */
public class
JWTVerifyFilter extends OncePerRequestFilter {

    private final UserService userService;

    public JWTVerifyFilter(UserService userService) {
        this.userService = userService;
    }

    /**
     *
     * @param request - The request to be handled
     * @param response - The response to be handled
     * @param filterChain - The filterChain object
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException
    {
        //Checks for token in the header
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        var jwtToken = authorizationHeader.substring("Bearer ".length());

        if (jwtToken.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }
        /**
         * If there is a token, we decode it and
         * connect the user based on the username from the subject of the token
         */
        try {
            var algorithm = Algorithm.HMAC256("test");
            var verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build();

            DecodedJWT jwt = verifier.verify(jwtToken);

            var user = userService.loadUserByUsername(jwt.getSubject());

            var authentication = new UsernamePasswordAuthenticationToken(
                    user,
                    user.getPassword(),
                    user.getAuthorities()
            );
            /**
             * Sets the user in context, so we can access
             * our userObject based on the token that we send in
             */
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (JWTVerificationException exception){
            throw new IllegalStateException("Failed to authenticate");
        }

    }

}
