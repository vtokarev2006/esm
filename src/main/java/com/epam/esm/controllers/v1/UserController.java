package com.epam.esm.controllers.v1;

import com.epam.esm.domain.Order;
import com.epam.esm.domain.User;
import com.epam.esm.exceptions.ResourceDoesNotExistException;
import com.epam.esm.services.OrderService;
import com.epam.esm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Deprecated
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;
    private final OrderService orderService;

    @Autowired
    public UserController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public User get(@PathVariable long id) {
        Optional<User> user = userService.get(id);
        if (user.isEmpty())
            throw new ResourceDoesNotExistException("User not found, id = " + id);
        return user.get();
    }

    @GetMapping
    public Page<User> getAll(Pageable pageable) {
        return userService.getAll(pageable);
    }

    @GetMapping("/{userId}/orders")
    public List<Order> getUserOrders(@PathVariable long userId) {
        return orderService.getByUserId(userId);
    }

    @GetMapping("/{userId}/orders/{orderId}")
    public Order getUserOrders(@PathVariable long userId, @PathVariable long orderId) {
        return orderService.getByUserOrderId(userId, orderId);
    }


}
