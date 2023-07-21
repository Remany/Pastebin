package ru.romanov.pastbin.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.romanov.pastbin.dto.PersonDTO;
import ru.romanov.pastbin.services.RegistrationService;

@RestController
@RequestMapping("/pastebin/auth")
public class RegistrationController {
    private final RegistrationService registrationService;
    private final ModelMapper modelMapper;

    @Autowired
    public RegistrationController(RegistrationService registrationService, ModelMapper modelMapper) {
        this.registrationService = registrationService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> registration(@RequestBody @Valid PersonDTO personDTO,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {

        }
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
