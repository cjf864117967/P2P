package cn.itcast.service.charge.impl;

import cn.itcast.service.charge.ChargeService;
import cn.itcast.utils.HttpClientUtil;
import org.apache.http.client.HttpClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ChargeServiceimpl implements ChargeService {
    @Override
    public void recharge(String bankCardNum, double money) {
        //访问webservice服务完成银行转账
        //调用httpClient工具来完成访问Mybank资源
        HashMap<String, Object> map = new HashMap<>();
        map.put("bankCardNum",bankCardNum);

        map.put("money",money);

        String result = HttpClientUtil.visitWebService(map, "bank_url");
        System.out.println(result+".....................");
        if(Boolean.parseBoolean(result)){

            //2.根据银行转账结果来完成自己的平台账户信息改变
            //修改userAccount中的total blance
        }else{
            // 金额不足，转账失败
        }
    }
}
