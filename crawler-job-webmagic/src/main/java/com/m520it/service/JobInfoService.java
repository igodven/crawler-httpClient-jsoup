package com.m520it.service;

import com.m520it.domain.JobInfo;

import java.util.List;

public interface JobInfoService {

    /**
     * 保存数据到数据库中
     */
    void save(JobInfo jobInfo);

    /**
     * 查询操作,用来判断数据库中是否已经存在该数据
     */
    List<JobInfo> findAll(JobInfo jobInfo);
}
