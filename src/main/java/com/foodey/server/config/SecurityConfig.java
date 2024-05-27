package com.foodey.server.config;

import com.foodey.server.auth.jwt.AuthEntryPointJwt;
import com.foodey.server.auth.jwt.LazyJwtAuthTokenFilter;
import com.foodey.server.utils.ApiEndpointSecurityInspector;
import com.webauthn4j.data.AttestationConveyancePreference;
import com.webauthn4j.data.AuthenticatorAttachment;
import com.webauthn4j.data.PublicKeyCredentialParameters;
import com.webauthn4j.data.PublicKeyCredentialType;
import com.webauthn4j.data.ResidentKeyRequirement;
import com.webauthn4j.data.UserVerificationRequirement;
import com.webauthn4j.data.attestation.statement.COSEAlgorithmIdentifier;
import com.webauthn4j.springframework.security.config.configurers.WebAuthnLoginConfigurer;
import com.webauthn4j.springframework.security.options.AssertionOptionsProvider;
import com.webauthn4j.springframework.security.options.AttestationOptionsProvider;
import com.webauthn4j.springframework.security.options.PublicKeyCredentialUserEntityProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
@Profile("!dev")
public class SecurityConfig {

  private final UserDetailsService userDetailsService;
  private final LazyJwtAuthTokenFilter lazyJwtAuthTokenFilter;
  private final AuthEntryPointJwt unauthorizedHandler;
  private final LogoutHandler logoutHandler;
  private final ApiEndpointSecurityInspector apiEndpointSecurityInspector;
  private final AttestationOptionsProvider attestationOptionsProvider;
  private final AssertionOptionsProvider assertionOptionsProvider;
  private final PublicKeyCredentialUserEntityProvider publicKeyCredentialUserEntityProvider;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(List<AuthenticationProvider> providers) {
    return new ProviderManager(providers);
  }

  // @Bean
  // public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
  //     throws Exception {
  //   return authConfig.getAuthenticationManager();
  // }

  @Bean
  public CorsConfigurationSource corsApiConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addAllowedOriginPattern("*");
    configuration.addAllowedHeader("*");
    configuration.addAllowedMethod("*");
    configuration.setAllowCredentials(true);
    configuration.setMaxAge(3600L);
    configuration.setAllowedMethods(
        List.of(
            "HEAD", "GET", "POST", "PUT", "DELETE", "PATCH",
            "OPTIONS")); // <-- methods allowed in CORS policy
    configuration.setAllowedHeaders(
        List.of(
            "Authorization",
            "Cache-Control",
            "Content-Type",
            "Accept",
            "X-Requested-With",
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Headers",
            "Origin")); // <-- headers allowed in CORS policy
    configuration.setExposedHeaders(
        List.of(
            "Authorization",
            "Cache-Control",
            "Content-Type",
            "Accept",
            "X-Requested-With",
            "X-Rate-Limit-Retry-After-Seconds",
            "X-Rate-Limit-Remaining",
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Headers",
            "Origin")); // <-- headers exposed in CORS policy
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  // @Bean
  // public PublicKeyCredentialUserEntityProvider publicKeyCredentialUserEntityProvider() {
  //   // return new PublicKeyCredentialUserEntityProvider() {};
  // }

  @Bean
  public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
    apiEndpointSecurityInspector.getPublicEndpoints().add("/api/v1/auth/**");

    // WebAuthn Login
    http.with(
            WebAuthnLoginConfigurer.webAuthnLogin(),
            (customizer) -> {
              customizer
                  .rpId("foodey.com") // this is the domain name of the foodey web
                  // .loginPage("/api/v1/auth/webauthn/login")
                  // .usernameParameter("username")
                  // .passwordParameter("rawPassword")
                  // .credentialIdParameter("credentialId")
                  // .clientDataJSONParameter("clientDataJSON")
                  // .authenticatorDataParameter("authenticatorData")
                  // .signatureParameter("signature")
                  // .clientExtensionsJSONParameter("clientExtensionsJSON")
                  .loginProcessingUrl("/api/v1/auth/webauthn/login")
                  .attestationOptionsEndpoint()
                  .attestationOptionsProvider(attestationOptionsProvider)
                  .processingUrl("/api/v1/auth/webauthn/attestation/options")
                  .userProvider(publicKeyCredentialUserEntityProvider)
                  .rp()
                  .name("Foodey Corporation")
                  .and()
                  .pubKeyCredParams(
                      new PublicKeyCredentialParameters(
                          PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.RS256),
                      new PublicKeyCredentialParameters(
                          PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.ES256),
                      new PublicKeyCredentialParameters(
                          PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.RS1))
                  .authenticatorSelection()
                  .authenticatorAttachment(AuthenticatorAttachment.CROSS_PLATFORM)
                  .residentKey(ResidentKeyRequirement.PREFERRED)
                  .userVerification(UserVerificationRequirement.PREFERRED)
                  .and()
                  .attestation(AttestationConveyancePreference.NONE)
                  .extensions()
                  .credProps(true)
                  .uvm(true)
                  .and()
                  .assertionOptionsEndpoint()
                  .assertionOptionsProvider(assertionOptionsProvider)
                  .rpId("example.com")
                  .userVerification(UserVerificationRequirement.PREFERRED);
            })

        // headers
        .headers(
            headers -> {

              // 'publickey-credentials-get *' allows getting WebAuthn credentials to all
              // nested browsing contexts (iframes) regardless of their origin.
              headers.permissionsPolicy(config -> config.policy("publickey-credentials-get *"));

              // Disable "X-Frame-Options" to allow cross-origin iframe access
              headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable);
            })
        .cors(cors -> cors.configurationSource(corsApiConfigurationSource()))
        // .csrf((csrf) -> csrf.disable())
        .csrf(
            customizer -> {
              customizer.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
              customizer.ignoringRequestMatchers("/api/v1/auth/webauthn/**");
            })

        // exception handling
        .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))

        // session management
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        // authorize
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        HttpMethod.GET,
                        apiEndpointSecurityInspector.getPublicGetEndpoints().toArray(String[]::new))
                    .permitAll()
                    .requestMatchers(
                        HttpMethod.POST,
                        apiEndpointSecurityInspector
                            .getPublicPostEndpoints()
                            .toArray(String[]::new))
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .authenticationProvider(authenticationProvider())

        // filter
        .addFilterBefore(lazyJwtAuthTokenFilter, UsernamePasswordAuthenticationFilter.class)
        .logout(
            logout ->
                logout
                    .logoutUrl("/api/v1/auth/logout")
                    .addLogoutHandler(logoutHandler)
                    .logoutSuccessHandler(
                        (request, response, authentication) ->
                            SecurityContextHolder.clearContext()));

    return http.build();
  }
}
