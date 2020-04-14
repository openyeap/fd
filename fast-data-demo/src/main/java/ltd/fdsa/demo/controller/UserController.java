package ltd.fdsa.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ltd.fdsa.demo.service.IUserService;

@RestController
@RequestMapping("/user")
public class UserController {
	@GetMapping("/add")
	public boolean addUser(String user) {
		return false;
	}

	@Autowired
	protected IUserService userService;

	@GetMapping("/{userId}")
	public Object getUser(@PathVariable(value = "userId") String userId) {
		return this.userService.selectUserByGroupId(userId);
	}
}