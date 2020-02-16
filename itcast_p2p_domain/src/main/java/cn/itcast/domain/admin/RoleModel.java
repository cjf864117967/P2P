package cn.itcast.domain.admin;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "t_role")
public class RoleModel {
    @Id
    @GeneratedValue
    private Integer id;

    private String rname;


    @ManyToMany
    @JoinTable(name = "admin_role",joinColumns = {@JoinColumn(name = "rid",referencedColumnName = "id")},inverseJoinColumns = {@JoinColumn(name = "aid",referencedColumnName = "id")})
    private Set<AdminModel> admins = new HashSet<>();
    @ManyToMany(mappedBy = "roles")
    private Set<PermissionModel> ps = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public Set<AdminModel> getAdmins() {
        return admins;
    }

    public void setAdmins(Set<AdminModel> admins) {
        this.admins = admins;
    }

    public Set<PermissionModel> getPs() {
        return ps;
    }

    public void setPs(Set<PermissionModel> ps) {
        this.ps = ps;
    }
}
