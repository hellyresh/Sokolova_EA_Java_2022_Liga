package com.tasktracker;

import com.tasktracker.services.CommandRouter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
@AllArgsConstructor
public class TaskTrackerController {

    @Autowired
    private CommandRouter commandRouter;

    @GetMapping
    public void root(HttpServletResponse response) throws IOException {
        response.sendRedirect("/cli?command=help");
    }

    @GetMapping("/cli")
    public Object processCliCommand(@RequestParam String command) {
        return commandRouter.executeCommand(command);
    }

}
