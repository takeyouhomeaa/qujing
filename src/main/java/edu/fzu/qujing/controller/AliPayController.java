package edu.fzu.qujing.controller;

import com.alipay.api.AlipayApiException;

import edu.fzu.qujing.bean.Expenses;
import edu.fzu.qujing.bean.Recharge;
import edu.fzu.qujing.bean.Type;
import edu.fzu.qujing.service.PayService;
import edu.fzu.qujing.service.SettleService;
import edu.fzu.qujing.util.AliPayUtil;
import edu.fzu.qujing.util.AuthorityUtil;
import edu.fzu.qujing.util.DateUtil;
import edu.fzu.qujing.util.JwtUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Api(tags = "支付相关操作")
@RestController
@RequestMapping("/pay")
public class AliPayController {

    @Qualifier("aliPayServiceImpl")
    @Autowired
    PayService payService;

    @Autowired
    SettleService settleService;

    @ApiOperation(value = "用户充值接口", notes = "会自动跳转到支付页面")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "支付金额",name = "totalAmount",dataType = "string",required = true)
    })


    @PostMapping(value = "/recharge")
    public String goAlipay(@ApiIgnore @RequestBody Map<String, String> map,
                           @ApiIgnore HttpServletRequest request) {
        String username = JwtUtil.getSubject(request);
        AuthorityUtil.setPrincipal(username);
        String totalAmount = map.get("totalAmount");
        String body = username;
        String form = payService.AliPay(totalAmount, body);
        return form;
    }


    @ApiIgnore
    @RequestMapping("/return_url")
    public String return_url(HttpServletRequest request) throws IOException, AlipayApiException {
        String username = AuthorityUtil.getPrincipal();
        AuthorityUtil.removePrincipal();
        boolean verifyResult = AliPayUtil.rsaCheckV1(request);
        ModelAndView mv = null;
        if (verifyResult) {
            String total_amount = request.getParameter("total_amount");
            Integer amount = Double.valueOf(total_amount).intValue();
            System.out.println(amount);
            settleService.increasePoints(username, amount);
            return "success";
        }
        return "fail";
    }

    @ApiOperation(value = "用户提现接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "金额",name = "amount",dataType = "string",required = true),
            @ApiImplicitParam(value = "支付宝账户",name = "aliAccount",dataType = "string",required = true)
    })

    @ApiResponses({
            @ApiResponse(code = 200,message = "Withdraw success"),
            @ApiResponse(code = 201,message = "没有这个返回值"),
            @ApiResponse(code = 400,message = "Withdraw fail"),
            @ApiResponse(code = 401,message = "没有这个返回值"),
            @ApiResponse(code = 403,message = "没有这个返回值"),
            @ApiResponse(code = 401,message = "没有这个返回值")
    })

    @PutMapping("/withdraw")
    public ResponseEntity<String> withdraw(@ApiIgnore @RequestBody Map<String, String> map,
                                           @ApiIgnore HttpServletRequest request) {
        String username = JwtUtil.getSubject(request);
        String amount = map.get("amount");
        String aliAccount = map.get("aliAccount");
        String tradeNo = DateUtil.getDate();
        boolean driverCash = payService.driverCash(tradeNo, amount, aliAccount, username);
        if (driverCash) {
            return ResponseEntity.ok("Withdraw success");
        }
        return ResponseEntity.badRequest().body("Withdraw fail");
    }

    @ApiOperation(value = "获取用户的消费记录", notes = "URL传递pos,消费类型请使用type查询出全部的类型和状态，然后使用数组去赋值")

    @GetMapping("/listExpenseRecord/{pos}")
    public List<Expenses> listExpenseRecord(@ApiIgnore @PathVariable("pos") Integer pos,
                                            @ApiIgnore HttpServletRequest request) {
        return settleService.listExpensesRecord(JwtUtil.getSubject(request), pos);
    }

    @ApiOperation(value = "获取用户的消费记录", notes = "URL传递pos,消费类型请使用type查询出全部的类型和状态，然后使用数组去赋值")

    @GetMapping("/listRechargeRecord/{pos}")
    public List<Recharge> listRechargeRecord(@ApiIgnore @PathVariable("pos") Integer pos,
                                             @ApiIgnore HttpServletRequest request) {
        return settleService.listRecharheRecord(JwtUtil.getSubject(request), pos);
    }
}
