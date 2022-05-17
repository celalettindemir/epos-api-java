package com.hineks.epos.repositories;


import com.hineks.epos.entities.TicketItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TicketItemsRepository extends GenericRepository<TicketItem> {

    @Query("SELECT t FROM ticket_items t WHERE t.ticket.id = :ticketId")
    List<TicketItem> findByTicketId(@Param("ticketId") UUID ticketId);
}
