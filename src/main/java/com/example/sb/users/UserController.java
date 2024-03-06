package com.example.sb.users;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired private UserService userSvc;
	
	@GetMapping("/list/{page}")
	public String list(@PathVariable int page, Model model) {	//list를 던져주기위해 model사용
		List<User> list = userSvc.getUserList(page);
		model.addAttribute("userList", list);
		return "user/list";
	}
	
	@GetMapping("/register")
	public String register() {
		return "user/register";
	}
	
	@PostMapping("/register")
	public String registerProc(String uid, String pwd, String pwd2, String uname, String email) {
		if(userSvc.getUserByUid(uid) == null && pwd.equals(pwd2)) {
			String hashedPwd = BCrypt.hashpw(pwd, BCrypt.gensalt());
			User user = new User(uid, hashedPwd, uname, email);
			userSvc.registerUser(user);
		}
		return "redirect:/user/list/1";
	}
	
	@GetMapping("login")
	public String login() {
		return "user/login";
	}
	
	@PostMapping("login")
	public String loginProc(String uid, String pwd, Model model, HttpSession session) {
		String msg, url;
		int result = userSvc.login(uid, pwd);
		if (result == userSvc.CORRECT_LOGIN) {
			User user = userSvc.getUserByUid(uid);
			session.setAttribute("sessUid", uid);
			session.setAttribute("sessUname", user.getUname());
			msg = user.getUname() + "님 환영합니다.";
			url = "list/1";
		} else if (result == userSvc.WRONG_PASSWORD) {
			msg = "패스워드가 틀립니다.";
			url = "login";
		} else {
			msg = "아이디 입력이 잘못되었습니다.";
			url = "login";
		}
		model.addAttribute("msg", msg);
		model.addAttribute("url", url);
		return "user/alertMsg";
	}
	
	@GetMapping("logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/user/list/1";
	}
	
	@GetMapping("update/{uid}")
	public String update(@PathVariable String uid, Model model) {
		User user = userSvc.getUserByUid(uid);
		model.addAttribute("user", user);
		return "user/update";
	}
	
	@PostMapping("update")
	public String updateProc(String uid, String pwd, String pwd2, String uname, String email, String hashedPwd, Model model) {
		if (pwd != null && pwd.equals(pwd2))
			hashedPwd = BCrypt.hashpw(pwd, BCrypt.gensalt());
		User user = new User(uid, hashedPwd, uname, email);
		userSvc.updateUser(user);
		return "redirect:/user/list/1";
	}
	
	@GetMapping("delete/{uid}")
	public String delete(@PathVariable String uid, HttpSession session) {
		userSvc.deleteUser(uid);
//		String sessUid = (String) session.getAttribute("sessUid");
//		if (!sessUid.equals("admin"))
//			session.invalidate();
		return "redirect:/user/list/1";
	}
}
