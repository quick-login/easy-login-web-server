package kr.co.easylogin.easyloginwebserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kr.co.easylogin.easyloginwebserver.auth.LoginHistoryRepository;
import kr.co.easylogin.easyloginwebserver.common.security.filter.AuthFilter;
import kr.co.easylogin.easyloginwebserver.common.security.filter.ExceptionFilter;
import kr.co.easylogin.easyloginwebserver.common.security.filter.LoginFilter;
import kr.co.easylogin.easyloginwebserver.common.security.filter.LogoutFilter;
import kr.co.easylogin.easyloginwebserver.common.security.filter.RefreshFilter;
import kr.co.easylogin.easyloginwebserver.common.security.handler.RefreshSuccessHandler;
import kr.co.easylogin.easyloginwebserver.common.security.jwt.JwtConfig;
import kr.co.easylogin.easyloginwebserver.common.security.provider.AccessTokenAuthenticationProvider;
import kr.co.easylogin.easyloginwebserver.common.security.provider.RefreshTokenAuthenticationProvider;
import kr.co.easylogin.easyloginwebserver.common.security.provider.UsernamePasswordAuthenticationProvider;
import kr.co.easylogin.easyloginwebserver.common.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    public static final String SIGNUP_URL = "/api/v1/member/signup";
    public static final String LOGIN_URL = "/api/v1/member/login";
    public static final String LOGOUT_URL = "/api/v1/member/logout";
    public static final String REFRESH_URL = "/api/v1/member/refresh";

    private static final List<String> ALLOWED_ORIGINS = List.of(
//        "http://localhost:3000",
        "*"
                                                               );

    private static final List<String> ALLOWED_METHODS = List.of(
        HttpMethod.GET.name(),
        HttpMethod.POST.name(),
        HttpMethod.PUT.name(),
        HttpMethod.PATCH.name(),
        HttpMethod.DELETE.name()
                                                               );

    private final AccessTokenAuthenticationProvider accessTokenAuthProvider;
    private final RefreshTokenAuthenticationProvider refreshTokenAuthProvider;
    private final UsernamePasswordAuthenticationProvider usernamePasswordAuthProvider;

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(refreshTokenAuthProvider, accessTokenAuthProvider, usernamePasswordAuthProvider);
    }

    @Bean
    public RefreshFilter refreshFilter(RefreshSuccessHandler refreshSuccessHandler) {
        return new RefreshFilter(refreshSuccessHandler, authenticationManager());
    }

    @Bean
    public AuthFilter authFilter() {
        return new AuthFilter(authenticationManager());
    }

    @Bean
    public LoginFilter loginFilter(ObjectMapper objectMapper, AuthenticationSuccessHandler loginSuccessHandler,
                                   LoginHistoryRepository loginHistoryRepository) {
        LoginFilter filter = new LoginFilter(objectMapper, loginSuccessHandler, loginHistoryRepository);
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    @Bean
    public LogoutFilter logoutFilter(RedisUtil redisUtil) {
        return new LogoutFilter(redisUtil);
    }

    @Bean
    public ExceptionFilter exceptionFilter(ObjectMapper objectMapper) {
        return new ExceptionFilter(objectMapper);
    }

//    @Bean
//    public AuthFilter jwtAuthFilter() {
//        return new AuthFilter(authenticationManager());
//    }

    @Bean
    public SecurityFilterChain filterChain(
        HttpSecurity http,
        LoginFilter loginFilter,
        LogoutFilter logoutFilter,
        AuthFilter authFilter,
        RefreshFilter refreshFilter,
        ExceptionFilter exceptionFilter
                                          ) throws Exception {

        // CSRF 비활성화
        http.csrf(AbstractHttpConfigurer::disable);

        // CORS 설정
        http.cors(getCorsConfigurerCustomizer());

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Filter 순서 설정
        http.addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(logoutFilter, LoginFilter.class);
        http.addFilterBefore(authFilter, LogoutFilter.class);
        http.addFilterBefore(refreshFilter, AuthFilter.class);
        http.addFilterBefore(exceptionFilter, RefreshFilter.class);

        // 요청 URL 접근 설정
        settingRequestAuthorization(http);

        return http.build();
    }

    /**
     * CORS 설정
     */
    private Customizer<CorsConfigurer<HttpSecurity>> getCorsConfigurerCustomizer() {
        return corsConfigurer -> corsConfigurer.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(ALLOWED_ORIGINS);
            config.setAllowedMethods(ALLOWED_METHODS);
            config.setAllowedHeaders(List.of("*")); // 모든 요청 헤더 허용
//            config.setAllowedHeaders(List.of("")); // preflight 요청에 대한 응답 헤더 허용
            config.setExposedHeaders(List.of(JwtConfig.REFRESH_TOKEN_HEADER)); // 브라우저가 접근할 수 있는 응답 헤더 허용
            return config;
        });
    }

    /**
     * 요청 URL 접근 설정
     */
    private void settingRequestAuthorization(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
            auth ->
                auth
                    // 정적 파일
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                    // 어드민
                    .requestMatchers("/admin/**").hasAnyRole("MANAGER", "ADMIN")
                    // 인증
                    .requestMatchers(SIGNUP_URL, REFRESH_URL, "/api/v1/member/duplicate", "/api/v1/member/email-verification",
                                     "/api/v1/member/email-validation").permitAll()
                    // 그 외
                    .anyRequest().authenticated()
                                  );
    }
}
