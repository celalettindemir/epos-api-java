package com.hineks.epos.services;

import com.hineks.epos.definitions.ServiceResponse;
import com.hineks.epos.entities.*;
import com.hineks.epos.repositories.PaymentTypesRepository;
import com.hineks.epos.repositories.PaymentsRepository;
import com.hineks.epos.repositories.ReceiptsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentsRepository paymentsRepository;
    private final PaymentTypesRepository paymentTypesRepository;
    private final ReceiptService receiptService;
    public ServiceResponse<Payment> createOrUpdateReceipt(Payment payment)
    {
        ServiceResponse<Payment> response = new ServiceResponse<>();
        try {

            response.data = paymentsRepository.save(payment);
            response.status = true;
            response.message = "Success!";
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }
    public ServiceResponse<Payment> getPaymentById(UUID id)
    {
        ServiceResponse<Payment> response = new ServiceResponse<>();
        Optional<Payment> ticketItem = Optional.empty();
        try {
            ticketItem = paymentsRepository.findById(id).filter(p -> !p.getIsDeleted());
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }

        if (ticketItem.isPresent()) {
            response.data = ticketItem.get();
            response.status = true;
            response.message = "Success!";
        }
        else {
            response.message = "Kayıt bulunamadı.";
        }
        return response;
    }
    public ServiceResponse<List<PaymentType>> getPaymentTypes()
    {
        ServiceResponse<List<PaymentType>> response = new ServiceResponse<>();
        response.data= (List<PaymentType>) paymentTypesRepository.findAll();
        return response;
    }
    public ServiceResponse<Boolean> deletePaymentItemById(UUID id)
    {
        ServiceResponse<Payment> receipResponse = getPaymentById(id);
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        if (receipResponse.data != null) {
            try {
                //ProductDetail isDeleted islemi
                Payment payment = receipResponse.data;
                payment.setIsDeleted(true);
                paymentsRepository.save(payment);

                response.status = true;
                response.message = "Success!";
                response.data = true;
            } catch (Exception ex) {
                response.message = ex.getMessage();
            }
        }
        else{
            response.message = receipResponse.message;
        }
        return response;
    }
    public ServiceResponse<Boolean> paymentType(UUID id)
    {
        ServiceResponse<Payment> receipResponse = getPaymentById(id);
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        if (receipResponse.data != null) {
            try {
                //ProductDetail isDeleted islemi
                Payment payment = receipResponse.data;
                payment.setIsDeleted(true);
                paymentsRepository.save(payment);

                response.status = true;
                response.message = "Success!";
                response.data = true;
            } catch (Exception ex) {
                response.message = ex.getMessage();
            }
        }
        else{
            response.message = receipResponse.message;
        }
        return response;
    }

}
