package co.com.foundation.sgd.apigateway.security.filters;

import java.io.IOException;
import java.security.Key;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang3.StringUtils;

import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import co.com.foundation.sgd.infrastructure.KeyManager;
import io.jsonwebtoken.Jwts;

@Provider
@JWTTokenSecurity
@Priority(Priorities.AUTHENTICATION)
public class JWTTokenFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		// Get the HTTP Authorization header from the request
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		// Extract the token from the HTTP Authorization header

		String token = null;

		try {

			
			if (StringUtils.isBlank(authorizationHeader)) {
				requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
				return;
			}

			token = authorizationHeader.substring("Bearer".length()).trim();
			// Validate the token
			Key key = KeyManager.getInstance().getKey();
			Jwts.parser().setSigningKey(key).parseClaimsJws(token);

		} catch (Exception e) {
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		}
	}

}
