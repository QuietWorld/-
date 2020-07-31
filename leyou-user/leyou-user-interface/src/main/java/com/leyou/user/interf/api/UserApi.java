package com.leyou.user.interf.api;

import com.leyou.user.interf.domain.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserApi {

    @GetMapping("/query")
    User getUser(@RequestParam("username")String username, @RequestParam("password")String password);
}
