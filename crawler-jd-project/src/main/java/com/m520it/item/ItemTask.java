package com.m520it.item;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.m520it.dao.ItemDao;
import com.m520it.domain.Item;
import com.m520it.service.ItemService;
import com.m520it.util.HttpUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
public class ItemTask {

    @Autowired
    private HttpUtils httpUtils;

    @Autowired
    private ItemService service;

    @Autowired
    private ItemDao itemDao;

    //开启定时任务,每过100秒就开始执行任务
    @Scheduled(fixedDelay = 100*1000)
    public void itemTask() throws JsonProcessingException {
        String url="https://search.jd.com/Search?keyword=手机&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&wq=手机&cid2=653&cid3=655&s=56&click=0&page=";
        for (int i = 1; i < 10; i=i+2) {
            String content = httpUtils.getContent(url + i);
            //解析返回的网页信息(也就是html的信息)
            parse(content);
        }
    }

    //解析页面,获取商品的信息,并进行存储
    private void parse(String content) throws JsonProcessingException {
        Document document = Jsoup.parse(content);
        Elements elements = document.select("#J_goodsList>ul>li");
        for (Element element : elements) {
            Item item=new Item();
            //获取spu的值
            String spu = element.attr("data-spu");
            //设置sku的值
            item.setItemSpu(Long.parseLong(spu));
            //获取sku的值
            Elements elements1 = element.select("li[class=ps-item]");
            for (Element element1 : elements1) {
                Element element2 = element1.select("a>img").first();
                String sku = element2.attr("data-sku");
                //判断数据是否存在
                item.setItemSku(Long.parseLong(sku));
                List<Item> list = service.findAll(item);
                if (list.size()>0){
                    continue;
                }
                //获取图片的地址
                String imageAddr = element2.attr("data-lazy-img");
                //拼接地址值
                imageAddr="http:"+imageAddr;
                String image = httpUtils.getImage(imageAddr);
                //把图片的名称设置进数据库
                item.setItemPic(image);
                //解析标题,先拼接标题
                String titleUrl="https://item.jd.com/"+Long.parseLong(sku)+".html";
                String titleContent = httpUtils.getContent(titleUrl);
                //设置网页的地址
                item.setItemUrl(titleUrl);
                //解析标题的信息
                Document titleDocument = Jsoup.parse(titleContent);
                String titleName = titleDocument.select("div[class=sku-name]").first().text();
                item.setItemTitle(titleName);

                //获取商品的价格
                //拼接商品的价格网页的地址
                String priceUrl="https://p.3.cn/prices/mgets?skuIds=J_"+Long.parseLong(sku);
                //得到价格的信息
                String priceMessage = httpUtils.getContent(priceUrl);
                System.out.println(priceMessage);
                ObjectMapper objectMapper=new ObjectMapper();
                try {
                    double p = objectMapper.readTree(priceMessage).get(0).get("p").asDouble();
                    item.setItemPrice((long) p);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                item.setItemCreatTime(new Date());
                item.setItemUpdateTime(item.getItemCreatTime());
                service.save(item);

            }
        }
    }
}
