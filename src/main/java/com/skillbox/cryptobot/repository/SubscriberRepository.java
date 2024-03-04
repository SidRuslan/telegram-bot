package com.skillbox.cryptobot.repository;

import com.skillbox.cryptobot.domain.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, UUID> {
    Subscriber getByTelegramId(Long telegramId);
    List<Subscriber> getByPriceAfter(double price);
}
