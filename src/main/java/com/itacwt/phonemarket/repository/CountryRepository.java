package com.itacwt.phonemarket.repository;

import com.itacwt.phonemarket.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    Country findByPrefix(String prefix);
}
