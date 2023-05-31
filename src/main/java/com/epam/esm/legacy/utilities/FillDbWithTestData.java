package com.epam.esm.legacy.utilities;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.Tag;
import com.epam.esm.domain.User;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.UserRepository;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class FillDbWithTestData {
    private final TagRepository tagRepository;
    private final Faker faker;
    private final UserRepository userRepository;
    private final CertificateRepository certificateRepository;
    private final OrderRepository orderRepositorySpringData;

    void addOrders() {
        Random r = new Random();
        List<User> users = userRepository.findAll();
        List<Certificate> certificates = certificateRepository.findAll();

        User user;
        Certificate certificate;
        Order order;

        for (int i = 0; i < 5000; i++) {
            user = users.get(r.nextInt(1000));
            certificate = certificates.get(r.nextInt(10000));
            order = Order.builder()
                    .user(user)
                    .certificate(certificate)
                    .price(certificate.getPrice())
                    .description(faker.funnyName().name())
                    .build();

            orderRepositorySpringData.save(order);
        }
    }

    void addCertificates() {
        Random r = new Random();
        IntStream.rangeClosed(1, 7).forEach(i -> {
            Certificate certificate;
            Set<Integer> setOfTagsId = new Random().ints(1, 1001).distinct().limit(r.nextInt(r.nextInt(2) + 8) + 1).boxed().collect(Collectors.toSet());

            Set<Tag> tags = new HashSet<>();

            setOfTagsId.forEach(id -> tags.add(tagRepository.findById((long) id).orElseThrow()));
            certificate = Certificate.builder()
                    .name(faker.funnyName().name())
                    .tags(tags)
                    .description(faker.funnyName().name())
                    .duration(r.nextInt(20) + 1)
                    .price(r.nextDouble(100) + 10)
                    .build();
            try {
                certificateRepository.save(certificate);
            } catch (Exception ignored) {
            }
        });
    }
    void addTags() {
        List<Tag> tags = new ArrayList<>();
        IntStream.rangeClosed(1, 1000).forEach(i -> tags.add(Tag.builder().name(String.format("Tag%03d", i)).build()));
        tagRepository.saveAll(tags);
    }
    void addUsers() {
        List<User> user = new ArrayList<>();
        IntStream.rangeClosed(1, 1000).forEach(i -> user.add(User.builder().email(faker.internet().emailAddress()).password(faker.internet().password()).build()));
        userRepository.saveAll(user);
    }
}
