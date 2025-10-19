package chubby.teu.tuda.feature.auth.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import java.util.Date;

// 1. Bạn phải đánh dấu là @Component hoặc @Service
@Component
public class JwtTokenProvider {

    // 2. Bạn cần một "khóa bí mật"
    private final String JWT_SECRET = "DayLaMotChuoiBiMatAnToanRatDaiChiBaoGomChuVaSoKhongKyTuDacBiet64";

    // 3. Thời gian hết hạn của token (ví dụ: 1 ngày)
    private final long JWT_EXPIRATION = 86400000L;

    // 4. Đây chính là hàm TẠO TOKEN
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

        // Tạo chuỗi token
        return Jwts.builder()
                .setSubject(username) // Gắn username vào token
                .setIssuedAt(now) // Thời gian phát hành
                .setExpiration(expiryDate) // Thời gian hết hạn
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET) // Ký tên bằng thuật toán và khóa bí mật
                .compact();
    }

    // 5. Bạn cũng sẽ cần các hàm khác để giải mã token (dùng trong Security Filter)
    public String getUsernameFromJWT(String token) {
        // ... code giải mã ...
        return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        // ... code kiểm tra token ...
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            // Token không hợp lệ
        }
        return false;
    }
}
