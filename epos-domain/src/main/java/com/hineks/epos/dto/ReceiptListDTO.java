package com.hineks.epos.dto;

import java.util.List;

public class ReceiptListDTO {
    public String TicketId;
    public Double TotalPrice;
    public List<OrderItemDTO> orderItemDTOList;
}
