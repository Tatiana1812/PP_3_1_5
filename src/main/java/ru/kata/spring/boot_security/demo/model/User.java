package ru.kata.spring.boot_security.demo.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.kata.spring.boot_security.demo.configs.WebSecurityConfig;

import javax.persistence.*;
import java.util.*;


@Entity
@Table(name = "user")
public class User implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "age")
    private int age;
    @Column(name = "password")
    private String password;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public User() {
    }

    public User(String name, String lastName, int age) {
        this.name = name;
        this.lastName = lastName;
        this.age = age;
    }

    public User(String name, String lastName, int age, String password) {
        this(name, lastName, age);
        this.setPassword(password);
    }

    public User(String name, String lastName, int age, String password, Set<Role> roles) {
        this(name, lastName, age, password);
        this.roles = roles;
    }

    public User(Long id, String name, String lastName, int age, String password, Set<Role> roles) {
        this(name, lastName, age, password, roles);
        this.setId(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null) {
            this.name = name;
        }
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (lastName != null) {
            this.lastName = lastName;
        }
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age != 0) {
            this.age = age;
        }
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    public void setPassword(String password) {
        if (password != null) {
            if (!Objects.equals(password, this.password)) {
                this.password = WebSecurityConfig.passwordEncoder().encode(password);
            }
        }
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        if (roles != null) {
            this.roles = roles;
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public boolean isAdmin() {
        return hasRole(new Role("ROLE_ADMIN"));
    }

    public boolean hasRole(Role role) {
        for (Role userRole : roles) {
            if (userRole.getRoleName().equals(role.getRoleName())) {
                return true;
            }
        }
        return false;
    }
    public String rolesToString(){
        StringBuilder stringBuilder = new StringBuilder();
        for (Role userRole : roles) {
            stringBuilder.append(userRole.getRoleName());
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getName().equals(user.getName()) && getPassword().equals(user.getPassword());
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", roles=" + rolesToString() +
                ", password='" + password + '\'' +
                '}';
    }
}
