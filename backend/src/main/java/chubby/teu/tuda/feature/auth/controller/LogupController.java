package chubby.teu.tuda.feature.auth.controller;

import chubby.teu.tuda.feature.auth.dto.logup.LogupRequest;
import chubby.teu.tuda.feature.auth.dto.logup.LogupResponse;
import chubby.teu.tuda.feature.auth.service.LogupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LogupController {

    @Autowired
    private LogupService logupService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody LogupRequest req) {
        String message = logupService.signup(req);
        boolean success = message.equals("Đăng ký thành công!");
        return ResponseEntity.ok(new LogupResponse(success, message));
    }
}
