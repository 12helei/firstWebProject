package com.hl.servlet;

import com.hl.entity.vo.wechat.message.TextMessage;
import com.hl.service.wechatService;
import com.hl.until.CheckUntil;
import com.hl.until.WechatMessageUtil;
import org.apache.log4j.Logger;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.Map;


/**
 * @Author: hlei
 * @Description:
 * @Date: create in 2021/6/17 9:08
 * @Vsersion: 1.0
 */
public class WeixinServlet extends javax.servlet.http.HttpServlet {
    private static Logger log = Logger.getLogger(WeixinServlet.class);
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        System.out.println("进入doPost方法");
        PrintWriter out=response.getWriter();
        String responseMessage = processRequest(request);
        out.print(responseMessage);
        out.flush();

        System.out.println("进入doPost方法结束");
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        System.out.println("进入doget方法");
        String   signature=request.getParameter("signature");
        String   timestamp=request.getParameter("timestamp");
        String   nonce=request.getParameter("nonce");
        String   echostr=request.getParameter("echostr");
        PrintWriter out=response.getWriter();
        try {
            if (CheckUntil.checkSignature(signature,timestamp,nonce)){
                //如果校验成功将得到的随机字符串
                out.print(echostr);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


    }
    //處理消息
    public String processRequest(HttpServletRequest request) {
        Map<String, String> map = WechatMessageUtil.xmlToMap(request);
        log.info(map);
        // 发送方帐号（一个OpenID）
        String fromUserName = map.get("FromUserName");
        // 开发者微信号
        String toUserName = map.get("ToUserName");
        // 消息类型
        String msgType = map.get("MsgType");
        // 默认回复一个"success"
        String responseMessage = "success";
        // 对消息进行处理
        if (WechatMessageUtil.MESSAGE_TEXT.equals(msgType)) {// 文本消息
            TextMessage textMessage = new TextMessage();
            textMessage.setMsgType(WechatMessageUtil.MESSAGE_TEXT);
            textMessage.setToUserName(fromUserName);
            textMessage.setFromUserName(toUserName);
            textMessage.setCreateTime(System.currentTimeMillis());
            textMessage.setContent("wo接收到你的信息了");
            responseMessage = WechatMessageUtil.textMessageToXml(textMessage);
        }
        log.info(responseMessage);
        return responseMessage;

    }

}
