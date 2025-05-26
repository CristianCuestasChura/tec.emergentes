package test.sis414.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.sis414.demo.model.User;
import test.sis414.demo.util.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public String login(@RequestBody User userRequest)
    {
        if(userRequest.getUsername().equals("sis414"))
        {
            return jwtUtil.generateToken(userRequest.getUsername());
        }
        else
        {
            return "No exite usuario";
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout()
    {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("work");
    }
}
