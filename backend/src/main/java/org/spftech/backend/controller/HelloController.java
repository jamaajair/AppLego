package org.spftech.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HelloController {

    @GetMapping("/api/speam")
    public Map<String, String> hello() {
        return Map.of("message", "Hello World, this is our SPEAM Project! This message comes from the backend. :)) !" );
    }

    @GetMapping("/team")
    public Map<String, String> team(){
        return Map.of("team", "Our team is composed of Alex, Jamaa, Nathan, Jérôme and Pierre.");
    }
}
