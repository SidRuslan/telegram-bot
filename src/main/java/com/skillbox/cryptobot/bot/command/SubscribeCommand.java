package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.service.SubscriberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * Обработка команды подписки на курс валюты
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SubscribeCommand implements IBotCommand {

    private final SubscriberService subscriberService;
    private final GetPriceCommand getPriceCommand;

    @Override
    public String getCommandIdentifier() {
        return "subscribe";
    }

    @Override
    public String getDescription() {
        return "Подписывает пользователя на стоимость биткоина";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        try {
            if (isMessageCorrect(arguments)) {
                double price = Double.parseDouble(arguments[0]);
                getPriceCommand.processMessage(absSender, message, arguments);
                subscriberService.updateSubscriber(message.getFrom().getId(), price);
                answer.setText("Новая подписка создана на стоимость " + price + " USD");
                absSender.execute(answer);
            } else {
                answer.setText("После команды /subscribe введите число.\n" +
                        "Пример ввода: /subscribe 11111");
                absSender.execute(answer);
            }
        } catch (Exception e) {
            log.error("Ошибка возникла в /subscribe методе", e);
        }
    }

    private boolean isMessageCorrect(String[] arguments) {

        String inputRegex = "[0-9]*[.,]?[0-9]*";
        return arguments.length != 0 && arguments[0].matches(inputRegex);
    }

}