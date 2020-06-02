package com.zcj.wxpro.repository;

import com.zcj.wxpro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IUserRepository extends JpaRepository<User,Integer> {
    public List<User> findByOpenId(String openId);
    public List<User> findByName(String name);
    public List<User> findByUserId(String userId);
}
