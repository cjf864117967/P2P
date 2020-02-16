package cn.itcast.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class HttpClientUtil {
    private static Properties config_pro = new Properties();

    private static String propertyFileName = "config/config.properties";

    /**
     * 静态读入属性文件到Properties p变量中
     */
    static {
        // loger.debug("初始化配置文件" + propertyFileName + "到 Constants....");
        InputStream in = null;
        try {
            in = ConfigurableConstants.class.getClassLoader().getResourceAsStream(propertyFileName);
            if (in != null) {
                config_pro.load(in);
            }
        } catch (IOException e) {

        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static String visitWebService(Map<String, Object> map, String server_url) {
        // httpClient操作
        // 1。得到一个httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 2.得到一个请求方式对象
        HttpPost post = new HttpPost(config_pro.getProperty(server_url));

        // 3.配置对象
        RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(10000).setConnectTimeout(100000)
                .setSocketTimeout(10000).build();

        // 3.封装请求参数
        List params = new ArrayList();
        for (String key : map.keySet()) {
            params.add(new BasicNameValuePair(key, map.get(key).toString()));
        }
        try {
            post.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
            post.setConfig(config);
            // 4.得到一个响应对象
            CloseableHttpResponse response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();
            String str = EntityUtils.toString(entity);
            System.out.println("客户端得到的响应数据 :" + str);
            response.close(); // 关闭
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
