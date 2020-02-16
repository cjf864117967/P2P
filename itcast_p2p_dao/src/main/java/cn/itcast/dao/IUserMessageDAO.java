package cn.itcast.dao;

import cn.itcast.domain.userMessage.UserMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserMessageDAO extends JpaRepository<UserMessage,Integer> {
}
