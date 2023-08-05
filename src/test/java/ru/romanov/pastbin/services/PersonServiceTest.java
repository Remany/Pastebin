package ru.romanov.pastbin.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.romanov.pastbin.models.Person;
import ru.romanov.pastbin.repositories.PersonRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {
    @Mock
    private PersonRepository personRepository;
    @InjectMocks
    private PersonService personService;
    @Test
    void shouldGettingPersonWhenUserFound() {
        Person expectedPerson = new Person();
        expectedPerson.setUsername("some name");
        expectedPerson.setPassword("some password");

        when(personRepository.findPersonByUsername(expectedPerson.getUsername()))
                .thenReturn(Optional.of(expectedPerson));

        Optional<Person> resultPerson = personRepository.findPersonByUsername(expectedPerson.getUsername());

        assertTrue(resultPerson.isPresent());
        assertEquals(expectedPerson.getUsername(), resultPerson.get().getUsername());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        String username = "some name";

        when(personRepository.findPersonByUsername(username)).thenReturn(Optional.empty());

        Optional<Person> resultPerson = personRepository.findPersonByUsername(username);

        assertThrows(NoSuchElementException.class, () -> {
            personService.getPersonByUsername(username);
        });
    }
}