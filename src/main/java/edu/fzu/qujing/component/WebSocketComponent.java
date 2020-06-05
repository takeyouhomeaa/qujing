package edu.fzu.qujing.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.java_websocket.server.WebSocketServer;
import edu.fzu.qujing.bean.Message;
import edu.fzu.qujing.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@Component
@ServerEndpoint("/webSocket/{studentId}")
public class WebSocketComponent {
    @Autowired
    //MessageService messageService;
    

    //静态变量，用来记录当前在线连接数
    private static AtomicInteger onlineNum = new AtomicInteger();

    //用来存放每个客户端对应的WebSocketComponent对象
    private static ConcurrentHashMap<String, WebSocketComponent> webSocketMap = new ConcurrentHashMap<>();

    private Session session;

    private String studentId = "";

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public static void sendInfo(String message, @PathParam("studentId") String studentId) throws IOException {
        if (!StringUtils.isEmpty(message) && webSocketMap.containsKey(studentId)) {
            webSocketMap.get(studentId).sendMessage(message);
        } else {
            log.error("用户{},不在线", studentId);
        }
    }

    public static AtomicInteger getOnlineCount() {
        return onlineNum;
    }

    public static synchronized void addOnlineCount() {
        WebSocketComponent.onlineNum.incrementAndGet();
    }

    public static synchronized void subOnlineCount() {
        WebSocketComponent.onlineNum.decrementAndGet();
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("studentId") String studentId) {
        log.info("webSocket连接开启");
        this.session = session;
        this.studentId = studentId;
        if (webSocketMap.containsKey(studentId)) {
            webSocketMap.remove(studentId);
            webSocketMap.put(studentId, this);
        } else {
            webSocketMap.put(studentId, this);
            addOnlineCount();
        }

        log.info("用户连接{} ", studentId);
        log.info("当前在线人数为: {}", getOnlineCount().get());
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            log.error("用户:{},网络异常!!!!!!", studentId);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("用户消息:{} ", studentId);
        log.info("报文{} ", message);
        if (!StringUtils.isEmpty(message)) {
            Message msg = new Message();
            JSONObject jsonObject = JSON.parseObject(message);
            jsonObject.put("fromUserId", this.studentId);

            String toUserId = jsonObject.getString("toUserId");
            if (!StringUtils.isEmpty(toUserId) && webSocketMap.containsKey(studentId)) {
                try {
                    webSocketMap.get(toUserId).sendMessage(jsonObject.toJSONString());
                    this.saveMessage(message, toUserId, studentId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                log.info("请求的studentId:{}不在该服务器上", studentId);
                this.saveMessage(message, toUserId, studentId);
                //messageService.sendMessage(message1);
            }
        }
    }

    @OnError
    public void onError(Throwable error) {
        log.error("用户错误:{},原因:", this.studentId);
        log.error("错误原因{}", error.getMessage());
        error.printStackTrace();
    }

    private synchronized void saveMessage(String content, String receiveId, String publisherId) {
        Message message = new Message();
        message.setContent(content);
        message.setReceiveId(receiveId);
        message.setPublisherId(publisherId);
        message.setTime(new Date());
        //messageService.sendMessage(message);
    }

    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(studentId)) {
            webSocketMap.remove(studentId);
            //从set中删除
            subOnlineCount();
        }
        log.info("用户退出:{}", studentId);
        log.info("当前在线人数为:", getOnlineCount());


    }


}
