package ru.romanov.pastbin.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.validation.BindingResult;
import ru.romanov.pastbin.dto.PersonDTO;
import ru.romanov.pastbin.models.Person;
import ru.romanov.pastbin.security.JWTUtil;
import ru.romanov.pastbin.services.RegistrationService;
import ru.romanov.pastbin.util.PersonValidator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @Mock
    private JWTUtil jwtUtil;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private PersonValidator personValidator;
    @Mock
    private RegistrationService registrationService;
    @InjectMocks
    private AuthController authController;

    @Test
    void shouldBeRegister() throws InstantiationException, IllegalAccessException {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setFullName("Test user");
        personDTO.setUsername("testuser");
        personDTO.setPassword("testpassword");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        when(modelMapper.map(personDTO, Person.class)).thenReturn(Person.class.newInstance());
    }

    @Test
    void login() {
    }
}