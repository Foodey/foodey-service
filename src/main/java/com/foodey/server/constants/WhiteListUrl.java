package com.foodey.server.constants;

/** WhiteListUrl */
public class WhiteListUrl {
  public static final String[] GET_METHODS = {
    "/api/v1/product-categories/**",
    "/api/v1/branches/**",
    "/api/v1/products/**",
    "/api/v1/search/**",
    "/api/v1/restaurants/**",
    "/api/v1/vouchers/**",
    "/api/v1/customer/favorites/**",
  };

  public static final String[] POST_METHODS = {};

  public static final String[] PUT_METHODS = {};

  public static final String[] DELETE_METHODS = {};

  public static final String[] PATCH_METHODS = {
    "/api/v1/admin/**",
  };

  public static final String[] ALL_METHODS = {
    "/api/v1/public/**",
    "/api/v1/auth/**",
    "/api/v1/test/**",
    "/configuration/ui",
    "/configuration/security",
    "/webjars/**",
    "/swagger-ui/**",
    "/swagger-ui.html",
    "/swagger-resources",
    "/swagger-resources/**",
    "/v2/api-docs",
    "/v3/api-docs",
    "/v3/api-docs/**",
  };
}
