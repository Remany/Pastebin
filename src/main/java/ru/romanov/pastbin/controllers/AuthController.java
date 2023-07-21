package ru.romanov.pastbin.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.romanov.pastbin.dto.PersonDTO;
import ru.romanov.pastbin.models.Person;
import ru.romanov.pastbin.services.RegistrationService;
import ru.romanov.pastbin.util.AuthErrorResponse;
import ru.romanov.pastbin.util.PersonAlreadyTakenException;
import ru.romanov.pastbin.util.PersonValidator;

import java.util.List;

@RestController
@RequestMapping("/pastebin/auth")
public class AuthController {
    private final RegistrationService registrationService;
    private final PersonValidator personValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthController(RegistrationService registrationService, ModelMapper modelMapper, PersonValidator personValidator) {
        this.registrationService = registrationService;
        this.modelMapper = modelMapper;
        this.personValidator = personValidator;
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

    public Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody @Valid PersonDTO personDTO,
                                                   BindingResult bindingResult) {
        personValidator.validate(personDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new PersonAlreadyTakenException(bindingResultHasErrors(bindingResult));
        }
        Person person = convertToPerson(personDTO);
        registrationService.registration(person);
        return ResponseEntity.ok(String.valueOf(HttpStatus.OK));
    }

    @ExceptionHandler
    private ResponseEntity<AuthErrorResponse> handleException(PersonAlreadyTakenException e){
        AuthErrorResponse error = new AuthErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
