package com.ikosmov.hw;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class StartComponent {
    @RequestMapping("/")
    public void index(HttpServletResponse response) throws IOException {
        response.sendRedirect("index.html");
    }
}
