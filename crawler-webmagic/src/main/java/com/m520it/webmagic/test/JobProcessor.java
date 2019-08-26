package com.m520it.webmagic.test;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

public class JobProcessor implements PageProcessor {

    //解析页面
    public void process(Page page) {
        //使用css的选择器的方式,对页面信息进行获取
        List<String> list = page.getHtml().css("#navitems>ul>li>a").all();

        //xpath的方式获取页面的数据://标签名[@属性名=属性值]/直接子类/直接子类
        List<String> list1 = page.getHtml().xpath("//div[@id=navitems]/ul/li/a").all();

        //使用正则表达式的方式抽取页面的数据
        List<String> list2 = page.getHtml().css("#navitems a").regex(".*会员.*").all();

        //获取一条数据,默认获取的是第一条数据
        String list3 = page.getHtml().xpath("//div[@id=navitems]/ul/li/a").get();
        String list4 = page.getHtml().xpath("//div[@id=navitems]/ul/li/a").toString();

        //获取链接,根据这个链接作为网址,再次发送请求
        page.addTargetRequests(page.getHtml().css("#navitems>ul>li>a").links().all());
        //根据获取的链接再次解析页面
        List<String> list5 = page.getHtml().css("#shortcut-2014 a").all();

        //把解析的数据放到ResultItem中去
        page.putField("div",list);
        page.putField("div2",list1);
        page.putField("div3",list2);
        page.putField("div4",list3);
        page.putField("div5",list4);
        page.putField("div6",list5);
    }

    private Site site=Site.me()
            .setCharset("utf-8")  //设置编码
            .setTimeOut(10000)    //设置超时的时间
            .setRetrySleepTime(3000)  //设置重试的时间的间隔
            .setSleepTime(3);       //设置重试的次数
    public Site getSite() {
        return site;
    }

    //主函数,执行爬虫
    public static void main(String[] args) {
        //spider将四大组件联系在一起
        Spider.create(new JobProcessor())
                .addUrl("https://www.jd.com")//设置爬取数据的网址
                .addPipeline(new FilePipeline("C:\\Users\\asus\\Desktop\\result\\")) //设置将数据信息输入到文件中
                .thread(5) //用多线程的方式,进行解析,输入数据
                .run();
    }
}
