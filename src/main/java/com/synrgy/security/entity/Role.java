package com.synrgy.security.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity()
@Table(
        name = "oauth_role",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "role_name_and_type",
                        columnNames = {"type", "name"}
                )
        }
)
public class Role implements GrantedAuthority {

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime updatedAt;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String name;

    private String type;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RolePath> rolePaths;

    @JsonIgnore
    @ManyToMany(targetEntity = User.class, mappedBy = "roles",fetch = FetchType.LAZY)
    private List<User> users;


    @Override
    @JsonIgnore
    public String getAuthority() {
        return this.name;
    }

}
