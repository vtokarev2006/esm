package com.epam.esm;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.Tag;
import com.epam.esm.domain.User;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.springdata.CertificateRepository;
import com.epam.esm.repository.springdata.TagRepository;
import com.epam.esm.repository.springdata.UserRepository;
import com.github.javafaker.Faker;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
class EsmApplicationTests {

    private final OrderRepository orderRepository;

    private final TagRepository tagRepository;


    private final EntityManager em;
    private final Faker faker;

    private final com.epam.esm.repository.springdata.UserRepository userRepository;

    private final com.epam.esm.repository.springdata.UserRepository userRepositorySpringData;


    private final com.epam.esm.repository.springdata.CertificateRepository certificateRepository;
    private final com.epam.esm.repository.springdata.OrderRepository orderRepositorySpringData;


    @Autowired
    public EsmApplicationTests(OrderRepository orderRepository, TagRepository tagRepository, EntityManager em, Faker faker, UserRepository userRepository, UserRepository userRepositorySpringData, CertificateRepository certificateRepository, com.epam.esm.repository.springdata.OrderRepository orderRepositorySpringData) {
        this.orderRepository = orderRepository;
        this.tagRepository = tagRepository;
        this.em = em;
        this.faker = faker;
        this.userRepository = userRepository;
        this.userRepositorySpringData = userRepositorySpringData;
        this.certificateRepository = certificateRepository;
        this.orderRepositorySpringData = orderRepositorySpringData;
    }

    @Test
    void contextLoads() {
        User user = userRepositorySpringData.getUsersOrderedByOrdersCostDesc(PageRequest.of(0,1)).get(0);
        List<Tag> tags = tagRepository.getTagsOrderedDescByFreqUsageByUserId(user.getId());


        System.out.println(user);
        System.out.println(tags);

//        tags.forEach(System.out::println);

/*
        addTags();
        addUsers();
        addCertificates();
        addOrders();
*/
/*
        TestTransaction.flagForCommit(); // need this, otherwise the next line does a rollback
        TestTransaction.end();
*/
    }
    void addOrders() {
        Random r = new Random();
        List<User> users = userRepository.findAll();
        List<Certificate> certificates = certificateRepository.findAll();

        User user;
        Certificate certificate;
        Order order;
        Instant now;

        for (int i = 0; i < 5000; i++) {
            now = Instant.now();
            user = users.get(r.nextInt(1000));
            certificate = certificates.get(r.nextInt(10000));
            order = Order.builder()
                    .user(user)
                    .certificate(certificate)
                    .price(certificate.getPrice())
                    .createDate(now)
                    .description(faker.funnyName().name())
                    .build();

            orderRepositorySpringData.save(order);
        }
    }

    void addCertificates() {
        Random r = new Random();
        IntStream.rangeClosed(1, 7).forEach(i -> {
            Certificate certificate;
            Set<Integer> setOfTagsId = new Random().ints(1, 1001)
                    .distinct()
                    .limit(r.nextInt(r.nextInt(2) + 8) + 1)
                    .boxed()
                    .collect(Collectors.toSet());

            List<Tag> tags = new ArrayList<>();

            setOfTagsId.forEach(id -> tags.add(tagRepository.findById((long) id).get()));
            Instant now = Instant.now();
            certificate = Certificate.builder()
                    .name(faker.funnyName().name())
                    .tags(tags)
                    .description(faker.funnyName().name())
                    .duration(r.nextInt(20) + 1)
                    .price(r.nextDouble(100) + 10)
                    .createDate(now)
                    .lastUpdateDate(now)
                    .build();
            try {
                certificateRepository.save(certificate);
            } catch (Exception ignored) {
            }
        });
    }

    void addTags() {
        List<Tag> tags = new ArrayList<>();
        IntStream.rangeClosed(1, 1000).forEach(i -> {
            tags.add(Tag.builder().name(String.format("Tag%03d", i)).build());
        });
        tagRepository.saveAll(tags);
    }

    void addUsers() {
        List<User> user = new ArrayList<>();
        IntStream.rangeClosed(1, 1000).forEach(i -> {
            user.add(User.builder().email(faker.internet().emailAddress()).password(faker.internet().password()).build());
        });
        userRepository.saveAll(user);
    }


}
