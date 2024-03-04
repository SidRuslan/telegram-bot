package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.domain.Subscriber;
import com.skillbox.cryptobot.service.SubscriberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetSubscriptionCommand implements IBotCommand {

    private final SubscriberService subscriberService;

    @Override
    public String getCommandIdentifier() {
        return "get_subscription";
    }

    @Override
    public String getDescription() {
        return "Возвращает текущую подписку";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());

        try {
            Subscriber subscriber = subscriberService.getSubscriber(message.getFrom().getId());
            if(subscriber.getPrice() != null) {
                answer.setText("Вы подписаны на стоимость биткоина " + subscriber.getPrice() + " USD");
                absSender.execute(answer);
            } else {
                answer.setText("Активные подписки отсутствуют");
                absSender.execute(answer);
            }

        } catch (Exception e) {
            log.error("Ошибка возникла /get_price методе", e);
        }
    }

}