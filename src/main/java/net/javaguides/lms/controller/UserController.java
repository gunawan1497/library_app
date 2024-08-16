package net.javaguides.lms.controller;

import jakarta.validation.Valid;
import net.javaguides.lms.dto.ResponseData;
import net.javaguides.lms.entity.Book;
import net.javaguides.lms.entity.User;
import net.javaguides.lms.service.UserService;
import net.javaguides.lms.util.ResponseUtil;
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
        User user = userService.findById(id);

        if (user == null) {
            return ResponseUtil.createNotFoundResponse("User not found for ID: " + id);
        }
        return ResponseUtil.createSuccessResponse(user, "User retrieved successfully");
    }

    @PostMapping
    public ResponseEntity<ResponseData<User>> addUser(@Valid @RequestBody User user, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseUtil.createErrorResponse(errors);
        }
        return ResponseUtil.createSuccessResponse(userService.save(user), "User added successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<User>> updateUser(@Valid @PathVariable Long id, @Valid @RequestBody User user, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseUtil.createErrorResponse(errors);
        }
        if (!userService.existsById(id)) {
            return ResponseUtil.createNotFoundResponse("User not found for ID: " + id);
        }
        user.setId(id);
        return ResponseUtil.createSuccessResponse(userService.save(user), "User updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<User>> deleteUser(@PathVariable Long id) {
        if (!userService.existsById(id)) {
            return ResponseUtil.createNotFoundResponse("User not found for ID: " + id);
        }
        userService.deleteById(id);
        return ResponseUtil.createSuccessResponse(null, "User deleted successfully");
    }
}
