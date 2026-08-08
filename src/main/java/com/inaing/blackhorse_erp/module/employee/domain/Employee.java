package com.inaing.blackhorse_erp.module.employee.domain;

import java.time.LocalDate;

import org.hibernate.annotations.SQLRestriction;

import com.inaing.blackhorse_erp.common.domain.BaseEntity;
import com.inaing.blackhorse_erp.module.role.domain.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employees", indexes = {
        @Index(name = "idx_employee_code", columnList = "code") }, uniqueConstraints = @UniqueConstraint(columnNames = "phone"))
@SQLRestriction("deleted = false")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Employee extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "fullname", nullable = false, unique = false, length = 120)
    private String fullname;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "phone", nullable = false, unique = true, length = 20)
    private String phone;

    @Column(name = "email", length = 160)
    private String email;

    @Column(name = "pan_number", length = 10, unique = true)
    private String panNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20)
    private Role role;

    @Column(name = "address", length = 225)
    private String address;

    @Column(name = "joined_date", length = 20)
    private LocalDate joinedOn;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private EmployeeStatus status = EmployeeStatus.ACTIVE;

    public boolean isActive() {
        return status == EmployeeStatus.ACTIVE;
    }

    public static Employee of(
            String fullname,
            String passwordHash,
            String phone,
            String email,
            String panNumber,
            Role role,
            String address,
            LocalDate joinedOn) {
        return Employee.builder()
                .fullname(fullname)
                .passwordHash(passwordHash)
                .phone(phone)
                .email(email)
                .panNumber(panNumber)
                .role(role)
                .address(address)
                .joinedOn(joinedOn)
                .build();
    }
}
