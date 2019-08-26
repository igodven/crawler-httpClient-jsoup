package com.m520it.task;

import com.m520it.domain.JobInfo;
import com.m520it.service.JobInfoService;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

@Component
public class JobProcessor implements PageProcessor {

    @Autowired
    private JobInfoService service;

     String url = "https://search.51job.com/list/000000,000000,0000,01%252C32,9,99,java,2,1.html?lang=c&stype=&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&providesalary=99&lonlat=0%2C0&radius=-1" +
            "&ord_field=0&confirmdate=9&fromType=" +
            "&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=";

    @Override
    public void process(Page page) {

        //拿到工作招聘的信息列表
        List<Selectable> list = page.getHtml().css("#resultList div.el").nodes();
        if (list.size()==0){
            //如果没有url的地址后,就执行解析页面的操作
            //解析页面,获取招聘的详情的信息,保存数据
            saveJobInfo(page);
        }else{
            for (Selectable selectable : list) {
                //获取招聘简历的具体的url的地址
                String jobInfourl = selectable.links().toString();
                //把获取到的url放到队列中
                if (jobInfourl!=null){
                    page.addTargetRequest(jobInfourl);
                }
            }
            //获取下一页的url的地址
            String nextPageUrl = page.getHtml().css("div.p_in li.bk").nodes().get(1).links().toString();
            //把下一页的url的地址放到队列中
            page.addTargetRequest(nextPageUrl);
        }
    }

    /**
     * 解析页面,获取招聘的详情的信息,保存数据
     * @param page
     */
    private void saveJobInfo(Page page) {

        //创建招聘的详情的对象
        JobInfo jobInfo=new JobInfo();

        //解析页面
        Html html = page.getHtml();

        //获取数据,封装到对象中
        jobInfo.setCompanyName(Jsoup.parse(html.css("div.cn p.cname a").nodes().get(0).toString()).text());
        jobInfo.setCompanyAddr(Jsoup.parse(html.css("div.bmsg").nodes().get(1).toString()).text());
        jobInfo.setCompanyInfo(Jsoup.parse(html.css("div.tmsg").toString()).text());
        jobInfo.setJobName(html.css("div.cn h1","text").toString());
        String text = html.css("div.cn p.ltype", "text").toString();
        String[] split = text.split("    ");

        jobInfo.setJobAddr(split[0]);
        String text1 = Jsoup.parse(html.css("div.job_msg").toString()).text();
        jobInfo.setJobInfo(text1);
        String text2 = html.css("div.cn strong", "text").toString();
        String[] split1 = text2.split("-");
        Double salarymin=0.0;
        Double salaryMax=0.0;
        if (split1[1].contains("万")){
            salarymin = Double.parseDouble(split1[0])*10000;
            String maxSub = split1[1].substring(0, split1[1].indexOf("万"));
            salaryMax=Double.parseDouble(maxSub)*10000;

        }else{
            salarymin = Double.parseDouble(split1[0])*1000;
            String maxSub = split1[1].substring(0, split1[1].indexOf("千"));
            salaryMax=Double.parseDouble(maxSub)*1000;
        }
        jobInfo.setSalaryMin(salarymin);
        jobInfo.setSalaryMax(salaryMax);
        jobInfo.setUrl(page.getUrl().toString());
        jobInfo.setTime(split[4].substring(0,split[4].length()-2));

        //把结果保存起来
        page.putField("jobInfo",jobInfo);
    }

    private Site site=Site.me();
    @Override
    public Site getSite() {
      site = this.site.setTimeOut(10000)  //设置超时的时间
                .setCharset("gbk")      //设置编码
                .setSleepTime(3000)       //设置重新连接的时间
                .setRetrySleepTime(3);    //设置重试的次数

        return site;
    }

    @Autowired
    private SpringDataPipeline springDataPipeline;
    /**
     * initialDelay:当任务开启后,等等多久开始执行
     * fixedDelay:每隔多久执行任务
     */
    @Scheduled(initialDelay = 1000, fixedDelay = 100 * 1000)
    public void process() {

        Spider.create(new JobProcessor())
                //设置爬取的网址
                .addUrl(url)
                //设置用哪种方法去重
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000)))
                .thread(10)
                .addPipeline(this.springDataPipeline)  //添加输出的方式
                .run();
    }
}
