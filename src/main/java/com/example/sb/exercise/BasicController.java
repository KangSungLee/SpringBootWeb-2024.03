package com.example.sb.exercise;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/ex")
public class BasicController {
	// log 사용하려고 선언한 코드 org.slf4j
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@GetMapping("/hello")	// localhost:8090/sb/ex/hello
	public String hello() {
		return "exercise/hello";		// hello.html
	}
	
	@ResponseBody			// html 파일을 찾지 말고, 데이터를 직접 전송(생성)
	@GetMapping("/noHtml")	// 에이젝스를 사용할떄 바뀌는 부분만 바꿀 때 사용
	public String noHtml() {
		return "<h1>Hello Spring Boot!!!</h1>";		// 전송 되는 값
	}
	
	@GetMapping("/redirect")
	public String redirect() {
		return "redirect:/ex/hello";	// Redirection redirect:/ex/hello로 이동
	}
	
	@GetMapping("/params")		
	public String params(Model model) {
		model.addAttribute("name", "제임스"); // set 대신 add
		return "exercise/params";
	}
	
	// 파일을 업로드하는 경우 사용
	@GetMapping("/params2")		
	public String params2(Model model, HttpServletRequest req) {		
		String name = req.getParameter("name");	// 파라메터로 값을 받아서
		model.addAttribute("name", name);	// name 값을 html로 전송
		return "exercise/params";
	}
	
	// 자주 사용하는 경우
	@GetMapping("/params3")		// String name 선언만 해주면 나머지는 Spirng이 처리해줌
	public String params3(Model model, String name, int count) {	// int도 자동 변환해줌
		model.addAttribute("name", name + count);	
		return "exercise/params";
	}
	
	@GetMapping("/memberForm")
	public String memberForm() {
		return "exercise/memberForm";
	}
	
	@PostMapping("/memberProc")
	public String memberProc(Member member, Model model) {
		log.info(member.toString());
		model.addAttribute("name", member.getName());	
		return "exercise/params";
	}
	
	@GetMapping("/login")
	public String login() {
		return "exercise/login";
	}
	
	@PostMapping("/login")
	public String loginProc(String uid, String pwd, HttpSession session, Model model) {
		String hashedPwd = "$2a$10$7xpDk1gkUVpvqNNbdgVhH.5VKxiWNLusIn0i6fYeWHwizmOVB4fzG";
		if (uid.equals("james") && BCrypt.checkpw(pwd, hashedPwd)) {
			model.addAttribute("msg", uid + "님이 로그인했습니다.");
			session.setAttribute("sessUid", uid);
			session.setAttribute("sessUname", "제임스");			
			return "exercise/loginResult";
		} else {
			model.addAttribute("msg", "uid, 비밀번호를 확인하세요.");
			return "exercise/loginResult";
		}
	}
	
	@ResponseBody
	@GetMapping(value={"/path/{uid}/{bid}", "/path/{uid}"})
	public String path(@PathVariable String uid, @PathVariable(required = false) Integer bid) {
		bid = (bid == null) ? 0 : bid;
		return "<h1>uid=" + uid + ", bid=" +  bid + "</h1>";
	}
}
