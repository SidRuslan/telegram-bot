package com.skillbox.cryptobot.service;

import com.skillbox.cryptobot.bot.CryptoBot;
import com.skillbox.cryptobot.domain.Subscriber;
import com.skillbox.cryptobot.repository.SubscriberRepository;
import com.skillbox.cryptobot.utils.TextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class PriceTrackingService {

    private final CryptoBot cryptoBot;
    private final SubscriberRepository subscriberRepository;
    private final CryptoCurrencyService currencyService;
    private final Set<Subscriber> subscribersForNotification = new HashSet<>();
    private double actualBitcoinPrice;

    @Scheduled(fixedRate = 120000)
    private void priceTracking() {
        log.info("Вызван метод SubscribeService -> priceTracking");
        try {
            actualBitcoinPrice = currencyService.getBitcoinPrice();
            List<Subscriber> subscribers = subscriberRepository.getByPriceAfter(actualBitcoinPrice);
            if (subscribers.isEmpty()) {
                subscribersForNotification.clear();
                return;
            }
            for (Subscriber subscriber : subscribers) {
                if (!subscribersForNotification.contains(subscriber)) {
                    sendMessage(subscriber.getTelegramId(), actualBitcoinPrice);
                }
            }
            subscribersForNotification.clear();
            subscribersForNotification.addAll(subscribers);

        } catch (Exception e) {
            log.error("Ошибка возникла в методе SubscribeService -> priceTracking", e);
        }
    }


    @Scheduled(initialDelay = 600000, fixedRate = 600000)
    private void userNotification() {
        log.info("Вызван метод SubscribeService -> userNotification");
        if(subscribersForNotification.isEmpty()) {
            return;
        }
        try {
            actualBitcoinPrice = currencyService.getBitcoinPrice();
            for (Subscriber subscriber : subscribersForNotification) {
                if(actualBitcoinPrice < subscriber.getPrice()) {
                    sendMessage(subscriber.getTelegramId(), actualBitcoinPrice);
                } else {
                    subscribersForNotification.remove(subscriber);
                }
            }
        } catch (Exception e) {
            log.error("Ошибка возникла в методе SubscribeService -> sendMessage", e);
        }
    }

    private void sendMessage(Long telegramId, double actualBitcoinPrice ) throws TelegramApiException {
        log.info("Вызван метод SubscribeService -> sendMessage");
        SendMessage sendMessage = new SendMessage(telegramId.toString(),
                "Пора покупать, стоимость биткоина " + TextUtil.toString(actualBitcoinPrice) + " USD");
        cryptoBot.execute(sendMessage);
    }
}
