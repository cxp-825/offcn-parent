package com.offcn.project.controller;

import com.alibaba.fastjson.JSON;
import com.offcn.common.enums.ProjectStatusEnum;
import com.offcn.common.response.AppResponse;
import com.offcn.project.pojo.TReturn;
import com.offcn.project.service.ProjectCreateService;
import com.offcn.common.vo.BaseVo;
import com.offcn.project.vo.req.ProjectBaseInfoVo;
import com.offcn.project.vo.req.ProjectRedisStorageVo;
import com.offcn.project.vo.req.ProjectReturnVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "项目添加的基本操作")
public class ProjectCreateController {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ProjectCreateService projectCreateService;

    @ApiOperation("初始化第一步，创建ProjectToken")
    @PostMapping("/init")
    public AppResponse<String> init(BaseVo vo) {
        String accessToken = vo.getAccessToken();
        //通过Redis来获取用户的ID
        String memberId = redisTemplate.opsForValue().get(accessToken);
        if (StringUtils.isEmpty(memberId)) {
            return AppResponse.fail("用户错误");
        }
        String projectToken = projectCreateService.initCreateProject(Integer.parseInt(memberId));
        return AppResponse.ok(projectToken);
    }

    @ApiOperation("初始化第二步，收集页面基本数据")
    @PostMapping("/saveBaseInfo")
    public AppResponse<String> saveBaseInfo(ProjectBaseInfoVo vo) {
        //目的：将用户提交的数据vo 添加到projectRedisStorageVo的对象中
        //1、获取Redis中的projectRedisStorageVo的对象
        String redisProjectStr = redisTemplate.opsForValue().get(vo.getProjectToken());
        System.out.println(redisProjectStr);
        ProjectRedisStorageVo storageVo = JSON.parseObject(redisProjectStr,ProjectRedisStorageVo.class);
        //2、将用户填写的数据添加到storageVo中
        BeanUtils.copyProperties(vo,storageVo);
        //3.将加好的数据的storageVo存入Redis中
        redisProjectStr = JSON.toJSONString(storageVo);
        redisTemplate.opsForValue().set(vo.getProjectToken(),redisProjectStr);
        return AppResponse.ok("基本信息添加成功");

    }

    @ApiOperation("初始化第三步，提交汇报增量保存")
    @PostMapping("/saveReturn")
    public AppResponse<Object> saveReturn(@RequestBody List<ProjectReturnVo> returns) {
        //目的：从Redis中获取数据，加入属性，存入到Redis中
        if (returns != null && returns.size() > 0) {
            ProjectReturnVo returnVo = returns.get(0);
            String projectToken = returnVo.getProjectToken();
            //从Redis中获取对象
            String storageStr = redisTemplate.opsForValue().get(projectToken);
            ProjectRedisStorageVo storageVo = JSON.parseObject(storageStr, ProjectRedisStorageVo.class);
            //添加数据
            List<TReturn> list = new ArrayList<>();
            for (ProjectReturnVo vo : returns) {
                TReturn tReturn = new TReturn();
                BeanUtils.copyProperties(vo,tReturn);
                list.add(tReturn);
            }
            storageVo.setProjectReturns(list);
            //存入Redis中
            redisTemplate.opsForValue().set(projectToken,JSON.toJSONString(storageVo));
            return AppResponse.ok("汇报添加成功");
        } else {
            return AppResponse.fail("数据为空");
        }
    }

    @ApiOperation("初始化第四步，保存完成")
    @PostMapping("/submit")
    public AppResponse<Object> submit(String accessToken, String projectToken, String ops) {
        //根据AccessToken获取用户信息
        String memberId = redisTemplate.opsForValue().get(accessToken);
        if (StringUtils.isEmpty(memberId)) {
            return AppResponse.fail("用户登录过期，请重新登录后重试");
        }
        //获取项目信息
        String storageStr = redisTemplate.opsForValue().get(projectToken);
        ProjectRedisStorageVo storageVo = JSON.parseObject(storageStr, ProjectRedisStorageVo.class);
        //判断用户操作类型是否为空
        if (!StringUtils.isEmpty(ops)) {
            if (ops.equals("1")) {
                projectCreateService.saveProjectInfo(ProjectStatusEnum.SUBMIT_AUTH,storageVo);
            } else if ("0".equals(ops)) {
                projectCreateService.saveProjectInfo(ProjectStatusEnum.DRAFT,storageVo);
            }
            return AppResponse.ok("数据导入成功");
        }
        return AppResponse.fail("数据导入失败");
    }
}
