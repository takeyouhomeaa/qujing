package edu.fzu.qujing.util;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.JsonObject;

import java.util.UUID;

public class PhoneUtil {

    final private static String ACCESS_KEY_ID = "LTAI4G3reCHQUAiHY374JLZ8";
    final private static String ACCESS_SECRET = "M5zXe6pwcqy0YebMNVZYBcWMlzUxc7";
    final private static String SIGN_NAME = "取经web应用";

    final public static String TEMPLATE_CODE = "SMS_191210506";

    private static DefaultProfile profile;
    private static IAcsClient client;

    static {
        profile = DefaultProfile.getProfile("cn-hangzhou", ACCESS_KEY_ID, ACCESS_SECRET);
        client = new DefaultAcsClient(profile);
    }

    public static boolean send(String phone,String code) {
        CommonRequest request = new CommonRequest();

        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");

        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", SIGN_NAME);
        request.putQueryParameter("TemplateCode", TEMPLATE_CODE);
        JsonObject object = new JsonObject();
        object.addProperty("code",code);
        request.putQueryParameter("TemplateParam", object.toString());
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());

            return response.getHttpResponse().isSuccess();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getCode(){
        return String.valueOf(Math.abs(UUID.randomUUID().toString().hashCode())).substring(0, 5);
    }

    public static void main(String[] args) {

    }
}
