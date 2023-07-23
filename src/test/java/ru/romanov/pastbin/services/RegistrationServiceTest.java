package ru.romanov.pastbin.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.romanov.pastbin.models.Person;
import ru.romanov.pastbin.repositories.PersonRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {
    @Mock
    private PersonRepository personRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private RegistrationService registrationService;

    @Test
    void shouldBeRegister() {
        Person registerPerson = new Person();
        registerPerson.setUsername("testuser");
        registerPerson.setPassword("testpassword");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        registrationService.registration(registerPerson);

        verify(passwordEncoder).encode("testpassword");
        verify(personRepository).save(registerPerson);

        assertEquals("encodedPassword", registerPerson.getPassword());
        assertEquals("ROLE_USER", registerPerson.getRole());
    }
}