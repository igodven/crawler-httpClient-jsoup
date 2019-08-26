package com.m520it.service.impl;

import com.m520it.dao.JobInfoDao;
import com.m520it.domain.JobInfo;
import com.m520it.service.JobInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class JobServiceImpl implements JobInfoService {

    @Autowired
    private JobInfoDao jobInfoDao;

    /**
     * 保存数据之前,应该进行判断
     *     1.招聘工作的信息是否已经存在(根据url和发布的时间判定)
     *     2.如果存在,则对信息进行跟新
     *     3.如果不存在,则保存数据
     *
     * @param jobInfo
     */
    @Override
    @Transactional
    public void save(JobInfo jobInfo) {
        JobInfo jobInfo1=new JobInfo();
        //获取url和发布的时间
        jobInfo1.setUrl(jobInfo.getUrl());
        jobInfo1.setTime(jobInfo.getTime());
        //查询数据是否已经存在
        List<JobInfo> list = this.findAll(jobInfo1);
        if (list.size()==0){
            jobInfoDao.saveAndFlush(jobInfo);
        }

    }

    /**
     * 查询数据库中是否已近有了该数据
     * @param jobInfo
     * @return
     */
    @Override
    public List<JobInfo> findAll(JobInfo jobInfo) {
        //创建查询的条件
        Example<JobInfo> example=Example.of(jobInfo);
        //返回查询的结果
        List<JobInfo> list = jobInfoDao.findAll(example);
        return list;
    }
}
