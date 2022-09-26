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

    /*
    curl --location --request GET 'http://127.0.0.1:8080/helloWorld' | json_pp
    curl --location --request GET 'http://127.0.0.1:8080/helloWorld?name=teste' | json_pp
    * */

    @GetMapping("/helloWorld")
    public ExampleDTO greeting(@RequestParam(value = "name", defaultValue = "World") String name) {

        System.out.println(LocalDateTime.now());

        ExampleDTO exampleDTO = new ExampleDTO();
        exampleDTO.setName(name);

        return exampleDTO;
    }

    /*
    curl --location --request GET 'http://127.0.0.1:8080/echoGet' | json_pp
    curl --location --request GET 'http://127.0.0.1:8080/echoGet' | json_pp
    * */

    @GetMapping("/echoGet")
    public HashMap<String, String> echo(@RequestParam(value = "echo", defaultValue = "default....") String echo) {

        HashMap<String, String> ret = new HashMap<>();

        ret.put("echo", echo);

        System.out.println(LocalDateTime.now());

        return ret;
    }

    /*
    curl --location --request POST 'http://127.0.0.1:8080/echoPost' \
    > --header 'Content-Type: application/json' \
    > --data-raw '{
    >     "name": "John"
    > }' | json_pp
    * */

    @PostMapping("/echoPost")
    public ExampleDTO echo(@RequestBody ExampleForm form) {

        ExampleDTO exampleDTO = new ExampleDTO();
        exampleDTO.setName(form.getName());

        return exampleDTO;
    }
}