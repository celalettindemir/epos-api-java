package com.hineks.epos.services;

import com.hineks.epos.definitions.ServiceResponse;
import com.hineks.epos.entities.*;
import com.hineks.epos.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PersonRoleService {

    private final PersonRolesRepository personRolesRepository;
    private final PersonService personService;

    @Autowired
    public PersonRoleService(PersonRolesRepository personRolesRepository, @Lazy PersonService personService) {
        this.personRolesRepository = personRolesRepository;
        this.personService = personService;
    }

    public ServiceResponse<PersonRole> createOrUpdatePersonRole(PersonRole personRole)
    {
        ServiceResponse<PersonRole> response = new ServiceResponse<>();
        try {
            response.data = personRolesRepository.save(personRole);
            response.status = true;
            response.message = "Success!";
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }

    public ServiceResponse<PersonRole> getPersonRoleById(UUID id)
    {
        ServiceResponse<PersonRole> response = new ServiceResponse<>();
        Optional<PersonRole> personRole = Optional.empty();
        try {
            personRole = personRolesRepository.findById(id).filter(p -> !p.getIsDeleted());
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }

        if (personRole.isPresent()) {
            response.data = personRole.get();
            response.status = true;
            response.message = "Success!";
        }
        else {
            response.message = "Kayıt bulunamadı.";
        }
        return response;
    }

    public ServiceResponse<Boolean> deletePersonRoleById(UUID id)
    {
        ServiceResponse<PersonRole> personRoleResponse = getPersonRoleById(id);
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        if (personRoleResponse.data != null) {
            try {
                //PersonRole isDeleted islemi
                PersonRole personRole = personRoleResponse.data;
                personRole.setIsDeleted(true);
                personRolesRepository.save(personRole);

                //Role altındaki kisilerin isDeleted islemi
                for (Person person :
                        personRole.getPeople()) {
                    personService.deletePersonById(person.getId());
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
            response.message = personRoleResponse.message;
        }
        return response;
    }

    public ServiceResponse<List<PersonRole>> getPersonRoles()
    {
        ServiceResponse<List<PersonRole>> response = new ServiceResponse<>();
        try {
            response.data = personRolesRepository.findAllByIsDeleted(false);
            response.message = "Success!";
            response.status = true;
        } catch (Exception ex) {
            response.message = ex.getMessage();
        }
        return response;
    }
}
