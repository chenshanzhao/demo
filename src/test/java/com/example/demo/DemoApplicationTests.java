package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");

        ListIterator iterator = list.listIterator();

        while (iterator.hasNext()){
            String str = (String)iterator.next();
            System.out.println(str);
        }
    }

}
