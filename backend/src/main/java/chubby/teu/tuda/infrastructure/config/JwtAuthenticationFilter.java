package chubby.teu.tuda.infrastructure.config;

import chubby.teu.tuda.core.User;
import chubby.teu.tuda.feature.auth.component.JwtTokenProvider;
import chubby.teu.tuda.feature.auth.repository.LoginRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component // Rất quan trọng!
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    @Autowired
    private LoginRepository loginRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Bỏ qua /api/login, không cần kiểm tra token
        String path = request.getServletPath();
        if ("/api/login".equals(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 1. Lấy token từ header "Authorization"
            String token = getTokenFromRequest(request);

            // 2. Kiểm tra xem token có hợp lệ không
            if (token != null && jwtTokenProvider.validateToken(token)) {
                // 3. Lấy username từ token
                String username = jwtTokenProvider.getUsernameFromJWT(token);

                // 4. Lấy user từ database để có role thực
                Optional<User> userOpt = loginRepository.findByUsernameOrEmailOrPhone(username, username, username);
                
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    String role = user.getRole() != null ? user.getRole().name() : "USER";
                    
                    // 5. Tạo authentication với role thực
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority(role))
                    );

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 6. ĐẶT NGƯỜI DÙNG NÀY VÀO CONTEXT
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.info("[DEBUG] Đã xác thực thành công cho user: " + username + " với quyền: " + role);
                }
            }
        } catch (Exception e) {
            // Lỗi (token hỏng, hết hạn...) thì cứ bỏ qua,
            // người dùng sẽ bị coi là "chưa đăng nhập"
            logger.error("Không thể xác thực người dùng: ", e);
        }

        filterChain.doFilter(request, response); // Cho request đi tiếp
    }

    // Hàm trợ giúp để lấy token từ "Bearer <token>"
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Bỏ 7 ký tự "Bearer "
        }
        return null;
    }
}