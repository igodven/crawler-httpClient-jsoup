package com.m520it.util;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

@Component
public class HttpUtils {
    //创建一个httpclient的连接池的对象,降低资源的消耗
    private PoolingHttpClientConnectionManager cm;

    public HttpUtils() {
        this.cm=new PoolingHttpClientConnectionManager();
        //设置连接的最大的数量
        this.cm.setMaxTotal(100);
        //配置主机连接的最大的数量
        this.cm.setDefaultMaxPerRoute(10);
    }

    /**
     * 传递一个uri的地址,用来获取网页的信息
     * @param url
     * @return
     */
    public String getContent(String url){
        //1.获取httpClient的对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(this.cm).build();
        //2.创建Httpget的对象,用来获取指定的url的网页的内容
        HttpGet httpGet=new HttpGet(url);
        httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36");
        httpGet.setConfig(getRequestConfig());
        //3.执行获取指定网页地址的内容
        CloseableHttpResponse response=null;
        try {
             response= httpClient.execute(httpGet);
             //4.获取响应体的信息
            //判断请求的状态码是否成功
            if (response.getStatusLine().getStatusCode()==200){
                if (response.getEntity()!=null){
                    String content = EntityUtils.toString(response.getEntity(), "utf-8");
                    return content;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //如果请求不成功则返回空字符串
        return "";
    }

    /**
     * 获取图片的信息
     * @param url
     * @return
     */
    public String getImage(String url){


        //1.获取httpClient的对象
        CloseableHttpClient httpClient = HttpClients.custom().build();
        //2.创建Httpget的对象,用来获取指定的url的网页的内容
        HttpGet httpGet=new HttpGet(url);
        httpGet.setConfig(getRequestConfig());
        //3.执行获取指定网页地址的内容
        CloseableHttpResponse response=null;
        try {
            response= httpClient.execute(httpGet);
            //4.获取响应体的信息
            //判断请求的状态码是否成功
            if (response.getStatusLine().getStatusCode()==200){
                if (response.getEntity()!=null){
                    //获取图片的后缀名
                    String picPostName = url.substring(url.lastIndexOf("."));
                    //给图片取一个名字
                    String picName = UUID.randomUUID().toString()+picPostName;
                    //下载图片
                    OutputStream out=new FileOutputStream(new File("C:\\Users\\asus\\Desktop\\image\\"+picName));
                    response.getEntity().writeTo(out);
                    return picName;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //如果请求不成功则返回空字符串
        return "";
    }

    private RequestConfig getRequestConfig() {
        RequestConfig requestConfig=RequestConfig.custom()
                .setConnectTimeout(1000)       //创建连接的最长的时间
                .setConnectionRequestTimeout(500)//获取请求连接的最长的时间
                .setSocketTimeout(10000) //获取数据的最长的时间
                .build();
        return requestConfig;
    }

}
