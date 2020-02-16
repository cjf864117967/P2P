package cn.itcast.domain.admin;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "t_admin")
public class AdminModel {
    @Id
    @GeneratedValue
    private Integer Id;
    @Column(name = "t_username",length = 20)
    private String username;
    private String password;

    @ManyToMany(mappedBy = "admins")
    private Set<RoleModel> roles = new HashSet<>();

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<RoleModel> getRoles() {
        return roles;
    }













    public void setRoles(Set<RoleModel> roles) {
        this.roles = roles;
    }
}
