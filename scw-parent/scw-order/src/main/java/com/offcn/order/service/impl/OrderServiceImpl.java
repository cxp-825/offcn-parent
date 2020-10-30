package com.offcn.order.service.impl;

import com.offcn.common.enums.OrderStatusEnum;
import com.offcn.common.response.AppResponse;
import com.offcn.order.mapper.TOrderMapper;
import com.offcn.order.pojo.TOrder;
import com.offcn.order.service.OrderService;
import com.offcn.order.service.ProjectServiceFeign;
import com.offcn.order.vo.req.OrderInfoSubmitVo;
import com.offcn.order.vo.resp.TReturn;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ProjectServiceFeign projectServiceFeign;


    @Override
    public TOrder saveOrder(OrderInfoSubmitVo vo) {
        //创建返回对象
        TOrder tOrder = new TOrder();
        BeanUtils.copyProperties(vo,tOrder);
        //vo没有数据
        //获取用户的id 登录的时候在Redis中存储， 键是=> 令牌
        String accessToken = vo.getAccessToken();
        String memberId = redisTemplate.opsForValue().get(accessToken);
        tOrder.setMemberid(Integer.parseInt(memberId));
        //订单编号
        String ordernum = UUID.randomUUID().toString().replace("-", "");
        tOrder.setOrdernum(ordernum);
        //状态
        tOrder.setStatus(OrderStatusEnum.UNPAY.getCode() + "");
        AppResponse<List<TReturn>> response = projectServiceFeign.getReturnList(vo.getProjectid());
        List<TReturn> data = response.getData();
        for (TReturn tReturn : data) {
            if (tReturn.getId().equals(vo.getReturnid())) {
                //支持金额 tReturn.getSupportmony();
                //运费 TReturn.getFreight();
                Integer money = vo.getRtncount() * tReturn.getSupportmoney() + tReturn.getFreight();
                tOrder.setMoney(money);
            }
        }
        return tOrder;
    }
}
