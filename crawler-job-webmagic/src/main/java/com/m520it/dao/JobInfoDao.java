package com.m520it.dao;

import com.m520it.domain.JobInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JobInfoDao extends JpaRepository<JobInfo,Long>, JpaSpecificationExecutor<JobInfo> {
}
