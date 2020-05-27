package edu.fzu.qujing.service;

import org.springframework.stereotype.Service;

@Service
public interface PayService {
    /**
     * @param totalAmount 付款金额
     * @param body        学号
     * @return 支付宝支付界面
     */
    public String aliPay(String totalAmount, String body);


    /**
     * 对外转账
     *
     * @param no         订单号
     * @param amount     金额
     * @param aliAccount 支付宝账号
     * @param studentId  学号
     * @return 提示信息
     */
    public boolean driverCash(String no, String amount, String aliAccount, String studentId);


}
