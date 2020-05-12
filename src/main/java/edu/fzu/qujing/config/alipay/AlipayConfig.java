package edu.fzu.qujing.config.alipay;


import java.io.FileWriter;
import java.io.IOException;

public class AlipayConfig {
    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号,开发时使用沙箱提供的APPID，生产环境改成自己的APPID
    public static String APP_ID = "2016102200738774";
    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCYzfCs5fblHWlYE7RAh1Z/8cXv5Tat71w2x0qg1H5M3H/M8mBiEoeVMTmKOpHJO2bfWVVj0sDaAYiEwZS6DAOvi+pzLJYe0E0M+E6v9Kw48OW6lVOLPiO7ebpqNlnHFgvMs18nelOxqKN0g9bwDSemqRl3JQIeaowwiw1hjYR6wINYo3G1oYIAo3FVzSUfYgBu2DlOzni6OWIUG/tVu4G26IomU6x3rosHilhGoijz+pIDyMAq47occMxvkDJWgJFjyjGWconNcev8B5Wp22nAnwobnv+lUuk32T5AVkzgzE5O0iGbw4MTboh+/H4ckXS/gupj0mX/NNkYZwRWSA3zAgMBAAECggEAFy2d5UwYsFZOxWpvvB4LU+sDoEj/i1C0hyahvlWdNuF3jUnyrs2qvUmzNbF+raySBS0S08qDME5tO931RmdOI9GwZXoM522nUmFT5kLR4O1Bm5TNEGEDoIttaHEIGzfP4GQQ+fcHPPqcmK7q6ozER/Z8g2McuCpnS3G8PODviILvVJappoIYHmP0PW3r54v6tplwL6zm7+zoxqAir/oxszDIsauKFRlaXABoYBz7SFU7fecyY3+JPKuVjH9BNNQaelvUdIDiFbLyN2qcY9ALVLOepbFtSZuBI7+fMfiJOYfQFqMBBylDyZcVCS+F+jhdFINAcfp+RFcRp8O2zNNJeQKBgQD9BeA6SZxp4k2WD/Cc9iRYN0gBLKpT4WEH7phuJpnhcrOCNFtgRCTavgluf8kKilK2Ux7HV6ukWuN/LF9eczWpSqcWmpagSpoOWzBcjSZ12/pgWHT/OBUqs4DGTdxpU/ZMeeVCn8NihoQSP43OYf8Qb93dhmHKEPQpEKuKN+tIJQKBgQCamjLYUM6CfL13UduSCawF8G6/TWb0VbhgPGjdS6RMt51oF1sQDdn/Qlm8GutDg15d3NYnn8td/CRolUr/0YqigSrCLrx0Mi/3HQVgJCNSYi9n1FAdcsHo31VUCKE1AHYQtGyIMjNmjEaCSvRwE5z+cIwSa+azf3wmIgP63Uj2NwKBgQDHTOeQQgdIiA8hErEukduS+QholGwd4jtx6gh7S0COirDG8MLBEywMeQAo39LV0JrSLqrhzjbPISBtPeBpH0q3bKwsa9wVqm2qvT1cyXnwADQ5y7NsjRpvJjFbAl2JQa+SEbn6PqnCY76IOTNTy3ED23nhUkaYyb3o0s6r8twLWQKBgFOvkkQ7gX1qrlhvS+6CI8bXPet5p3gtCm+1Y4IeVuJcJe1ZbyaCiPU+YjqLS7ww2Rzv1FCAk+HdAYxG++D7+RI73vxbthEjtYkLAYToKuKGWWjXqUI3gzVd5t9lCHYbOM2UDIewvNgOqIAQuL4OL9Ye8ANsuuWA5Z/ym7rom30DAoGAap5Yzl3eRiQFp74mRC5qV5TgeMnL1nWaMLzSh0GgHnbeyoARqzeNv/PK1nKUjGwtY+2pGvFZi9zMAiyEac/zG4jKcaPkVaPkoE+EiLjb2QBefhWepON7dhLvTR105lwpUBlKY5QAwyBjZpuobfXDt7QhaljCuLhv68U11sc8qaM=";
    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtdwHH3WlpqyyHgxE9X7QSPwWR1dfKyJcFempW8w/B3bxKVJviiMO4KUYIPw6bcTOyZ1TbZP8a9/B3ierr/TKTNm11pNdT6GbDv0tKiPLaCgOiOi46Nx3GvoF4T+ehQ2ootKRx++v0A1DfBUEXyJrIGNcwyhSIOgLZT5eVvfoV4VOS8XaDfMSQPRXmshyA0jCNcZZ+bElIzGNxONZc4Ak3c2IGAwyV4Z0Od9DF4bGy81QQTHYxlgyfXuvB/rbhil7jPyJUgpoN3gaLA71tLMPNp0PV67mJMrDi2YK2ZHtdchKTgzImwHGLpGEqBMXmE2MFkq7o9ACmrxPhKl9wJczBQIDAQAB";
    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "";
    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问(其实就是支付成功后返回的页面)
    public static String return_url = "http://localhost:8081/qujin/client/pay/return_url";
    // 签名方式
    public static String sign_type = "RSA2";
    // 字符编码格式
    public static String CHARSET = "utf-8";
    // 支付宝网关，这是沙箱的网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";
    // 支付宝网关
    public static String log_path = "D:\\qujin\\client\\log";

    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis() + ".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
