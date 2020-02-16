package cn.itcast.action.match;

import cn.itcast.action.common.BaseAction;
import cn.itcast.service.match.IMatchService;
import cn.itcast.utils.Response;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Namespace("/match")
@Controller
@Scope("prototype")
public class MatchAction extends BaseAction {
    @Autowired
    IMatchService iMatchService;


    @Action("startMatchByManually")
    public void startMatchByManually(){
        //调用service开始匹配
        iMatchService.startMatch();
        //响应状态
        try {
            this.getResponse().getWriter().write(Response.build().setStatus("1").toJSON());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
