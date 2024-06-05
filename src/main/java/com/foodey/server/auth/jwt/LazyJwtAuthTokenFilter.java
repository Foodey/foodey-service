package com.foodey.server.auth.jwt;

import com.foodey.server.auth.enums.TokenType;
import com.foodey.server.exceptions.InvalidTokenRequestException;
import com.foodey.server.user.model.User;
import com.foodey.server.user.repository.UserRepository;
import com.foodey.server.utils.ApiEndpointSecurityInspector;
import com.foodey.server.utils.HttpHeaderUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Slf4j
@Component
// @RequiredArgsConstructor
public class LazyJwtAuthTokenFilter extends OncePerRequestFilter {

  @SuppressWarnings("unused")
  private static final long serialVersionUID = 1L;

  private JwtService jwtService;
  private UserRepository userRepository;
  private ApiEndpointSecurityInspector apiEndpointSecurityInspector;
  private HandlerExceptionResolver resolver;

  public LazyJwtAuthTokenFilter(
      JwtService jwtService,
      UserRepository userRepository,
      ApiEndpointSecurityInspector apiEndpointSecurityInspector,
      @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
    this.jwtService = jwtService;
    this.userRepository = userRepository;
    this.apiEndpointSecurityInspector = apiEndpointSecurityInspector;
    this.resolver = resolver;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    return apiEndpointSecurityInspector.isUnsecureRequest(request);
  }

  @Override
  @SneakyThrows
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    try {
      SecurityContext context = SecurityContextHolder.getContext();
      if (context.getAuthentication() == null) {

        String jwtToken = HttpHeaderUtils.extractBearerToken(request);

        String userPubId = jwtService.extractSubject(jwtToken);

        if (userPubId != null) {
          log.debug("Processing authentication for userPubId: {}", userPubId);

          User user =
              userRepository
                  .findByPubId(userPubId)
                  .orElseThrow(
                      () ->
                          new InvalidTokenRequestException(
                              TokenType.BEARER, jwtToken, "User not found"));

          if (jwtService.isAccessTokenValid(jwtToken, user)) {
            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            context.setAuthentication(authentication);
            log.debug("User {} successfully authenticated with pubId {}", user.getId(), userPubId);
          }
        }
      }
      filterChain.doFilter(request, response);
    } catch (MissingServletRequestPartException e) {
      resolver.resolveException(request, response, null, e);
    } catch (Exception e) {
      resolver.resolveException(request, response, null, e);
    }
  }
}
