package com.offcn.project.service.impl;

import com.offcn.project.mapper.*;
import com.offcn.project.pojo.*;
import com.offcn.project.service.ProjectInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProjectInfoServiceimpl implements ProjectInfoService {

    @Resource
    private TReturnMapper returnMapper;

    @Resource
    private TProjectMapper projectMapper;

    @Resource
    private TProjectImagesMapper projectImagesMapper;

    @Resource
    private TTagMapper tagMapper;

    @Resource
    private TTypeMapper typeMapper;


    /**
     * 根据项目的ID获取回报信息
     * @param projectId
     * @return
     */
    @Override
    public List<TReturn> getReturnById(Integer projectId) {
        TReturnExample example = new TReturnExample();
        TReturnExample.Criteria criteria = example.createCriteria();
        criteria.andProjectidEqualTo(projectId);
        return returnMapper.selectByExample(example);
    }

    @Override
    public List<TProject> findAllProject() {
        return projectMapper.selectByExample(null);
    }

    @Override
    public List<TProjectImages> getProjectImages(Integer projectId) {
        TProjectImagesExample example = new TProjectImagesExample();
        TProjectImagesExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(projectId);
        return projectImagesMapper.selectByExample(example);
    }

    @Override
    public TProject findProjectInfo(Integer projectId) {
        TProject tProject = projectMapper.selectByPrimaryKey(projectId);
        return tProject;
    }

    @Override
    public List<TTag> findAllTag() {
        return tagMapper.selectByExample(null);
    }

    @Override
    public List<TType> findAllType() {
        return typeMapper.selectByExample(null);
    }

    @Override
    public TReturn findReturnById(Integer returnId) {
        return returnMapper.selectByPrimaryKey(returnId);
    }
}
