package com.pragma.usersMicroservice.infrastructure.output.jpa.entity;

import com.pragma.usersMicroservice.domain.util.RoleName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private RoleName name;
    private String description;

}
