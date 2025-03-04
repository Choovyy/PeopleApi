package com.example.PeopleApi_Hermosisima.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/")
public class IndexController {

    @GetMapping
    public String home(Principal principal, Model model) {
        if (principal != null) {
            model.addAttribute("user", principal.getName());
        }
        return "Homepage";
        
    }
}