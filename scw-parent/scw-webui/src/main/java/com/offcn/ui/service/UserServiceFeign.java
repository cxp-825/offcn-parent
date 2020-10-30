package com.offcn.ui.service;

import com.offcn.common.response.AppResponse;
import com.offcn.ui.service.impl.UserServiceFeignImpl;
import com.offcn.ui.vo.resp.UserRespVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "SCW-USER", fallback = UserServiceFeignImpl.class)
public interface UserServiceFeign {

    @PostMapping("/user/login")
    public AppResponse<UserRespVo> login(@RequestParam("loginacct") String username,@RequestParam("password") String password);
}
