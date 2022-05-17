package com.hineks.epos.repositories;

import com.hineks.epos.definitions.ServiceResponse;
import com.hineks.epos.entities.Table;
import com.hineks.epos.entities.TicketItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TablesRepository extends GenericRepository<Table> {
    @Query("SELECT t FROM tables t WHERE t.name = :tableName")
    ServiceResponse<Table> findByName(@Param("tableName") String tableName);
}
