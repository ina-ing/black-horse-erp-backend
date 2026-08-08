package com.inaing.blackhorse_erp.module.retailer.domain;

import java.time.LocalDate;

import org.hibernate.annotations.SQLRestriction;

import com.inaing.blackhorse_erp.common.domain.BaseEntity;
import com.inaing.blackhorse_erp.common.domain.Province;
import com.inaing.blackhorse_erp.module.employee.domain.Employee;
import com.inaing.blackhorse_erp.module.retailer.domain.enums.BusinessMedium;
import com.inaing.blackhorse_erp.module.retailer.domain.enums.BusinessType;
import com.inaing.blackhorse_erp.module.role.domain.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "retailers", indexes = { @Index(name = "idx_retailer_code", columnList = "code") })
@SQLRestriction("deleted = false")
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Retailer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "store_name", nullable = false, length = 120)
    private String storeName;

    @Column(name = "contact_person", nullable = false)
    private String contactPerson;

    @Column(name = "email", length = 160)
    private String email;

    @Column(name = "phone", nullable = false, unique = true, length = 20)
    private String phone;

    @Column(name = "pan_number", length = 10)
    private String panNumber;

    @Column(name = "store_address", length = 250)
    private String storeAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "province", length = 15)
    private Province province;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assigned_salesman_id", nullable = false)
    private Employee assignedSalesman;

    @Enumerated(EnumType.STRING)
    @Column(name = "business_type", length = 15)
    private BusinessType businessType;

    @Enumerated(EnumType.STRING)
    @Column(name = "business_medium", length = 15)
    private BusinessMedium businessMedium;

    @Column(name = "joined_date", length = 20)
    private LocalDate joinedOn;
}
