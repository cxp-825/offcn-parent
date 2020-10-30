package com.offcn.ui.service.impl;

import com.offcn.common.response.AppResponse;
import com.offcn.ui.service.UserServiceFeign;
import com.offcn.ui.vo.resp.UserRespVo;
import org.springframework.stereotype.Component;

@Component
public class UserServiceFeignImpl implements UserServiceFeign {
    @Override
    public AppResponse<UserRespVo> login(String username, String password) {
        AppResponse<UserRespVo> fail = AppResponse.fail(null);
        fail.setMsg("远程调用失败（登录）");
        return fail;
    }
}
