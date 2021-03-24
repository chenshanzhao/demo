package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
public interface DemoService {

    Future<String> doamin();

    Future<Integer> processA() throws InterruptedException ;

    Future<Integer> processB() throws InterruptedException ;

    void sendSMS() throws InterruptedException ;

    void doNoReturn();

    public Future<String> doReturn(int i);
}
