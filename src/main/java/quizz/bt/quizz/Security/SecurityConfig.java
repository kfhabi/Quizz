package quizz.bt.quizz.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Nếu bạn chưa hoàn thiện logic JWT, tạm thời comment 3 dòng này lại để code chạy được đã
    // private final JwtAuthenticationFilter jwtFilter;
    // public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
    //     this.jwtFilter = jwtFilter;
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Tắt bảo mật CSRF để HTML gọi được POST
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Kích hoạt cấu hình CORS bên dưới
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/v1/users/register", // Cho phép Đăng ký
                    "/api/v1/users/login",    // <--- QUAN TRỌNG: THÊM DÒNG NÀY ĐỂ ĐĂNG NHẬP ĐƯỢC
                                                // Cho phép truy cập trang HTML
                    "/css/**", "/js/**", "/images/**" // Cho phép tải file tĩnh
                ).permitAll()
                
                // Các API khác bắt buộc phải có tài khoản mới được gọi
                .anyRequest().authenticated()
            );
            
            // Tạm thời tắt dòng này nếu chưa xử lý xong JWT
            // .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        
        // SỬA LỖI 1: Đổi thành "*" để chấp nhận mọi nguồn (HTML file, Live Server 5500...)
        config.setAllowedOrigins(List.of("*")); 
        
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        
        // Khi dùng "*" thì KHÔNG ĐƯỢC để allowCredentials(true) -> Comment dòng này lại
        // config.setAllowCredentials(true); 

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}