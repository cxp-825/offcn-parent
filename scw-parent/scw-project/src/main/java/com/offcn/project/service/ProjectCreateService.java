package com.offcn.project.service;

import com.offcn.common.enums.ProjectStatusEnum;
import com.offcn.project.vo.req.ProjectRedisStorageVo;

public interface ProjectCreateService {

    //第一步 初始化项目
    public String initCreateProject(Integer memberId);  //通过用户发起项目

    //保存项目数据
    public void saveProjectInfo(ProjectStatusEnum status, ProjectRedisStorageVo storageVo);
}
