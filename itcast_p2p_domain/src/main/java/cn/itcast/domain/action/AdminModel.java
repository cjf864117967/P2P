package cn.itcast.domain.action;

import javax.persistence.*;

@Entity
@Table(name = "t_admin")
public class AdminModel {
    @Id
    @GeneratedValue
    private Integer id;
//    @Column(name = "t_username",length = 20)
    private String username;
    private String password;
}
