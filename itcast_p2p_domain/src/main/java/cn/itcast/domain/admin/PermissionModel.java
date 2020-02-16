package cn.itcast.domain.admin;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "t_permission")
public class PermissionModel {
    @Id
    @GeneratedValue
    private Integer id;

    private String pname;

    @ManyToMany
    @JoinTable(name = "role_permission",joinColumns = {@JoinColumn(name = "pid",referencedColumnName = "id")},inverseJoinColumns = {@JoinColumn(name = "rid",referencedColumnName = "id")})
    private Set<RoleModel> roles = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public String getPname() {
        return pname;
    }

    public Set<RoleModel> getRoles() {
        return roles;
    }
}
