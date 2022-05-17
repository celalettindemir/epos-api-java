package com.hineks.epos.controllers;

import com.hineks.epos.definitions.*;
import com.hineks.epos.dto.*;
import com.hineks.epos.entities.*;
import com.hineks.epos.services.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final TableService tableService;
    private final TicketService ticketService;
    private final TicketItemService ticketItemService;
    private final ReceiptService receiptService;
    private final ProductAdditionService productAdditionService;
    private final PaymentService paymentService;

    @PostMapping(value = "/new")
    public ServiceResponse<Ticket> newOrder(@RequestBody NewOrderDTO newOrderDTO)
    {
        ServiceResponse<Ticket> ticketResponse = new ServiceResponse<>();
        if (newOrderDTO.TableName.length() > 0) {
            ServiceResponse<Table> existingPlaceResponse = tableService.getTableByName(newOrderDTO.TableName);

            if (existingPlaceResponse.status) {
                Table currentTable = existingPlaceResponse.data;
                Ticket ticket =new Ticket();
                ticket.setStatus(TicketStatus.OPENING);
                ticket.setType(newOrderDTO.TicketType);
                ticket.setCreatedDate(new Date());
                ticketResponse = ticketService.createOrUpdateTicket(ticket);
                currentTable.setTicketId(ticketResponse.data.getId());
                tableService.createOrUpdateTable(currentTable);
                //currentTable.setWaiter();
                //ticket.setCreatedBy();
            } else {
                ticketResponse.message = "Alan Id değeri hatalı!";
            }
        }
        else {
            ticketResponse.message = "Masa adı girmediniz!";
        }
        return ticketResponse;
    }
    @PostMapping(value = "/add")
    public ServiceResponse<Ticket> AddOrder(@RequestBody OrderDTO orderDTO)
    {
        ServiceResponse<Ticket> ticketResponse = new ServiceResponse<>();
        Ticket ticket=ticketService.getTicketById(UUID.fromString(orderDTO.TicketId)).data;
        List<TicketItem> ticketItem=new ArrayList<>();
        for (OrderItemDTO ticketItemDTO: orderDTO.orderItemDTOS) {
            TicketItem ticketItem1=new TicketItem();
            ticketItem1.setId(ticket.getId());
            List<ProductAddition> productAdditions=new ArrayList<>();
            for(OrderItemAdditionDTO orderItemAdditionDTO:ticketItemDTO.orderItemAdditionDTOList)
            {
                ProductAddition productAddition=new ProductAddition();
                productAddition.setId(orderItemAdditionDTO.AdditionId);
                productAdditions.add(productAddition);
            }
            ticketItem.add(ticketItem1);
        }
        ticket.setTicketItems((Set<TicketItem>) ticketItem);

        ticketResponse=ticketService.createOrUpdateTicket(ticket);

        return ticketResponse;
    }
    @DeleteMapping(value = "/delete/{id}")
    public ServiceResponse<Boolean> DeleteOrder(@PathVariable("id")  String ticketId)
    {
        return ticketService.deleteTicketById(UUID.fromString(ticketId));
    }

    @GetMapping(value = "/list/{id}")
    public ServiceResponse<List<OrderItemDTO>> ListOrderbyTicketId(@PathVariable("id") String ticketId)
    {
        ServiceResponse<List<OrderItemDTO>> orderResponse = new ServiceResponse<>();
        List<TicketItem> ticketItems= ticketItemService.getTicketItemsByTicketId(UUID.fromString(ticketId)).data;
        List<OrderItemDTO> orderItemDTOList=new ArrayList<>();
        for (TicketItem ticketItem:ticketItems
             ) {
            OrderItemDTO orderItemDTO=new OrderItemDTO();
            orderItemDTO.Id=ticketItem.getId();
            orderItemDTO.Price=ticketItem.getPrice();
            orderItemDTO.Count=ticketItem.getCount();
            orderItemDTO.CustomerNumber=ticketItem.getCustomerNumber();
            orderItemDTO.Description=ticketItem.getDescription();
            //Addition Eklenebilir
            orderItemDTOList.add(orderItemDTO);
        }
        orderResponse.setData(orderItemDTOList);
        orderResponse.setStatus(true);
        return orderResponse;
    }
    @GetMapping(value = "/print")
    public ServiceResponse<Boolean> PrintOrder(@PathVariable("id") String ticketId)
    {
        ServiceResponse<Boolean> orderResponse = new ServiceResponse<>();
        List<TicketItem> ticketItems= ticketItemService.getTicketItemsByTicketId(UUID.fromString(ticketId)).data;
        for (TicketItem ticketItem:ticketItems
        ) {
            ticketItem.setPrinted(true);
        }
        ticketItemService.createOrUpdateTicketItems(ticketItems);
        orderResponse.setData(true);
        orderResponse.setStatus(true);
        return orderResponse;
    }
    @PostMapping(value = "/receiptAll/{id}")
    public ServiceResponse<ReceiptDTO> ReceiptAllOrder(@PathVariable("id") String ticketId)
    {
        ServiceResponse<ReceiptDTO> receiptDTOResponse = new ServiceResponse<>();
        ServiceResponse<Receipt> receiptResponse = receiptService.createReceiptByTicketId(UUID.fromString(ticketId));
        ReceiptDTO receiptDTO=new ReceiptDTO();
        receiptDTO.ReceiptId= String.valueOf(receiptResponse.data.getId());
        receiptDTO.TotalPrice=receiptResponse.data.getTotalPrice();
        receiptDTOResponse.data=receiptDTO;
        return receiptDTOResponse;
    }
    @PostMapping(value = "/receiptSelect")
    public ServiceResponse<ReceiptDTO> ReceiptSelectOrder(@RequestBody List<OrderItemDTO> orderDTO)
    {
        ServiceResponse<ReceiptDTO> receiptDTOResponse = new ServiceResponse<>();
        Ticket ticket=new Ticket();
        List<TicketItem> ticketItem=new ArrayList<>();

        for (OrderItemDTO ticketItemDTO: orderDTO) {
            TicketItem ticketItem1=new TicketItem();
            ticketItem1.setId(ticket.getId());
            List<ProductAddition> productAdditions=new ArrayList<>();
            for(OrderItemAdditionDTO orderItemAdditionDTO:ticketItemDTO.orderItemAdditionDTOList)
            {
                ProductAddition productAddition=new ProductAddition();
                productAddition.setId(orderItemAdditionDTO.AdditionId);
                productAdditions.add(productAddition);
            }
            ticketItem.add(ticketItem1);
        }
        ticket.setTicketItems((Set<TicketItem>) ticketItem);
        ticket=ticketService.createOrUpdateTicket(ticket).data;
        receiptDTOResponse = ReceiptAllOrder(String.valueOf(ticket.getId()));
        return receiptDTOResponse;
    }
    @PostMapping(value = "/paymentAll/{id}")
    public ServiceResponse<Receipt> PaymentAllOrder(@PathVariable("id") String ticketId,@RequestBody List<PaymentItemDTO> paymentItemDTOList)
    {
        ServiceResponse<Receipt> receiptDTOResponse = new ServiceResponse<>();
        ServiceResponse<Receipt> receiptResponse =receiptService.getReceiptById(UUID.fromString(ticketId));
        Receipt receipt=receiptResponse.data;
        List<PaymentType> paymentType=paymentService.getPaymentTypes().data;
        List<Payment> payments=new ArrayList<>();
        for (PaymentItemDTO paymentItemDTO:paymentItemDTOList) {
            Payment payment=new Payment();
            for (PaymentType paymentType1 :paymentType) {
                if(paymentType1.getTypeName().contains(paymentItemDTO.Type))
                    payment.setPaymentType(paymentType1);
            }
            payment.setPrice(paymentItemDTO.Price);
            payments.add(payment);
        }
        receipt.setPayments((Set<Payment>) payments);
        receiptService.createOrUpdateReceipt(receipt);
        return receiptDTOResponse;
    }
    @PostMapping(value = "/payment/{id}")
    public ServiceResponse<Receipt> PaymentOrder(@PathVariable("id") String ticketId,@RequestBody PaymentDTO paymentDTO)
    {
        Ticket ticket=new Ticket();
        ticket.setStatus(TicketStatus.OPENING);
        ticket.setType(TicketType.TABLE);
        ticket.setCreatedDate(new Date());
        // Ticket Listesi
        // Eski Ticket Kaldırma
        List<UUID> uuids=new ArrayList<>();
        for (OrderItemDTO orderItemDTO:paymentDTO.orderItemDTOList) {
            uuids.add(orderItemDTO.Id);
        }
        ticket=ticketService.createTicketandUpdateTicketItem(UUID.fromString(ticketId),uuids,ticket).data;
        //Receipt olusturma

        ServiceResponse<Receipt> receiptResponse =receiptService.getReceiptById(UUID.fromString(ticketId));
        Receipt receipt=receiptResponse.data;
        List<PaymentType> paymentType=paymentService.getPaymentTypes().data;
        List<Payment> payments=new ArrayList<>();
        for (PaymentItemDTO paymentItemDTO:paymentDTO.paymentItemDTOList) {
            Payment payment=new Payment();
            for (PaymentType paymentType1 :paymentType) {
                if(paymentType1.getTypeName().contains(paymentItemDTO.Type))
                    payment.setPaymentType(paymentType1);
            }
            payment.setPrice(paymentItemDTO.Price);
            payments.add(payment);
        }
        receipt.setPayments((Set<Payment>) payments);
        receiptService.createOrUpdateReceipt(receipt);

        return receiptResponse;
    }
}
