package com.hineks.epos.controllers;

import com.hineks.epos.definitions.*;
import com.hineks.epos.entities.*;
import com.hineks.epos.services.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TableController {
    private final TableService tableService;
    private final PlacesService placeService;

    @PostMapping(value = "/table/add")
    public ServiceResponse<Table> addTable(@RequestBody AddOrUpdateTable table)
    {
        ServiceResponse<Table> response = new ServiceResponse<>();
        if (table.TableName.length() > 0) {
            ServiceResponse<Place> existingPlaceResponse = placeService.getPlaceById(table.placeId);

            if (existingPlaceResponse.status) {
                Table newTable = new Table();
                newTable.setName(table.TableName);
                newTable.setPlace(existingPlaceResponse.data);
                newTable.setCreatedDate(new Date());
                //place.setCreatedBy();
                response = tableService.createOrUpdateTable(newTable);
            } else {
                response.message = "Alan Id değeri hatalı!";
            }
        }
        else {
            response.message = "Masa adı girmediniz!";
        }
        return response;
    }

    @PutMapping(value = "/table/update")
    public ServiceResponse<Table> updateTable(@RequestBody AddOrUpdateTable table)
    {
        ServiceResponse<Table> response = new ServiceResponse<>();
        if (table.TableName.length() > 0){
            ServiceResponse<Place> existingPlaceResponse = placeService.getPlaceById(table.placeId);
            ServiceResponse<Table> existingTableResponse = tableService.getTableById(table.id);
            if (existingPlaceResponse.status && existingTableResponse.status) {
                Table existingTable = existingTableResponse.data;
                existingTable.setPlace(existingPlaceResponse.data);
                existingTable.setName(table.TableName);
                response = tableService.createOrUpdateTable(existingTable);
            } else {
                response.message = "Masa Id numarası ya da Alan Id numarası hatalı!";
            }
        }
        else {
            response.message = "Alan adı girmediniz!";
        }
        return response;
    }

    @GetMapping(value = "/table/getTableById/{id}")
    public ServiceResponse<Table> getTableById(@PathVariable("id") UUID tableId)
    {
        return tableService.getTableById(tableId);
    }

    @GetMapping(value = "/table/getTables")
    public ServiceResponse<List<Table>> getTables()
    {
        return tableService.getTables();
    }

    @GetMapping(value = "/table/getTablesByPlaceId/{placeId}")
    public ServiceResponse<List<Table>> getTablesByPlaceId(@PathVariable("placeId") UUID placeId)
    {
        return tableService.getTables(placeId);
    }

    @DeleteMapping(value = "/table/deleteTableById/{id}")
    public ServiceResponse<Boolean> deleteTableById(@PathVariable("id") UUID tableId)
    {
        return tableService.deleteTableById(tableId);
    }
}
