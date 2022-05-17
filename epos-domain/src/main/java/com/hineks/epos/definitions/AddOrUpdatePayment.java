package com.hineks.epos.definitions;

import com.hineks.epos.entities.PaymentType;
import com.hineks.epos.entities.Receipt;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Getter
@Setter
public class AddOrUpdatePayment {
    public UUID id;
    public UUID receiptId;
    public UUID paymentTypeId;
    public Double price;
}
