package com.offcn.user.service;

import com.offcn.user.pojo.TMember;
import com.offcn.user.pojo.TMemberAddress;
import io.swagger.models.auth.In;

import java.util.List;

public interface UserService {

    public void registerUser(TMember member);

    public TMember login(String username, String password);

    //根据id获取用户信息
    public TMember findMemberById(Integer id);

    /**
     * 获取用户收货地址
     * @param memberId
     * @return
     */
    public List<TMemberAddress> findAddressList(Integer memberId);
}
