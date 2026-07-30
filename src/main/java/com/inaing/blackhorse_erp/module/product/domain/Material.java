package com.inaing.blackhorse_erp.module.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Material {

    @Column(name = "upper_material", nullable = false)
    private String upperMaterial;

    @Column(name = "lining_material", nullable = false)
    private String liningMaterial;

    @Column(name = "sole_material", nullable = false)
    private String soleMaterial;

    @Column(name = "insole_material", nullable = false)
    private String insoleMaterial;
}
