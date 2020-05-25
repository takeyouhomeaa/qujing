package edu.fzu.qujing.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import edu.fzu.qujing.config.alipay.AlipayConfig;
import edu.fzu.qujing.service.PayService;
import edu.fzu.qujing.service.SettleService;
import edu.fzu.qujing.util.AliPayUtil;
import edu.fzu.qujing.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(rollbackFor = Exception.class)
public class AliPayServiceImpl implements PayService {

    @Autowired
    SettleService settleService;

    /**
     * 支付宝支付
     *
     * @param totalAmount 付款金额
     * @param body        商品描述
     * @return
     */
    @Override
    public String AliPay(String totalAmount, String body) {
        //获得初始化的AlipayClient

        AlipayClient alipayClient = AliPayUtil.getAlipayClient();

        //设置请求参数
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setReturnUrl(AlipayConfig.return_url);
        request.setNotifyUrl(AlipayConfig.notify_url);

        AlipayTradePagePayModel model = new AlipayTradePagePayModel();

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = AliPayUtil.transcoding(DateUtil.getDate());
        System.out.println(out_trade_no);
        //付款金额，必填
        String total_amount = AliPayUtil.transcoding(totalAmount);
        System.out.println(total_amount);
        //订单名称，必填

        String subject = AliPayUtil.transcoding("充值");
        //商品描述，可空
        String studentId = AliPayUtil.transcoding(body);

        model.setOutTradeNo(out_trade_no);
        // System.out.println("out_trade_no"+out_trade_no);
        model.setTotalAmount(total_amount);
        // System.out.println("total_amount" +total_amount);
        model.setBody(studentId);
        // System.out.println("body" + studentId）;
        model.setProductCode("FAST_INSTANT_TRADE_PAY");
        model.setSubject(subject);
        request.setBizModel(model);
        ;
        String form = "";
        try {
            AlipayTradePagePayResponse response = alipayClient.pageExecute(request);
            if (response.isSuccess()) {
                form = response.getBody();//调用SDK生成表单
                //System.out.println("调用成功, 网页支付表单:" + response.getBody());
            } else {
                System.out.println("调用失败:" + response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        return form;
    }


    @Override
    public boolean driverCash(String no, String amount, String aliAccount, String studentId) {
        AlipayClient alipayClient = AliPayUtil.getAlipayClient();
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayFundTransToaccountTransferRequest alipayRequest = new AlipayFundTransToaccountTransferRequest();
        alipayRequest.setBizContent("{" + "\"out_biz_no\":\"" + no + "\"," + "\"payee_type\":\"ALIPAY_LOGONID\","
                + "\"payee_account\":\"" + aliAccount + "\"," + "\"amount\":\"" + amount + "\"," + "\"remark\":\""
                + "单笔提现" + "\"" + "}");
        try {
            AlipayFundTransToaccountTransferResponse aliResponse = alipayClient.execute(alipayRequest);
            if (aliResponse.isSuccess()) {
                System.out.println("调用成功");
                settleService.decreasePoints(studentId, Integer.valueOf(amount));
                return true;
            } else {
                System.out.println("调用失败");
                return false;
            }

        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return false;
    }


}
