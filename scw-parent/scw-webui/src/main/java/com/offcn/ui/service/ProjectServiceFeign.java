package com.offcn.ui.service;

import com.offcn.common.response.AppResponse;
import com.offcn.ui.service.impl.ProjectServiceFeignImpl;
import com.offcn.ui.vo.resp.ProjectVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "SCW-PROJECT", fallback = ProjectServiceFeignImpl.class)
public interface ProjectServiceFeign {

    @GetMapping("/findAllProject")
    public AppResponse<List<ProjectVo>> findAllProject();
}
