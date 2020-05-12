package edu.fzu.qujing.util;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import edu.fzu.qujing.config.alipay.AlipayConfig;


import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AliPayUtil {

    public static AlipayClient getAlipayClient() {
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.APP_ID,
                AlipayConfig.APP_PRIVATE_KEY, "json", AlipayConfig.CHARSET,
                AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.sign_type);

        return alipayClient;
    }

    /**
     * 转码
     *
     * @param param 字符串
     * @return 转换成UTF-8的字符串
     */
    public static String transcoding(String param) {
        try {
            return new String(param.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static boolean rsaCheckV1(HttpServletRequest request) {
        // https://docs.open.alipay.com/54/106370
        // 获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        System.out.println(params);
        try {
            boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY,
                    AlipayConfig.CHARSET, AlipayConfig.sign_type);
            return signVerified;
        } catch (AlipayApiException e) {
            return false;
        }
    }

}
