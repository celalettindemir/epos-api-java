package com.hineks.epos.services;

import com.hineks.epos.definitions.ServiceResponse;
import com.hineks.epos.entities.Place;
import com.hineks.epos.entities.Table;
import com.hineks.epos.repositories.TablesRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TableService {

    private final TablesRepository tablesRepository;
    private final PlacesService placesService;

    @Autowired
    public TableService(TablesRepository tablesRepository, @Lazy PlacesService placesService) {
        this.tablesRepository = tablesRepository;
        this.placesService = placesService;
    }

    public ServiceResponse<Table> createOrUpdateTable(Table table)
    {
        ServiceResponse<Table> response = new ServiceResponse<>();
        try {
            response.data = tablesRepository.save(table);
            response.status = true;
            response.message = "Success!";
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }

    public ServiceResponse<Table> getTableById(UUID id)
    {
        ServiceResponse<Table> response = new ServiceResponse<>();
        Optional<Table> table = Optional.empty();
        try {
            table = tablesRepository.findById(id).filter(p -> !p.getIsDeleted());
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }

        if (table.isPresent()) {
            response.data = table.get();
            response.status = true;
            response.message = "Success!";
        }
        else {
            response.message = "Kayıt bulunamadı.";
        }
        return response;
    }

    public ServiceResponse<Boolean> deleteTableById(UUID id)
    {
        ServiceResponse<Table> tableResponse = getTableById(id);
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        if (tableResponse.data != null) {
            try {
                Table table = tableResponse.data;
                table.setIsDeleted(true);
                tablesRepository.save(table);

                response.status = true;
                response.message = "Success!";
                response.data = true;
            } catch (Exception ex) {
                response.message = ex.getMessage();
            }
        }
        else{
            response.message = tableResponse.message;
        }
        return response;
    }

    public ServiceResponse<List<Table>> getTables()
    {
        ServiceResponse<List<Table>> response = new ServiceResponse<>();
        try {
            response.data = tablesRepository.findAllByIsDeleted(false);
            response.message = "Success!";
            response.status = true;
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }

    public ServiceResponse<List<Table>> getTables(UUID placeId)
    {
        ServiceResponse<List<Table>> response = new ServiceResponse<>();
        try {
            ServiceResponse<Place> placeResponse = placesService.getPlaceById(placeId);
            if (placeResponse.status) {
                response.data = new ArrayList<Table>(placeResponse.data.getTables());
                response.message = "Success!";
                response.status = true;
            }
            else {
                response.message = placeResponse.message;
            }
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }
    public ServiceResponse<Table> getTableByName(String tableName)
    {
        ServiceResponse<Table> response = new ServiceResponse<>();
        try {
            ServiceResponse<Table> tableResponse = tablesRepository.findByName(tableName);
            if (response.status) {
                response.data = tableResponse.data;
                response.message = "Success!";
                response.status = true;
            }
            else {
                response.message = tableResponse.message;
            }
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }
}
