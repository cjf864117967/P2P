package cn.itcast.action.creditor;

import cn.itcast.action.common.BaseAction;
import cn.itcast.domain.creditor.CreditorModel;
import cn.itcast.domain.creditor.CreditorSumModel;
import cn.itcast.service.creditor.ICreditorService;
import cn.itcast.utils.*;
import cn.itcast.utils.excel.DataFormatUtilInterface;
import cn.itcast.utils.excel.ExcelDataFormatException;
import cn.itcast.utils.excel.MatchupData;
import cn.itcast.utils.excel.SimpleExcelUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.IOUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.xml.crypto.Data;
import java.io.*;
import java.util.*;

@Namespace("/creditor")
@Controller
@Scope("prototype")
public class CreditorAction extends BaseAction {
    @Autowired
    ICreditorService iCreditorService;
    @Action("download")
    public void download(){

        //1.将资源读取

        //1.1.得到文件位置
        String path = this.getRequest().getSession().getServletContext().getRealPath("/WEB-INF/excelTemplate/ClaimsBatchImportTemplate.xlsx");


        //设置下载时两个响应头
        String mimeType = this.getRequest().getSession().getServletContext().getMimeType("ClaimsBatchImportTemplate.xlsx");
        this.getResponse().setHeader("content-type",mimeType);
        this.getResponse().setHeader("content-disposition","attachment;filename="+(new Date().toLocaleString()+".xlsx"));
        FileInputStream fis =  null;
        try {
            fis = new FileInputStream(path);
            OutputStream os = this.getResponse().getOutputStream();
            IOUtils.copy(fis,os);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally{
            if(fis != null){
                try {
                    fis.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    private File file; // 上传文件
    private String fileFileName; // 上传文件的名称

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileFileName() {
        return fileFileName;
    }

    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
    }


    @Action("upload")
    public void upload() {
        // 1.完成文件上传操作
        // 上传文件保存到WEB-INF/upload下
        String path = this.getRequest().getSession().getServletContext().getRealPath("/WEB-INF/upload");
        File file = new File(path);
        file.mkdir();
        String destName = new Date().getTime() + fileFileName;
        File destFile = new File(path, destName); // 目标文件
        InputStream is = null;
        try {
            FileUtil.copy(this.file, destFile);// 文件上传操作
            // 2.将文件中内容读取到封装成List<CreditorModel>
            is = new FileInputStream(path + "/" + destName);
            SimpleExcelUtil<CreditorModel> seu = new SimpleExcelUtil<CreditorModel>();

            List<CreditorModel> cms = seu.getDataFromExcle(is, "", 1, new MatchupData<CreditorModel>() {
                @Override
                public <T> T macthData(List<Object> data, int indexOfRow, DataFormatUtilInterface formatUtil) {
                    CreditorModel creditor = new CreditorModel();
                    if (data.get(0) != null) {
                        creditor.setContractNo(data.get(0).toString()); // 债权合同编号
                    } else {
                        throw new ExcelDataFormatException("{" + 0 + "}");
                    }
                    creditor.setDebtorsName(data.get(1).toString().replaceAll("\\s{1,}", " "));// 债务人名称
                    // 身份证号
                    if (data.get(2) != null) {
                        String data2 = data.get(2).toString().replaceAll("\\s{1,}", " ");
                        String[] art = data2.split(" ");
                        for (int i = 0; i < art.length; i++) {
                            String str = art[i];
                            if (!RegValidationUtil.validateIdCard(str)) {
                                throw new ExcelDataFormatException("{" + 2 + "}");
                            }
                        }
                        creditor.setDebtorsId(data2);// 债务人身份证编号
                    } else {
                        throw new ExcelDataFormatException("{" + 2 + "}");
                    }
                    creditor.setLoanPurpose(data.get(3).toString()); // 借款用途
                    creditor.setLoanType(data.get(4).toString());// 借款类型
                    creditor.setLoanPeriod(formatUtil.formatToInt(data.get(5), 5)); // 原始期限月
                    creditor.setLoanStartDate(formatUtil.format(data.get(6), 6));// 原始借款开始日期
                    creditor.setLoanEndDate(formatUtil.format(data.get(7), 7));// 原始贷款到期日期
                    // 还款方式
                    if (ConstantUtil.EqualInstallmentsOfPrincipalAndInterest.equals(data.get(8))) {// 等额本息
                        creditor.setRepaymentStyle(11601);
                    } else if (ConstantUtil.MonthlyInterestAndPrincipalMaturity.equals(data.get(8))) {// 按月付息到月还本
                        creditor.setRepaymentStyle(11602);
                    } else if (ConstantUtil.ExpirationTimeRepayment.equals(data.get(8))) {// 到期一次性还款
                        creditor.setRepaymentStyle(11603);
                    } else {
                        throw new ExcelDataFormatException("在单元格{" + 8 + "}类型不存在");
                    }
                    creditor.setRepaymenDate(data.get(9).toString());// 每月还款日
                    creditor.setRepaymenMoney(formatUtil.formatToDouble(data.get(10), 10));// 月还款金额
                    creditor.setDebtMoney(formatUtil.formatToDouble(data.get(11), 11));// 债权金额
                    creditor.setDebtMonthRate(formatUtil.formatToDouble(data.get(12), 12));// 债权月利率
                    creditor.setDebtTransferredMoney(formatUtil.formatToDouble(data.get(13), 13));// 债权转入金额
                    creditor.setDebtTransferredPeriod(formatUtil.formatToInt(data.get(14), 14));// 债权转入期限
                    creditor.setDebtRansferOutDate(formatUtil.format(data.get(15), 15));// 债权转出日期
                    creditor.setCreditor(data.get(16).toString());// 债权人

                    // 债权转入日期 原始开始日期+期限
                    Date startDate = formatUtil.format(data.get(6), 6); // 原始开始日期
                    int add = formatUtil.formatToInt(data.get(14), 14);// 期限
                    Calendar c = Calendar.getInstance();
                    c.setTime(startDate);
                    c.add(Calendar.MONTH, add);
                    creditor.setDebtTransferredDate(c.getTime());

                    Date da = new Date();
                    creditor.setDebtNo("ZQNO" + RandomNumberUtil.randomNumber(da));// 债权编号
                    creditor.setMatchedMoney(Double.valueOf("0"));// 已匹配金额
                    creditor.setDebtStatus(ClaimsType.UNCHECKDE); // 债权状态
                    creditor.setMatchedStatus(ClaimsType.UNMATCH);// 债权匹配状态
                    creditor.setBorrowerId(1); // 借款人id
                    creditor.setDebtType(FrontStatusConstants.NULL_SELECT_OUTACCOUNT); // 标的类型
                    creditor.setAvailablePeriod(creditor.getDebtTransferredPeriod());// 可用期限
                    creditor.setAvailableMoney(creditor.getDebtTransferredMoney());// 可用金额
                    return (T) creditor;

                }
            });
            // 3.调用service完成批量导入操作
            iCreditorService.addMultiple(cms);
            this.getResponse().getWriter().write(Response.build().setStatus("1").toJSON());
            return;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            this.getResponse().getWriter().write(Response.build().setStatus("0").toJSON());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Action("getCreditorlist")
    public void getCreditorlist(){
        // 1.获取请求参数
        String dDebtNo = this.getRequest().getParameter("dContractNo"); // 债权编号
        String dContractNo = this.getRequest().getParameter("dContractNo");// 合同编号
        String dDebtTransferredDateStart = this.getRequest().getParameter("dDebtTransferredDateStart");// 债权转入日期起始
        String dDebtTransferredDateEnd = this.getRequest().getParameter("dDebtTransferredDateEnd");// 债权转入日期结束
        String dDebtStatus = this.getRequest().getParameter("dDebtStatus"); // 债权状态
        String dMatchedStatus = this.getRequest().getParameter("dMatchedStatus"); // 匹配状态
        // 2.处理分页数据
        String offsetnum = this.getRequest().getParameter("offsetnum");// 当前页码
//        if (StringUtils.isEmpty(offsetnum)) {
//            try {
//                this.getResponse().getWriter().write(Response.build().setStatus("0").toJSON());
//                return;
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
        int perPage = 5;// 每页显示条数
        int start = (Integer.parseInt(offsetnum) - 1) * perPage;

// 3.封装查询数据到Map
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(dDebtNo)) {
            map.put("dDebtNo", dDebtNo);
        }
        if (StringUtils.isNotEmpty(dContractNo)) {

            map.put("dContractNo", dContractNo);
        }
        if (StringUtils.isNotEmpty(dDebtTransferredDateStart)) {
            map.put("dDebtTransferredDateStart", dDebtTransferredDateStart);
        }
        if (StringUtils.isNotEmpty(dDebtTransferredDateEnd)) {
            map.put("dDebtTransferredDateEnd",dDebtTransferredDateEnd);
        }
        if (StringUtils.isNotEmpty(dDebtStatus)) {
            map.put("dDebtStatus", Integer.parseInt(dDebtStatus));
        }
        if (StringUtils.isNotEmpty(dMatchedStatus)) {
            map.put("dMatchedStatus", Integer.parseInt(dMatchedStatus));
        }

        map.put("perPage", perPage);
        map.put("start", start);
// 4.调用service获取债权列表数据，并将查询数据封装处理，后续响应到浏览器端
        List<CreditorModel> cms = iCreditorService.getCreditorList(map);
        for (CreditorModel cm : cms) {
            // 匹配状态 int 部分匹配11401, 完全匹配11402, 未匹配11403, 正在匹配11404
            if (cm.getMatchedStatus() == 11401) {
                cm.setMatchedStatusDesc("部分匹配");
            } else if (cm.getMatchedStatus() == 11402) {
                cm.setMatchedStatusDesc("完全匹配");
            } else if (cm.getMatchedStatus() == 11403) {
                cm.setMatchedStatusDesc("未匹配");
            } else if (cm.getMatchedStatus() == 11404) {
                cm.setMatchedStatusDesc("正在匹配");
            }

            if (cm.getDebtStatus() == 11301) {
                cm.setDebtStatusDesc("未审核");
            } else if (cm.getDebtStatus() == 11302) {
                cm.setDebtStatusDesc("已审核");
            }
        }
// 5.调用service获取债权统计数据，并将查询数据封装处理，后续响应到浏览器端
        List<Object[]> list = iCreditorService.getCreditorSum(map);

        List<CreditorSumModel> csms = new ArrayList<CreditorSumModel>();

        for (Object[] obj : list) {
            CreditorSumModel csm = new CreditorSumModel();
            if (obj[0] != null) {
                csm.setdIdCount(Integer.parseInt(obj[0].toString()));
            } else {
                csm.setdIdCount(0);
            }
            if (obj[1] != null) {
                csm.setdDebtMoneySum(Double.parseDouble(obj[1].toString()));
            } else {
                csm.setdDebtMoneySum(0.0);
            }
            if (obj[2] != null) {
                csm.setdAvailableMoneySum(Double.parseDouble(obj[2].toString()));
            } else {
                csm.setdAvailableMoneySum(0.0);
            }
            csms.add(csm);
        }

// 6.封装列表数据，封装统计数据，响应到浏览器端
        Map<String, Object> resultmap = new HashMap<String, Object>();
        resultmap.put("date", cms);
        resultmap.put("datasum", csms);
        try {
            this.getResponse().getWriter().write(Response.build().setStatus("1").setData(resultmap).setTotal("2").setTotalPages("2").toJSON());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Action("checkCreditor")
    public void checkCreditor(){
        //得到请求参数
        String ids = this.getRequest().getParameter("ids");
        String[] id = ids.split(",");
        //调用service完成审核
        iCreditorService.checkCreditor(id);
        //响应状态
        try {
            this.getResponse().getWriter().write(Response.build().setStatus("1").toJSON());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}