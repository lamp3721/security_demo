package org.example.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.TestDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {


    @PostMapping("/test")
    public String test(@Valid @RequestBody TestDto testDto) {
        log.info("testDto: {}", testDto);
        return "success";
    }
}
