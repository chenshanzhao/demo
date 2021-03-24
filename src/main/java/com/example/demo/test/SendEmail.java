package com.example.demo.test;

import com.example.demo.pojo.Mail;
import com.example.demo.utils.MailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendEmail {
    private static final Logger LOGGER= LoggerFactory.getLogger(SendEmail.class);
    public static void main(String[] args) {
        Mail mail = new Mail();
        try {
//            String sql2 = "SELECT State FROM tbl_groupInfo  WHERE GroupInfoID = (SELECT MIN(GroupInfoID) FROM tbl_groupInfo WHERE groupName = '"+group+"' )";
//            String state = util.judgeState(sql2);
//
//            String sql3 = "SELECT MIN(GroupInfoID) GroupInfoID FROM tbl_groupInfo WHERE groupName ='"+group+"'";
//
//            String GroupInfoID = util.queryGroupInfoID(sql3);
//            String sql = "INSERT INTO tbl_content (beginUserID,userID,content,Title,State,context_type,groupName,GroupInfoID) " +
//                    " VALUES ('"+beginUserID+"','"+userID+"','"+context+"','"+title+"','"+state+"','"+type+"','"+group+"','"+GroupInfoID+"')";
//
//
//            flag = util.Insert(sql);

            mail.setHost("smtp.wondersgroup.com"); // 设置邮件服务器,如果不用163的,自己找找看相关的
            mail.setSender("chenshanzhao@wondersgroup.com");// 发件人
            mail.setReceiver("137088132@qq.com"); // 接收人
//            mail.setReceiver(userID); // 接收人
            mail.setUsername("chenshanzhao@wondersgroup.com"); // 登录账号
            mail.setPassword("1qaz@WSX"); // 发件人邮箱的登录密码
            mail.setSubject("测试title");
            mail.setMessage("测试content");
            new MailUtil().send(mail);
//            return flag;
        } catch (Exception e) {
            LOGGER.error("发送失败");
        }
    }
}
