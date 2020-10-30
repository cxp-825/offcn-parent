package com.offcn.ui.service.impl;

import com.offcn.common.response.AppResponse;
import com.offcn.ui.service.ProjectServiceFeign;
import com.offcn.ui.vo.resp.ProjectVo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectServiceFeignImpl implements ProjectServiceFeign {
    @Override
    public AppResponse<List<ProjectVo>> findAllProject() {
        AppResponse<List<ProjectVo>> fail = AppResponse.fail(null);
        fail.setMsg("远程调用失败（首页项目查询）");
        return fail;
    }
}
