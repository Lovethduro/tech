package com.web.tech.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Mapping the root URL to redirect to '/landing'
    @GetMapping("/")
    public String redirectToLanding() {
        return "redirect:/landing"; // This will send users to '/landing'
    }

    // Mapping '/landing' to the landing page
    @GetMapping("/landing")
    public String landingPage(Model model) {
        return "pages/landing";  // Refers to 'src/main/resources/templates/pages/landing.html'
    }
}
