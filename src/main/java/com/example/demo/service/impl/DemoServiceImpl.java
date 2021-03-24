package com.example.demo.service.impl;

import com.example.demo.pojo.Mail;
import com.example.demo.service.DemoService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.Future;

@Component
public class DemoServiceImpl implements DemoService {

    @Override
    @Async
    public Future<String> doamin() {
        System.out.println("------------------oh no");
        Future<String> returnmsg;
        try {
            Thread.sleep(1000*10);

            System.out.println("线程名字"+Thread.currentThread().getName());
            returnmsg=new AsyncResult<String>("sucess");//没异常就返回这个
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            returnmsg=new AsyncResult<String>("异常为"+e.getMessage());
        }
        System.out.println("------------------oh yes");
        return returnmsg;
    }

    @Async
    public void sendSMS() throws InterruptedException{
        System.out.println("调用短信验证服务方法");
        Long startTime = System.currentTimeMillis();
        Thread.sleep(5000);
        Long endTime=System.currentTimeMillis();
        System.out.println("短信业务执行消耗完成时间:\t"+(endTime-startTime));
    }
    @Async
    public Future<Integer> processA() throws InterruptedException {
        System.out.println("开始分析并统计业务A数据......");
        Long startTime = System.currentTimeMillis();
        Thread.sleep(4000);
        int count = 123456;
        Long endTime = System.currentTimeMillis();
        System.out.println("业务A数据统计消耗时间:\t" + (endTime - startTime));
        return new AsyncResult<>(count);
    }
    @Async
    public Future<Integer> processB() throws InterruptedException {
        System.out.println("开始分析并统计业务B数据......");
        Long startTime = System.currentTimeMillis();
        Thread.sleep(5000);
        int count = 456789;
        Long endTime = System.currentTimeMillis();
        System.out.println("业务B数据统计消耗时间:\t" + (endTime - startTime));
        return new AsyncResult<>(count);
    }

    @Async
    public void doNoReturn(){
        try {
            // 这个方法执行需要三秒
            Thread.sleep(3000);
            System.out.println("方法执行结束" + new Date());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Async
    public Future<String> doReturn(int i){
        try {
            // 这个方法需要调用500毫秒
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 消息汇总
        return new AsyncResult<>(String.format("这个是第{%s}个异步调用的证书", i));
    }
}
