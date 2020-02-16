package cn.itcast.service.user.impl;

import cn.itcast.dao.IProductRateDAO;
import cn.itcast.dao.UserAccoutModelDao;
import cn.itcast.domain.product.ProductEarningRate;
import cn.itcast.domain.user.UserAccountModel;
import cn.itcast.service.user.UserAccountModelService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.commons.httpclient.HttpException;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserAccountModelServiceimpl implements UserAccountModelService {
    @Autowired
    UserAccoutModelDao userAccoutModelDao;

    @Autowired
    IProductRateDAO iProductRateDAO;

    @Override
    public void add(int id) {

        UserAccountModel uam = new UserAccountModel();
        uam.setUserId(id);
        userAccoutModelDao.save(uam);
    }

    @Override
    public UserAccountModel findByUserId(int userId) {
        return userAccoutModelDao.findByUserId(userId);
    }

    @Override
    public void charge(int userId, Double chargeMoney, int type) throws HttpException, IOException {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();

        HttpPost post = new HttpPost("http://localhost:8080/payment/rest/sample/bankCard/" + userId + "/" + type);

        RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(100000).setConnectTimeout(100000)
                .setSocketTimeout(100000).build();

        CloseableHttpResponse response = null;
        try {
            List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
            formparams.add(new BasicNameValuePair("chargeMoney", String.valueOf(chargeMoney)));
            post.setEntity(new UrlEncodedFormEntity(formparams, "UTF-8"));
            post.setConfig(config);

            response = closeableHttpClient.execute(post);

            HttpEntity entity = response.getEntity();

            String content = EntityUtils.toString(entity);
            JSONObject JSONParam = JSON.parseObject(content);
            String data = JSONParam.getString("Result");
            String status = JSON.parseObject(data).getString("state");
            if ("1".equalsIgnoreCase(status)) {
                userAccoutModelDao.updateAccountByUserId(chargeMoney, userId);
            } else {
                try {
                    throw new Exception();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (ClientProtocolException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            closeableHttpClient.close();
            response.close();
        }
    }

    @Override
    public ProductEarningRate selectYearInterest(int pid, int mounth) {
        return iProductRateDAO.getEarningRateByProductIdAndMonth(pid, mounth);
    }

    // 根据用户id获取用户帐户信息
    @Override
    public UserAccountModel getAccountByUserId(int userId) {
        return userAccoutModelDao.getUserAccountByUserId(userId);
    }


}
