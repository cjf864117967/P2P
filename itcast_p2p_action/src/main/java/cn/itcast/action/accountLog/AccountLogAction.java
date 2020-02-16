package cn.itcast.action.accountLog;

import cn.itcast.action.common.BaseAction;
import cn.itcast.domain.productAccount.WaitMatchMoneyModel;
import cn.itcast.service.accountLog.AccountLogService;
import cn.itcast.utils.Response;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Namespace("/accountLog")
@Controller
@Scope("prototype")
public class AccountLogAction extends BaseAction {
    @Autowired
    AccountLogService accountLogService;

    @Action("selectWaitMoney")
    public void selectWaitMoney(){
        this.getResponse().setCharacterEncoding("utf-8");
        // 1.获取请求参数
        String page = this.getRequest().getParameter("page");// 获取当前页

        // userName=&pSerialNo=&endDate=&productName=0&investType=0
        String userName = this.getRequest().getParameter("userName"); // 用户名
        String pSerialNo = this.getRequest().getParameter("pSerialNo");// 投资编号
        String endDate = this.getRequest().getParameter("endDate"); // 截止日期
        String productName = this.getRequest().getParameter("productName");// 产品名称
        String investType = this.getRequest().getParameter("investType");// 资金类型

        // 2.处理请求参数

        // 3.调用service完成操作
        // 3.1 查询所有待匹配资金队列
        List<WaitMatchMoneyModel> waitMatchMoneys = accountLogService.selectWaitMatch();
        // 3.2查询待匹配资金队列统计信息
        WaitMatchMoneyModel waitMatchMoneyCount = accountLogService.selectWaitMatchCount();
        // 4.封装数据，并响应到浏览器端
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("listMatch", waitMatchMoneys);
        returnMap.put("waitMatchCount", waitMatchMoneyCount);
        try {
            this.getResponse().getWriter().write(Response.build().setStatus("1").setData(returnMap).toJSON());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
