package com.example.focus.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class ConfigurationSecurity {

    private final UserDetailsService userDetailsService;


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF if not required
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        // Public Endpoints
                        .requestMatchers(
                                "/api/v1/focus/studio/register",
                                "/api/v1/focus/photographer/register",
                                "/api/v1/focus/editor/register",
                                "/api/v1/focus/editor/get-all",
                                "/api/v1/focus/photographer/get-all",
                                "/api/v1/focus/studio/get-all",
                                "/api/v1/focus/media/**",
                                "/api/v1/focus/space/get-specific-space/{id}",
                                "/api/v1/focus/studio/get-studio-by-city/{city}",
                                "/api/v1/focus/space/get-all-spaces",
                                "/api/v1/focus/tool/get-photographer-tools/{photographer_id}"
                        ).permitAll()

                        // Admin Endpoints
                        .requestMatchers(
                                "/api/v1/focus/admin/**",
                                "/api/v1/focus/shift/get-all",
                                "/api/v1/focus/editor/delete-editor/{editorid}",
                                "/api/v1/focus/offer-editing/get-all",
                                "/api/v1/focus/request-editing/get-all",
                                "/api/v1/focus/shift/get-shift/{id}"
                        ).hasAuthority("ADMIN")

                        // Photographer Endpoints
                        .requestMatchers(
                                "/api/v1/focus/offer-editing/get-by-id/{id}",
                                "/api/v1/focus/photographer/update-photographer",
                                "/api/v1/focus/photographer/delete-photographer",
                                "/api/v1/focus/photographer/get-my-rent-tools",
                                "/api/v1/focus/photographer/profile/get-my-profile",
                                "/api/v1/focus/photographer/profile/upload-media",
                                "/api/v1/focus/photographer/profile/update",
                                "/api/v1/focus/request-editing/get-photographer-requests",
                                "/api/v1/focus/tool/add-tool",
                                "/api/v1/focus/tool/get-my-tools",
                                "/api/v1/focus/tool/update-tool/{tool_id}"
                        ).hasAuthority("PHOTOGRAPHER")

                        // Studio Endpoints
                        .requestMatchers(
                                "/api/v1/focus/book-space/get-all",
                                "/api/v1/focus/shift/create-shift",
                                "/api/v1/focus/shift/delete-shift/{id}",
                                "/api/v1/focus/space/create-space",
                                "/api/v1/focus/space/update-my-space",
                                "/api/v1/focus/space/get-my-spaces",
                                "/api/v1/focus/space/get-available-spaces/{studio_id}",
                                "/api/v1/focus/studio/upload-image"
                        ).hasAuthority("STUDIO")

                        // Editor Endpoints
                        .requestMatchers(
                                "/api/v1/focus/editor/update-editor",
                                "/api/v1/focus/editor/profile/get-my-profile",
                                "/api/v1/focus/editor/profile/upload-media",
                                "/api/v1/focus/editor/profile/update",
                                "/api/v1/focus/request-editing/get-editor-requests",
                                "/api/v1/focus/request-editing/reject/{requestId}"
                        ).hasAuthority("EDITOR")

                        // Shared Endpoints
                        .requestMatchers(
                                "/api/v1/focus/request-editing/get-by-id/{id}",
                                "/api/v1/focus/request-editing/get-by-id-active/{id}"
                        ).hasAnyAuthority("EDITOR", "PHOTOGRAPHER")

                        // All Other Endpoints
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults()) // Enable HTTP Basic Authentication
                .logout(logout -> logout
                        .logoutUrl("/api/v1/logout")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                );

        return http.build();
    }
}
