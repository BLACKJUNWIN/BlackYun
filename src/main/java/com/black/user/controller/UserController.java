package com.black.user.controller;
import com.black.common.pojo.responseCode;
import com.black.common.pojo.responseJson;
import com.black.common.utils.fileUtils;
import com.black.common.utils.tokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import com.black.user.pojo.User;
import com.black.user.service.UserService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;

	@PostMapping("/")
	public responseJson add(@RequestBody User user) {
	userService.save(user);
	return new responseJson(userService.listByPage(new HashMap<>()));
	}

	@DeleteMapping("/{id}")
	public responseJson del(@PathVariable Long id){
		User user = userService.getById(id);
		if(user!=null){
			fileUtils.removeFile(user.getAvatar());
			userService.removeById(id);
			return new responseJson(userService.listByPage(new HashMap<>()));
		}
		return new responseJson(responseCode.NO_USER);
	}

	@PutMapping("/")
	public responseJson upd(@RequestBody User user) {
	userService.updateById(user);
		return new responseJson(userService.listByPage(new HashMap<>()));
	}

	@PatchMapping("/")
	public responseJson select(@RequestBody Map<String,Object> map){
		return new responseJson(userService.listByPage(map));
	}
	@GetMapping("/")
	public responseJson userInfo(HttpServletRequest request){
		return new responseJson(userService.getById(tokenUtils.requestGetId(request)));
	}

	@PostMapping("/login")
	public responseJson login(@RequestBody User user){
		return userService.login(user,true);
	}

}
