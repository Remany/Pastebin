package ru.romanov.pastbin.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.romanov.pastbin.dto.AuthDTO;
import ru.romanov.pastbin.dto.PersonDTO;
import ru.romanov.pastbin.models.Person;
import ru.romanov.pastbin.security.JWTUtil;
import ru.romanov.pastbin.services.RegistrationService;
import ru.romanov.pastbin.util.PersonValidator;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/pastebin/auth")
public class AuthController {
    private final RegistrationService registrationService;
    private final AuthenticationManager authenticationManager;
    private final PersonValidator personValidator;
    private final ModelMapper modelMapper;
    private final JWTUtil jwtUtil;

    @Autowired
    public AuthController(RegistrationService registrationService, AuthenticationManager authenticationManager, ModelMapper modelMapper, PersonValidator personValidator, JWTUtil jwtUtil) {
        this.registrationService = registrationService;
        this.authenticationManager = authenticationManager;
        this.modelMapper = modelMapper;
        this.personValidator = personValidator;
        this.jwtUtil = jwtUtil;
    }

    public Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

    private String bindingResultHasErrors(BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder();
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors) {
            errorMessage
                    .append(error.getField())
                    .append(" - ")
                    .append(error.getDefaultMessage())
                    .append(";");
        }
        return errorMessage.toString();
    }

    @PostMapping("/registration")
    public String registration(@RequestBody @Valid PersonDTO personDTO,
                                                   BindingResult bindingResult) {
        personValidator.validate(personDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            log.error("Ошибка при регистрации пользователя: {}", bindingResultHasErrors(bindingResult));
            return bindingResultHasErrors(bindingResult);
        }
        Person person = convertToPerson(personDTO);
        registrationService.registration(person);
        return jwtUtil.generatedToken(person.getUsername());
    }

    @PostMapping("/login")
    public String login (@RequestBody AuthDTO authDTO) {
        UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(
                authDTO.getUsername(), authDTO.getPassword()
        );
        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            return "Incorrect credentials!";
        }
        return jwtUtil.generatedToken(authDTO.getUsername());
    }
}
