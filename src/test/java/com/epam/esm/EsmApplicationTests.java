package com.epam.esm;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.Tag;
import com.epam.esm.domain.User;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class EsmApplicationTests {

    @Autowired
    private CertificateRepository certificateRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    void testLoadingContext() {
        log.trace("Test log");

        Certificate c = certificateRepository.findById(1L).orElse(Certificate.builder().name("Empty cert").build());
        log.trace(c.toString());

        Tag tag = tagRepository.findById(1L).orElse(Tag.builder().name("Empty tag").build());
        log.trace(tag.toString());
        tag = tagRepository.findById(10L).orElse(Tag.builder().name("Empty tag").build());
        log.trace(tag.toString());

        User user = userRepository.findById(1L).orElse(User.builder().email("Empty user").build());
        log.trace(user.toString());

        Order order = orderRepository.findById(1L).orElse(Order.builder().description("Empty order").build());
        log.trace(order.toString());
    }
}