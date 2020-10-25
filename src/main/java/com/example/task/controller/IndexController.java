package com.example.task.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping("/")
@ApiIgnore
public class IndexController {

    @GetMapping
    public RedirectView redirectToSwaggerUi() {
        return new RedirectView("/swagger-ui.html");
    }
}
