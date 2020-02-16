package cn.itcast.service.city;

import cn.itcast.domain.city.City;

import java.util.List;

public interface CityService {
    List<City> findProvince();

    List<City> findByParentCityAreaNum(String cityAreaNum);
}
