package com.client.controllers.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpSession;

@Controller()
@RequestMapping("/admin")
public class AdminController {

    private String userUrl = "http://localhost:6789/api/users/";

    @Autowired
    private RestTemplate restTemplate;

    private boolean isAdminLoggedIn(HttpSession session) {
        return session.getAttribute("Administrator") != null;
    }

    private String authenticate(HttpSession session, String redirect) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:" + redirect;
        }
        return null;
    }

    @GetMapping()
    public String index(Model model, HttpSession session) {
        String redirect = authenticate(session, "/auth/adminlogin");
        if (redirect != null) {
            return redirect;
        }
        return "admin/index";
    }

    @GetMapping("/account")
    public String getAccounts(Model model, HttpSession session) {
        String redirect = authenticate(session, "/auth/adminlogin");
        if (redirect != null) {
            return redirect;
        }

        ResponseEntity<List<Object[]>> response = restTemplate.exchange(userUrl + "/listuser", HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Object[]>>() {
                });
        List<Object[]> userList = response.getBody();

        model.addAttribute("userList", userList);

        return "admin/account/index";
    }

}
