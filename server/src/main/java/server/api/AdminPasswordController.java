package server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password")
public class AdminPasswordController {
    AdminPasswordService adminPasswordService;
    @Autowired
    public AdminPasswordController(AdminPasswordService adminPasswordService) {
        this.adminPasswordService = adminPasswordService;
    }
    @GetMapping("/{inputPassword}")
    ResponseEntity<Boolean> checkPassword(@PathVariable String inputPassword){
        if(adminPasswordService.passwordChecker(inputPassword)){
            return ResponseEntity.ok(true);
        }
        else{
            return ResponseEntity.ok(false);
        }
    }
}
