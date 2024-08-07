package net.javaguides.lms.controller;

import jakarta.validation.Valid;
import net.javaguides.lms.dto.ResponseData;
import net.javaguides.lms.entity.User;
import net.javaguides.lms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<User>> getUser(@PathVariable Long id) {
        ResponseData<User> responseData = new ResponseData<>();

        User user = userService.findById(id);
        if (user == null) {
            responseData.setStatus(false);
            responseData.getMessage().add("User not found for ID: " + id);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
        }

        responseData.setStatus(true);
        responseData.setPayload(user);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping
    public ResponseEntity<ResponseData<User>> addUser(@Valid @RequestBody User user, Errors errors) {
        ResponseData<User> responseData = new ResponseData<>();

        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessage().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
        responseData.setStatus(true);
        responseData.getMessage().add("User added successfully");
        responseData.setPayload(userService.save(user));
        return ResponseEntity.ok(responseData);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<User>> updateUser(@Valid @PathVariable Long id, @Valid @RequestBody User user, Errors errors) {
        ResponseData<User> responseData = new ResponseData<>();

        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessage().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        if (!userService.existsById(id)) {
            responseData.setStatus(false);
            responseData.getMessage().add("User not found for ID: " + id);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
        }

        user.setId(id); // Ensure the user ID is set to the path variable ID
        responseData.setStatus(true);
        responseData.getMessage().add("User updated successfully");
        responseData.setPayload(userService.save(user));
        return ResponseEntity.ok(responseData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<User>> deleteUser(@PathVariable Long id) {
        ResponseData<User> responseData = new ResponseData<>();

        User user = userService.findById(id);
        if (user == null) {
            responseData.setStatus(false);
            responseData.getMessage().add("User not found for ID: " + id);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
        }

        userService.deleteById(id);
        responseData.setStatus(true);
        responseData.getMessage().add("User deleted successfully");
        return ResponseEntity.ok(responseData);
    }
}
