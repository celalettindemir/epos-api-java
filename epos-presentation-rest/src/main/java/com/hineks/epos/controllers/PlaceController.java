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
public class PlaceController {
    private final PlacesService placesService;

    @PostMapping(value = "/place/add")
    public ServiceResponse<Place> addPlace(@RequestBody String placeName)
    {
        ServiceResponse<Place> response = new ServiceResponse<>();
        if (placeName.length() > 0) {
            Place place = new Place();
            place.setName(placeName);
            place.setCreatedDate(new Date());
            //place.setCreatedBy();
            response = placesService.createOrUpdatePlace(place);
        }
        else {
            response.message = "Alan adı girmediniz!";
        }
        return response;
    }

    @PutMapping(value = "/place/update")
    public ServiceResponse<Place> updatePlace(@RequestBody UpdatePlace place)
    {
        ServiceResponse<Place> response = new ServiceResponse<>();
        if (place.PlaceName.length() > 0){
            ServiceResponse<Place> getPlaceResponse = placesService.getPlaceById(place.id);
            if (!getPlaceResponse.status)
                return getPlaceResponse;

            Place existing = getPlaceResponse.data;
            existing.setName(place.PlaceName);
            existing.setUpdatedDate(new Date());
            return placesService.createOrUpdatePlace(existing);
        }
        else {
            response.message = "Alan adı girmediniz!";
        }
        return response;
    }

    @GetMapping(value = "/place/getPlaceById/{id}")
    public ServiceResponse<Place> getPlaceById(@PathVariable("id") UUID placeId)
    {
        return placesService.getPlaceById(placeId);
    }

    @GetMapping(value = "/place/getPlaces")
    public ServiceResponse<List<Place>> getPlaces()
    {
        return placesService.getPlaces();
    }

    @DeleteMapping(value = "/place/deletePlaceById/{id}")
    public ServiceResponse<Boolean> deletePlaceById(@PathVariable("id") UUID placeId)
    {
        return placesService.deletePlaceById(placeId);
    }
}
