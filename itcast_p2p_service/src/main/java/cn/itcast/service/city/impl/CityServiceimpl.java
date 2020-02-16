package cn.itcast.service.city.impl;

import cn.itcast.dao.CityDAO;
import cn.itcast.domain.city.City;
import cn.itcast.service.city.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceimpl implements CityService {
    @Autowired
    CityDAO cityDAO;

    @Override
    public List<City> findProvince() {
        return cityDAO.findByParentCityAreaNumIsNull();
    }

    @Override
    public List<City> findByParentCityAreaNum(String cityAreaNum) {
        return cityDAO.findByParentCityAreaNum(cityAreaNum);
    }
}
