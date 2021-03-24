package com.example.demo.test;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.MultiPartEmail;

import java.net.URL;

public class Test {

    public static void main(String[] args) {
        try {
            // 发送人邮箱地址和密码
            String fromSMTP = "smtp.163.com";
            String fromEmail = "chenshanzhao123@163.com";
            String fromPwd = "WDTMGFMREVPRBKEE";
            // 收件人邮箱地址
            String toEmail = "137088132@qq.com";

//            // 创建一个带有图片附件
//            EmailAttachment attachmentImg = new EmailAttachment();
//            attachmentImg.setPath("document/150606.png");
//            attachmentImg.setDisposition(EmailAttachment.ATTACHMENT);
//            attachmentImg.setDescription("Email测试项目结构图");
//            attachmentImg.setName("Email项目");
//
            // 创建一个带有压缩包的附件
//            EmailAttachment attachmentJar = new EmailAttachment();
//            attachmentJar.setPath("document/对接容沂办接口文档(1).docx");
//            attachmentJar.setDisposition(EmailAttachment.ATTACHMENT);
//            attachmentJar.setDescription("Apache Commons Email项目的jar包");
//            attachmentJar.setName("Commons Email");

            // 创建一个网络加载图片附件
            EmailAttachment attachmentUri = new EmailAttachment();
            attachmentUri.setURL(new URL("https://www.hxstrive.com/hxstrivedocs/2015/06/07/1037120419_1.png"));
            attachmentUri.setDisposition(EmailAttachment.ATTACHMENT);
            attachmentUri.setDescription("1037120419_1");
            attachmentUri.setName("1037120419");

            // 创建邮件信息
            MultiPartEmail email = new MultiPartEmail();
            email.setHostName(fromSMTP);
            email.setCharset("UTF-8");
            email.setAuthentication(fromEmail, fromPwd);
            email.setFrom(fromEmail, "lishi");
            email.addTo(toEmail, "zhangsan");
            email.setSubject("通过邮件发送附件测试");
            email.setMsg("这封邮件包含1个附件：一种图片；");

            // 将附件设置到邮件中
//            email.attach(attachmentImg);
//            email.attach(attachmentJar);
            email.attach(attachmentUri);

            // 发送
            email.send();
            System.out.println("发送成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
