package com.skillbox.cryptobot.service;

import com.skillbox.cryptobot.domain.Subscriber;
import com.skillbox.cryptobot.repository.SubscriberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;

    public Subscriber getSubscriber(Long telegramId) {

        Subscriber subscriber = subscriberRepository.getByTelegramId(telegramId);
        if(subscriber == null) {
            throw new RuntimeException("Подписчик не найден!");
        }
        return subscriber;
    }

    public void updateSubscriber(Long telegramId, double price) {

        Subscriber subscriber = subscriberRepository.getByTelegramId(telegramId);
        if(subscriber != null) {
            subscriber.setPrice(price);
            subscriberRepository.save(subscriber);
        }
    }

    public String deleteSubscribePrise(Long telegram_id) {

        Subscriber subscriber = subscriberRepository.getByTelegramId(telegram_id);
        if(subscriber == null ) {
            log.error("Subscriber with telegram_id " + telegram_id + " not found!");
        }
        assert subscriber != null;
        if (subscriber.getPrice() == null) {
            return "Активные подписки отсутствуют";
        }
        subscriber.setPrice(null);
        subscriberRepository.save(subscriber);
        return "Подписка отменена";
    }

}
