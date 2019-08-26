package com.m520it.task;

import com.m520it.domain.JobInfo;
import com.m520it.service.JobInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
public class SpringDataPipeline implements Pipeline {

    @Autowired
    private JobInfoService service;

    @Override
    public void process(ResultItems resultItems, Task task) {
        //保存的数据封装到了ResultItems中
        //获取封装好的招聘的详情的对象
        JobInfo jobInfo = resultItems.get("jobInfo");
        //2.判断数据是否为空
        if (jobInfo!=null){
            //如果数据不为空,则把数据保存到数据库中
            this.service.save(jobInfo);
        }
    }
}
