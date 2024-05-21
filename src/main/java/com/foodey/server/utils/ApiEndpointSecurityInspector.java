package com.foodey.server.utils;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

import com.foodey.server.annotation.PublicEndpoint;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Utility class responsible for evaluating the accessibility of API endpoints based on their
 * security configuration. It works in conjunction with the mappings of controller methods annotated
 * with {@link PublicEndpoint}.
 */
@Component
public class ApiEndpointSecurityInspector {

  private RequestMappingHandlerMapping requestHandlerMapping;

  @Getter
  private List<String> publicGetEndpoints =
      List.of("/swagger-ui**/**", "/v3/api-docs**/**", "/api/v1/test/**");

  @Getter private List<String> publicPostEndpoints = new ArrayList<>();

  public ApiEndpointSecurityInspector(
      @Qualifier("requestMappingHandlerMapping")
          RequestMappingHandlerMapping requestHandlerMapping) {

    this.requestHandlerMapping = requestHandlerMapping;
  }

  /**
   * Initializes the class by gathering public endpoints for various HTTP methods. It identifies
   * designated public endpoints within the application's mappings and adds them to separate lists
   * based on their associated HTTP methods. If OpenAPI is enabled, Swagger endpoints are also
   * considered as public.
   */
  @PostConstruct
  public void init() {
    final var handlerMethods = requestHandlerMapping.getHandlerMethods();

    handlerMethods.forEach(
        (requestInfo, handlerMethod) -> {
          if (handlerMethod.hasMethodAnnotation(PublicEndpoint.class)) {
            final HttpMethod httpMethod =
                requestInfo.getMethodsCondition().getMethods().iterator().next().asHttpMethod();
            final var apiPaths = requestInfo.getPathPatternsCondition().getPatternValues();

            if (httpMethod.equals(GET)) {
              publicGetEndpoints.addAll(apiPaths);
            } else if (httpMethod.equals(POST)) {
              publicPostEndpoints.addAll(apiPaths);
            }
          }
        });
  }

  /**
   * Checks if the provided HTTP request is directed towards an unsecured API endpoint.
   *
   * @param request The HTTP request to inspect.
   * @return {@code true} if the request is to an unsecured API endpoint, {@code false} otherwise.
   */
  public boolean isUnsecureRequest(@NonNull final HttpServletRequest request) {
    final HttpMethod requestHttpMethod = HttpMethod.valueOf(request.getMethod());

    return getUnsecuredApiPaths(requestHttpMethod).stream()
        .anyMatch(apiPath -> new AntPathMatcher().match(apiPath, request.getRequestURI()));
  }

  /**
   * Retrieves the list of unsecured API paths based on the provided HTTP method.
   *
   * @param httpMethod The HTTP method for which unsecured paths are to be retrieved.
   * @return A list of unsecured API paths for the specified HTTP method.s
   */
  private List<String> getUnsecuredApiPaths(@NonNull final HttpMethod httpMethod) {
    if (httpMethod.equals(GET)) return publicGetEndpoints;
    else if (httpMethod.equals(POST)) return publicPostEndpoints;
    return Collections.emptyList();
  }
}
