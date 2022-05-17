package com.hineks.epos.services;

import com.hineks.epos.entities.Person;
import com.hineks.epos.repositories.PeopleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final PeopleRepository peopleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = peopleRepository.findByName(username);
        if (person == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(person.getName(), person.getPassword(), emptyList());
    }
}
