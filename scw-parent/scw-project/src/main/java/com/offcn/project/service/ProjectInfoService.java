package com.offcn.project.service;

import com.offcn.project.pojo.*;

import java.util.List;

public interface ProjectInfoService {

    //根据项目的ID获取回报信息
    List<TReturn> getReturnById(Integer projectId);

    /**
     * 获取系统中所有项目
     * @return
     */
    List<TProject> findAllProject();

    /**
     * 获取项目图片
     * @param projectId
     * @return
     */
    List<TProjectImages> getProjectImages(Integer projectId);

    /**
     * 获取项目信息
     * @param projectId
     * @return
     */
    TProject findProjectInfo(Integer projectId);

    /**
     * 获得项目标签
     * @return
     */
    List<TTag> findAllTag();

    /**
     * 获取项目分类
     * @return
     */
    List<TType> findAllType();

    /**
     * 获取回报信息
     * @param returnId
     * @return
     */
    TReturn findReturnById (Integer returnId);


}
