package com.example.demo.controller;

import com.example.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Controller
@EnableAsync
public class DemoController {

    @Autowired
    private DemoService demoService;

    @ResponseBody
    @GetMapping("/demo")
    public String doamin(){
        Future<String> future=demoService.doamin();
        String s = null;
        try {
            s=future.get();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("wait");
        return s;
    }

    @GetMapping(value = {"sendSMS"})
    @ResponseBody
    public String sendSMS() throws InterruptedException {
        Long startTime = System.currentTimeMillis();
        demoService.sendSMS();
        Long endTime = System.currentTimeMillis();
        System.out.println("主业务执行消耗完成时间:\t" + (endTime - startTime));
        return "success";
    }
    @GetMapping(value = {"statics"})
    @ResponseBody
    public String statics() throws InterruptedException, ExecutionException {
        Long startTime = System.currentTimeMillis();
        Future<Integer>futureA=demoService.processA();
        Future<Integer> futureB=demoService.processB();
        int total=futureA.get()+futureB.get();
        System.out.println("异步任务数据统计汇总结果:\t"+total);

        Long endTime = System.currentTimeMillis();
        System.out.println("主业务执行消耗完成时间:\t" + (endTime - startTime));
        return "success";
    }


    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    @ResponseBody
    public String testAsyncNoRetrun(){
        long start = System.currentTimeMillis();
        demoService.doNoReturn();
        return String.format("任务执行成功,耗时{%s}", System.currentTimeMillis() - start);
    }

    @GetMapping("/hi")
    @ResponseBody
    public Map<String, Object> testAsyncReturn() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();

        Map<String, Object> map = new HashMap<>();
        List<Future<String>> futures = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Future<String> future = demoService.doReturn(i);
            futures.add(future);
        }
        List<String> response = new ArrayList<>();
        for (Future future : futures) {
            String string = (String) future.get();
            response.add(string);
        }
        map.put("data", response);
        map.put("消耗时间", String.format("任务执行成功,耗时{%s}毫秒", System.currentTimeMillis() - start));
        return map;
    }
}
