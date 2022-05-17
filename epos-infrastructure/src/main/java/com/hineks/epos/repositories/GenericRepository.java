package com.hineks.epos.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.*;
import java.util.function.Predicate;

@NoRepositoryBean
public interface GenericRepository<T> extends CrudRepository<T,UUID> {
    List<T> findAllByIsDeleted(boolean isDeleted);
}
