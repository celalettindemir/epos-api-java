package com.hineks.epos.repositories;

import com.hineks.epos.entities.Person;
import com.hineks.epos.entities.ProductAddition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductAdditionsRepository extends GenericRepository<ProductAddition> {
    //@Query("SELECT t FROM people t WHERE t.name = :name")
    //Person findByName(@Param("name")String name);
}
