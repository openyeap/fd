package ltd.fdsa.demo.controller;
 


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
	@GetMapping("/add")
    public boolean addUser(  String user) {
        return false;
    }
  
}