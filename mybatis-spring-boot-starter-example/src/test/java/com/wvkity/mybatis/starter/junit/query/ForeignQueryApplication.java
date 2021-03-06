package com.wvkity.mybatis.starter.junit.query;

import com.wvkity.mybatis.core.conditional.Restrictions;
import com.wvkity.mybatis.core.wrapper.criteria.ForeignCriteria;
import com.wvkity.mybatis.core.wrapper.criteria.QueryCriteria;
import com.wvkity.mybatis.core.wrapper.criteria.SubCriteria;
import com.wvkity.mybatis.starter.example.entity.Grade;
import com.wvkity.mybatis.starter.example.entity.Student;
import com.wvkity.mybatis.starter.example.entity.User;
import com.wvkity.mybatis.starter.example.service.StudentService;
import com.wvkity.mybatis.starter.example.vo.StudentVo;
import com.wvkity.mybatis.starter.junit.RootTestRunner;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;

@Log4j2
public class ForeignQueryApplication extends RootTestRunner {

    @Inject
    private StudentService studentService;

    @Test
    public void innerJoinTest() {
        QueryCriteria<Student> criteria = QueryCriteria.from(Student.class);
        ForeignCriteria<Grade> foreign = criteria.as("st_").innerJoin(Grade.class);
        foreign.as("gr_");
        criteria.where(Restrictions.eq(foreign, Grade::getName, "S3"));
        List<StudentVo> result = studentService.list(criteria);
        log.info("执行结果: {}", result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void subInnerJoinTest1() {
        QueryCriteria<Student> criteria = QueryCriteria.from(Student.class);
        // 连表对象
        ForeignCriteria<Grade> foreign = criteria.innerJoin(Grade.class);
        //foreign.select(Grade::getName, "gradeName", Grade::getDeleted, "");
        foreign.fetch().as("gr_");
        // 子查询条件对象
        SubCriteria<User> subCriteria = criteria.sub(User.class, it ->
                it.eq(User::getState, 3L, User::getSex, 1, User::getDeleted, false));
        subCriteria.select(User::getId, User::getUserName).useAs();
        subCriteria.useAs();
        criteria.innerJoin(subCriteria).on(it ->
                it.nqWith("id", criteria, Student::getId));
        criteria.where(Restrictions.eq(foreign, Grade::getName, "S3"));
        List<StudentVo> result = studentService.list(criteria);
        log.info("执行结果: {}", result);
    }

    @Test
    public void subInnerJoinTest2() {
        QueryCriteria<Student> criteria = QueryCriteria.from(Student.class);
        // 连表对象
        ForeignCriteria<Grade> foreign = criteria.innerJoin(Grade.class);
        foreign.fetch();
        //foreign.as("gr_");
        // 子查询条件对象
        SubCriteria<User> subCriteria = criteria.sub(User.class, it ->
                it.eq(User::getState, 3L, User::getSex, 1, User::getDeleted, false));
        //subCriteria.select(User::getId, User::getUserName).useAs();
        subCriteria.useAs();
        criteria.innerJoin(subCriteria).on(it ->
                it.nqWith("id", criteria, Student::getId)).fetch();
        criteria.where(Restrictions.eq(foreign, Grade::getName, "S3"));
        List<StudentVo> result = studentService.list(criteria);
        log.info("执行结果: {}", result);
    }
}
