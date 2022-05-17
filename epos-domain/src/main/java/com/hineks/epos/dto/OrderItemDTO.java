package com.hineks.epos.dto;

import java.util.List;
import java.util.UUID;

public class OrderItemDTO {
    public UUID Id;
    public UUID ProductId;
    public int Count;
    public String Description;
    public int CustomerNumber;
    public Double Price;
    public List<OrderItemAdditionDTO> orderItemAdditionDTOList;
}
