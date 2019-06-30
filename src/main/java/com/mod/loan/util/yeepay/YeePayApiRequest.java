package com.mod.loan.util.yeepay;

import com.alibaba.fastjson.JSONObject;
import com.mod.loan.util.MoneyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @ author liujianjian
 * @ date 2019/6/16 12:58
 */
@Slf4j
@Component
public class YeePayApiRequest {

    //余额查询
    public long queryBalanceFen() {
        return MoneyUtil.yuan2Fen(queryBalance());
    }

    public double queryBalance() {
        try {
            String customerNumber = format(Config.getInstance().getValue("customerNumber"));

            Map<String, Object> params = new HashMap<>();
            params.put("customerNumber", customerNumber);
            String uri = YeepayUtil.getUrl(YeepayUtil.customeramountQuery_URL);

            JSONObject json = YeepayUtil.yeepayYOP(params, uri);
            return json.getDoubleValue("wtjsValidAmount"); //wtjsValidAmount	代付代发可用余额, accountAmount	账户可用余额
        } catch (Exception e) {
            log.error("易宝查询商户余额失败: " + e.getMessage(), e);
        }
        return 0D;
    }

    public static String format(String text) {
        return text == null ? "" : text.trim();
    }


    public static void main(String[] args) throws Exception {
        String customerNumber = format("1000046");

        Map<String, Object> params = new HashMap<>();
        params.put("customerNumber", customerNumber);
        String uri = YeepayUtil.getUrl(YeepayUtil.customeramountQuery_URL);

        JSONObject json = YeepayUtil.yeepayYOP(params, "/rest/v1.0/balance/query_customer_amount");
        System.out.print(Double.valueOf(MoneyUtil.fen2YuanStr(MoneyUtil.yuan2Fen(json.getDoubleValue("wtjsValidAmount"))))); //wtjsValidAmount	代付代发可用余额, accountAmount	账户可用余额
    }

}