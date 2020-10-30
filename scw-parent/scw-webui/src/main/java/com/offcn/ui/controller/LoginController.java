package com.offcn.ui.controller;

import com.offcn.common.response.AppResponse;
import com.offcn.ui.service.UserServiceFeign;
import com.offcn.ui.vo.resp.UserRespVo;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Resource
    private UserServiceFeign userService;

    @RequestMapping("/doLogin")
    public String doLogin(String loginacct, String password, HttpSession session) {
        AppResponse<UserRespVo> appResponse = userService.login(loginacct,password);
        //获取返回数据
        UserRespVo vo = appResponse.getData();
        if (vo == null) {
            return "redirect:/login.html";
        }
        //session中存值
        session.setAttribute("sessionMember",vo);
        String url = (String) session.getAttribute("url");
        if (StringUtils.isEmpty(url)) {
            //跳转首页
            return "redirect:/";
        } else {
            return "redirect:/" + url;
        }
    }
}
