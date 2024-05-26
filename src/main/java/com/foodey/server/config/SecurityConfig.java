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

  @Bean
  public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
    // WebAuthn Login
    http.with(
        WebAuthnLoginConfigurer.webAuthnLogin(),
        (customizer) -> {
          customizer
              .loginPage("/api/v1/auth/webauthn/login")
              .usernameParameter("username")
              .passwordParameter("rawPassword")
              .credentialIdParameter("credentialId")
              .clientDataJSONParameter("clientDataJSON")
              .authenticatorDataParameter("authenticatorData")
              .signatureParameter("signature")
              .clientExtensionsJSONParameter("clientExtensionsJSON")
              .loginProcessingUrl("/login")
              .rpId("example.com")
              .attestationOptionsEndpoint()
              .attestationOptionsProvider(attestationOptionsProvider)
              .processingUrl("/api/v1/auth/webauthn/attestation/options")
              .rp()
              .name("example")
              .and()
              .pubKeyCredParams(
                  new PublicKeyCredentialParameters(
                      PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.ES256),
                  new PublicKeyCredentialParameters(
                      PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.RS1))
              .authenticatorSelection()
              .authenticatorAttachment(AuthenticatorAttachment.CROSS_PLATFORM)
              .residentKey(ResidentKeyRequirement.PREFERRED)
              .userVerification(UserVerificationRequirement.PREFERRED)
              .and()
              .attestation(AttestationConveyancePreference.DIRECT)
              .extensions()
              .credProps(true)
              .uvm(true)
              .and()
              .assertionOptionsEndpoint()
              .assertionOptionsProvider(assertionOptionsProvider)
              .rpId("example.com")
              .userVerification(UserVerificationRequirement.PREFERRED);
        });

    http.headers(
            headers -> {

              // 'publickey-credentials-get *' allows getting WebAuthn credentials to all
              // nested browsing contexts (iframes) regardless of their origin.
              headers.permissionsPolicy(config -> config.policy("publickey-credentials-get *"));

              // Disable "X-Frame-Options" to allow cross-origin iframe access
              headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable);
            })
        .cors(cors -> cors.configurationSource(corsApiConfigurationSource()))
        .csrf(csrf -> csrf.disable())

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
