package chubby.teu.tuda.infrastructure.config;

import chubby.teu.tuda.feature.auth.component.JwtTokenProvider;
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

@Component // Rất quan trọng!
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

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
                // 3. Lấy username từ token (đây chính là "admin@tudabrand.com")
                String username = jwtTokenProvider.getUsernameFromJWT(token);

                // 4. Tạo một đối tượng xác thực
                // Ta nói với Spring: "Người này là 'username', không cần pass, không có quyền (Role)"
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username, // Đây chính là "Principal" mà bạn cần
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")) // Danh sách quyền (Role), để trống
                );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 5. ĐẶT NGƯỜI DÙNG NÀY VÀO CONTEXT
                // Đây là bước quan trọng nhất
                // Spring Security sẽ thấy cái này và biết là bạn đã đăng nhập
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.info("[DEBUG] Đã xác thực thành công cho user: " + username + " với quyền: " + authentication.getAuthorities());
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