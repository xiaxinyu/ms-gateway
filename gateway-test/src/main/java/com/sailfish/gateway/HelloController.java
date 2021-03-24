package com.sailfish.gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author XIAXINYU3
 * @date 2021/3/24
 */
@RestController
public class HelloController {
    @GetMapping(value = "/member")
    public String move(@RequestParam(name = "name") String name) throws InterruptedException {
        return "Hello," + name;
    }
}
