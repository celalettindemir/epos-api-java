package com.hineks.epos.services;

import com.hineks.epos.definitions.ServiceResponse;
import com.hineks.epos.entities.*;
import com.hineks.epos.repositories.PlacesRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlacesService {

    private final PlacesRepository placesRepository;
    private final TableService tableService;

    @Autowired
    public PlacesService(@Lazy TableService tableService, PlacesRepository placesRepository)
    {
        this.tableService = tableService;
        this.placesRepository = placesRepository;
    }

    public ServiceResponse<Place> createOrUpdatePlace(Place place)
    {
        ServiceResponse<Place> response = new ServiceResponse<Place>();
        try {
            response.data = placesRepository.save(place);
            response.status = true;
            response.message = "Success!";
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }

    public ServiceResponse<Place> getPlaceById(UUID id)
    {
        ServiceResponse<Place> response = new ServiceResponse<Place>();
        Optional<Place> place = Optional.empty();
        try {
            place = placesRepository.findById(id).filter(p -> !p.getIsDeleted());
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }

        if (place.isPresent()) {
            response.data = place.get();
            response.status = true;
            response.message = "Success!";
        }
        else {
            response.message = "Kayıt bulunamadı.";
        }
        return response;
    }

    public ServiceResponse<Boolean> deletePlaceById(UUID id)
    {
        ServiceResponse<Place> placeResponse = getPlaceById(id);
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        if (placeResponse.data != null) {
            try {
                //Place isDeleted islemi
                Place place = placeResponse.data;
                place.setIsDeleted(true);
                placesRepository.save(place);

                //Place altındaki masaların isDeleted islemi
                for (Table table :
                        place.getTables()) {
                    tableService.deleteTableById(table.getId());
                }

                //Response
                response.status = true;
                response.message = "Success!";
                response.data = true;
            } catch (Exception ex) {
                response.message = ex.getMessage();
            }
        }
        else{
            response.message = placeResponse.message;
        }
        return response;
    }

    public ServiceResponse<List<Place>> getPlaces()
    {
        ServiceResponse<List<Place>> response = new ServiceResponse<List<Place>>();
        try {
            response.data = (List<Place>)placesRepository.findAllByIsDeleted(false);
            response.message = "Success!";
            response.status = true;
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }
}
