package com.spring.site.web.home;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping("/")
    public String home(HttpServletRequest request, HttpServletResponse response) {
        return "home";
    }
    @PostMapping("tokenCheck")
    @ResponseBody
    public String tokenCheck(){
        System.out.println("토큰체크 컨트롤러");
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "home";
    }
}
