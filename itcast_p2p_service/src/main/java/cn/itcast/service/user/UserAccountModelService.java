package cn.itcast.service.user;

import cn.itcast.domain.product.ProductEarningRate;
import cn.itcast.domain.user.UserAccountModel;
import org.apache.http.client.ClientProtocolException;

import java.io.IOException;

public interface UserAccountModelService {
    void add(int id);

    UserAccountModel findByUserId(int userId);

    void charge(int userId, Double valueOf, int i) throws IOException;

    ProductEarningRate selectYearInterest(int parseInt, int parseInt1);

    UserAccountModel getAccountByUserId(int userId);
}
