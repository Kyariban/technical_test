package com.test.technical.service;

import com.test.technical.controller.assembler.UserModelAssembler;
import com.test.technical.controller.errorhandling.exception.InvalidUserException;
import com.test.technical.controller.errorhandling.exception.UserAlreadyExistsException;
import com.test.technical.controller.errorhandling.exception.UserNotFoundException;
import com.test.technical.controller.restresources.UserRepresentationModel;
import com.test.technical.dto.UserCreationBean;
import com.test.technical.model.User;
import com.test.technical.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserModelAssembler userModelAssembler;

    public ResponseEntity<UserRepresentationModel> getUserRepresentationModelByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        return new ResponseEntity<>(userModelAssembler.toModel(user), HttpStatus.OK);
    }

    public ResponseEntity<UserRepresentationModel> registerUserAndGetAsRepresentationModel(UserCreationBean creationBean) {
        User user = initializeFromBeanWithParsingValidityConstraints(creationBean);
        checkIfUserIsValid(user);
        checkIfUserAlreadyExists(user);

        User createdUser = userRepository.save(user);
        return new ResponseEntity<>(
                userModelAssembler.toModel(createdUser),
                HttpStatus.CREATED
        );
    }

    private User initializeFromBeanWithParsingValidityConstraints(UserCreationBean creationBean) {
        try {
            return User.fromCreationBean(creationBean);
        } catch (ParseException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private void checkIfUserIsValid(User user) {
        if (Boolean.FALSE.equals(user.isValidForRegistration())) {
            throw new InvalidUserException();
        }
    }

    private void checkIfUserAlreadyExists(User user) {
        Optional<User> alreadyExistingUser = userRepository.findByUsername(user.getUsername());
        if (alreadyExistingUser.isPresent()) {
            throw new UserAlreadyExistsException(alreadyExistingUser.get().getUsername());
        }
    }
}
