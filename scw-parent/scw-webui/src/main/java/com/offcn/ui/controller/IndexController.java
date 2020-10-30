package com.offcn.ui.controller;

import com.alibaba.fastjson.JSON;
import com.offcn.common.response.AppResponse;
import com.offcn.ui.service.ProjectServiceFeign;
import com.offcn.ui.vo.resp.ProjectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
public class IndexController {

    @Resource
    private ProjectServiceFeign projectService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping("/")
    public String showIndex(Model model) {

        //先从Redis中读，如果没有从数据库中读
        String projectStrs = redisTemplate.opsForValue().get("projects");
        List<ProjectVo> projectVos = JSON.parseArray(projectStrs,ProjectVo.class);
        if (projectVos == null) {
            //从数据库中获取
            AppResponse<List<ProjectVo>> appResponse = projectService.findAllProject();
            List<ProjectVo> data = appResponse.getData();
            String dataStr = JSON.toJSONString(data);
            //将获取的数据存储到Redis中
            redisTemplate.opsForValue().set("projects",dataStr,5, TimeUnit.HOURS);
            model.addAttribute("projectList",data);
        } else {
            model.addAttribute("projectList",projectVos);
        }
        return "index";
    }
}
