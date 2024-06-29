package com.itacwt.phonemarket.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "countries")
@Getter
@Setter
public class Country {
    @Id
    @Column(name = "country_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "prefix")
    private String prefix;
    @Column(name = "name")
    private String name;
    @Column(name = "validation_regex")
    private String validationRegex;
    @Column(name = "tax_rate")
    private BigDecimal taxRate;
}
