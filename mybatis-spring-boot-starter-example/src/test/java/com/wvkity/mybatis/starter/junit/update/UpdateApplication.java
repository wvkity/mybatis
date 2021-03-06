package com.wvkity.mybatis.starter.junit.update;

import com.wvkity.mybatis.core.wrapper.criteria.QueryCriteria;
import com.wvkity.mybatis.core.wrapper.criteria.UpdateCriteria;
import com.wvkity.mybatis.starter.example.entity.Grade;
import com.wvkity.mybatis.starter.example.entity.User;
import com.wvkity.mybatis.starter.example.service.GradeService;
import com.wvkity.mybatis.starter.example.service.UserService;
import com.wvkity.mybatis.starter.junit.RootTestRunner;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.LocalDateTime;

@Log4j2
public class UpdateApplication extends RootTestRunner {

    @Inject
    private UserService userService;

    @Inject
    private GradeService gradeService;

    @Test
    public void updateTest() {
        User user = new User();
        user.setId(1L).setUserName("李四").setPassword("123456c")
                .setState(2).setSex(1).setVersion(1)
                .setCreatedUserId(DEF_SYS_USER_ID).setCreatedUserName(DEF_SYS_USER_NAME)
                .setGmtCreated(LocalDateTime.now()).setDeleted(false);
        int result = userService.update(user);
        log.info("执行结果: {}", result);
    }

    @Test
    public void updateNotWithNullTest() {
        User user = new User();
        user.setId(1L).setUserName("张三")
                .setState(2).setVersion(2);
        int result = userService.updateNotWithNull(user);
        log.info("执行结果: {}", result);
    }

    @Test
    public void updateNotWithLockingTest() {
        User user = new User();
        user.setId(1L).setUserName("李四").setPassword("123456c")
                .setState(2).setSex(1).setVersion(1)
                .setCreatedUserId(DEF_SYS_USER_ID).setCreatedUserName(DEF_SYS_USER_NAME)
                .setGmtCreated(LocalDateTime.now()).setDeleted(false);
        int result = userService.updateNotWithLocking(user);
        log.info("执行结果: {}", result);
    }

    @Test
    public void updateNotWithNullAndLockingTest() {
        User user = new User();
        user.setId(1L).setUserName("张三")
                .setState(1).setVersion(2).setVersion(2);
        int result = userService.updateNotWithNullAndLocking(user);
        log.info("执行结果: {}", result);
    }

    @Test
    public void mixinUpdateNotWithNullTest() {
        User user = new User();
        user.setUserName("张三").setState(1).setVersion(2).setVersion(2);
        QueryCriteria<User> criteria = new QueryCriteria<>(User.class);
        criteria.idEq(1L).eq(User::getSex, 1, User::getVersion, 1);
        int result = userService.updateNotWithNull(user, criteria);
        log.info("执行结果: {}", result);
    }

    @Test
    public void updateByCriteriaTest() {
        UpdateCriteria<User> criteria = new UpdateCriteria<>(User.class);
        criteria.set(User::getSex, 1, User::getPassword, "111111")
                .eq(User::getId, 2, User::getVersion, 1, User::getDeleted, false);
        int result = userService.update(criteria);
        log.info("执行结果: {}", result);
    }

    @Test
    public void updateByCriteriaTest2() {
        UpdateCriteria<Grade> criteria = UpdateCriteria.from(Grade.class);
        criteria.set(Grade::getName, "SY2").eq(Grade::getId, 2L);
        int result = gradeService.update(criteria);
        log.info("执行结果: {}", result);
    }
}
