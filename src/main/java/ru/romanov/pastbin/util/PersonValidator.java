package ru.romanov.pastbin.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.romanov.pastbin.dto.PersonDTO;
import ru.romanov.pastbin.models.Person;
import ru.romanov.pastbin.services.PersonService;

import java.util.Optional;

@Component
public class PersonValidator implements Validator {
    private final PersonService personService;

    @Autowired
    public PersonValidator(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PersonDTO personDTO = (PersonDTO) target;
        Optional<Person> foundPerson = personService.getPersonByUsername(personDTO.getUsername());
        if (foundPerson.isPresent()) {
            errors.rejectValue("username", "", "This username is already taken");
        }
    }
}
