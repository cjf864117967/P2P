package cn.itcast.dao;

import cn.itcast.domain.city.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityDAO extends JpaRepository<City,Integer> {
    List<City> findByParentCityAreaNumIsNull();

    List<City> findByParentCityAreaNum(String cityAreaNum);
}
