package com.example.demo.controller;

import com.example.demo.controller.dto.ExampleDTO;
import com.example.demo.controller.form.ExampleForm;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class ExampleController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/helloWorld")
    public ExampleDTO greeting(@RequestParam(value = "name", defaultValue = "World") String name) {

        System.out.println(LocalDateTime.now());

        ExampleDTO exampleDTO = new ExampleDTO();
        exampleDTO.setName(name);

        return exampleDTO;
    }

    @GetMapping("/echoGet")
    public HashMap<String, String> echo(@RequestParam(value = "echo", defaultValue = "default....") String echo) {

        HashMap<String, String> ret = new HashMap<>();

        ret.put("echo", echo);

        System.out.println(LocalDateTime.now());

        return ret;
    }

    @PostMapping("/echoPost")
    public ExampleDTO echo(@RequestBody ExampleForm form) {

        ExampleDTO exampleDTO = new ExampleDTO();
        exampleDTO.setName(form.getName());

        return exampleDTO;
    }
}