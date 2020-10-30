package com.offcn.project.controller;

import com.offcn.common.response.AppResponse;
import com.offcn.common.util.OssTemplate;
import com.offcn.project.pojo.*;
import com.offcn.project.service.ProjectInfoService;
import com.offcn.project.vo.resp.ProjectDetailVo;
import com.offcn.project.vo.resp.ProjectVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "众筹项目的模块")
public class ProjectInfoController {

    @Autowired
    private OssTemplate ossTemplate;

    @Autowired
    private ProjectInfoService projectInfoService;

    //注意： 该测试只能通过postman来实现
    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public AppResponse<Map<String, Object>> upload(@RequestParam("file") MultipartFile[] files) throws IOException {

        Map<String, Object> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file != null) {
                String url = ossTemplate.upload(file.getInputStream(), file.getOriginalFilename());
                list.add(url);
            }
        }
        map.put("urls", list);
        return AppResponse.ok(map);
    }

    @ApiOperation("获取项目回报")
    @GetMapping("/returns/{projectId}")
    public AppResponse<List<TReturn>> getReturnList(@PathVariable("projectId") Integer projectId) {
        List<TReturn> returns = projectInfoService.getReturnById(projectId);
        return AppResponse.ok(returns);
    }

    @ApiOperation("获取所有的项目")
    @GetMapping("/findAllProject")
    public AppResponse<List<ProjectVo>> findAllProject() {
        //定义返回值的结果对象
        List<ProjectVo> list = new ArrayList<>();
        //查询所有的项目
        List<TProject> allProject = projectInfoService.findAllProject();
        for (TProject tProject : allProject) {
            //定义一个返回的结果
            ProjectVo projectVo = new ProjectVo();
            //获取所有项目的id 通过id获取对应的头图片；
            Integer projectId = tProject.getId();
            List<TProjectImages> projectImages = projectInfoService.getProjectImages(projectId);
            //从返回的图片中只获取头图片
            for (TProjectImages images : projectImages) {
                if (images.getImgtype() == 0) {  //头图片的类型是0
                    projectVo.setHeaderImage(images.getImgurl());
                }
            }
            //其余属性值
            BeanUtils.copyProperties(tProject, projectVo);
            //此时projectVo中的数据满了
            list.add(projectVo);
        }
        return AppResponse.ok(list);
    }

    @ApiOperation("获取项目信息详情")
    @GetMapping("/findProjectById/{projectId}")
    public AppResponse<ProjectDetailVo> findProjectInfo(@PathVariable("projectId") Integer projectId) {
        //数据库查询的出的结果
        TProject projectInfo = projectInfoService.findProjectInfo(projectId);
        //返回结果
        ProjectDetailVo vo = new ProjectDetailVo();
        //复制projectInfo属性
        BeanUtils.copyProperties(projectInfo, vo);
        //获取projectID对应的图片
        List<TProjectImages> projectImages = projectInfoService.getProjectImages(projectId);
        //创建一个详情图片的集合
        List<String> deImages = new ArrayList<>();
        if (projectImages == null) {
            vo.setDetailsImage(new ArrayList<>());
        } else {
            for (TProjectImages images : projectImages) {
                //类型
                if (images.getImgtype() == 0) { //头图
                    vo.setHeaderImage(images.getImgurl());
                } else { //详情图
                    deImages.add(images.getImgurl());
                }
            }
            vo.setDetailsImage(deImages);  //详情图片
        }
        //项目回报
        List<TReturn> returns = projectInfoService.getReturnById(projectId);
        vo.setProjectReturns(returns);
        return AppResponse.ok(vo);
    }

    @ApiOperation("获取系统所有的项目标签")
    @GetMapping("/findAllTag")
    public AppResponse<List<TTag>> findAllTag() {
        List<TTag> allTag = projectInfoService.findAllTag();
        return AppResponse.ok(allTag);
    }

    @ApiOperation("获取系统所有的项目分类")
    @GetMapping("/findAllType")
    public AppResponse<List<TType>> findAllType() {
        List<TType> allType = projectInfoService.findAllType();
        return AppResponse.ok(allType);
    }

    @ApiOperation("获取回报信息")
    @GetMapping("/findReturnByRid/{returnId}")
    public AppResponse<TReturn> findReturnByRid(@PathVariable("returnId") Integer returnId) {
        TReturn returnById = projectInfoService.findReturnById(returnId);
        return AppResponse.ok(returnById);
    }
}