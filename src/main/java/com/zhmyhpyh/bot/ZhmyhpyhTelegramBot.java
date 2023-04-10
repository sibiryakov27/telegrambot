package com.zhmyhpyh.bot;

import com.zhmyhpyh.config.BotConfig;
import com.zhmyhpyh.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@EnableScheduling
public class ZhmyhpyhTelegramBot extends TelegramLongPollingBot {

    private static final List<BotCommand> LIST_OF_COMMANDS = List.of(
            new BotCommand("/start", "start bot"),
            new BotCommand("/vyacheslave", "Информация о возвращении Вячеслава"),
            new BotCommand("/victor", "Информация о возвращении Виктора"),
            new BotCommand("/toggle_daily_messaging", "Включить/отключить ежедневные утренние сообщения"),
            new BotCommand("/help", "bot info")
    );

    private static final String HELP_TEXT = "Я сосиска в тесте. Я считаю дни до возвращения Вячеслава. " +
            "Вам доступны следующие команды:\n\n" +
            "/start - приветственное сообщение\n" +
            "/toggle_daily_messaging - включить/отключить ежедневные утренние сообщения\n" +
            "/help - помощь\n" +
            "/vyacheslave - информация о времени, оставшемся до возвращения Вячеслава\n" +
            "/victor - информация о времени, оставшемся до возвращения Виктора";

    private final BotConfig config;
    private final LocalDateTime vyacheslavReturningDate = LocalDateTime.of(2023, 6, 27, 0, 0, 0);
    private final LocalDateTime victorReturningDate = LocalDateTime.of(2023, 12, 26, 0, 0, 0);

    private final Map<Long, Boolean> subscribers = new HashMap<>();

    public ZhmyhpyhTelegramBot(BotConfig config) {
        this.config = config;
        try {
            this.execute(new SetMyCommands(LIST_OF_COMMANDS, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e){
            log.error(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(@NotNull Update update) {
        long chatId;
        long userId = 0;
        String userName;
        String receivedMessage;

        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
            userId = update.getMessage().getFrom().getId();
            userName = update.getMessage().getFrom().getFirstName();

            if (update.getMessage().hasText()) {
                receivedMessage = update.getMessage().getText();
                botAnswerUtils(receivedMessage, chatId, userName);
            }

        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            userId = update.getCallbackQuery().getFrom().getId();
            userName = update.getCallbackQuery().getFrom().getFirstName();
            receivedMessage = update.getCallbackQuery().getData();

            botAnswerUtils(receivedMessage, chatId, userName);
        }
    }

    @Scheduled(cron = "${bot.dailymessage.cron}")
    private void myScheduledMethod() {
        if (subscribers.containsValue(true)) {
            String message = "Доброе утро!\n\n" +
                    "До возвращения Вячеслава осталось: " + getDurationMessage(vyacheslavReturningDate) + ".\n\n" +
                    "До возвращения Виктора осталось: " + getDurationMessage(victorReturningDate) + ".\n\n" +
                    "Хорошего дня!";
            for (Map.Entry<Long, Boolean> entry : subscribers.entrySet()) {
                if (entry.getValue()) {
                    sendMessage(entry.getKey(), message);
                }
            }
        }
    }

    private void botAnswerUtils(String receivedMessage, long chatId, String userName) {
        if (chatId < 0L) {
            receivedMessage = receivedMessage.replace("@" + config.getBotName(), "");
        }
        String message;
        String waitingForTimeUserName;
        switch (receivedMessage) {
            case "/start":
                startBot(chatId, userName);
                break;
            case "/vyacheslave":
                message = "До возвращения Вячеслава осталось: " + getDurationMessage(vyacheslavReturningDate) + ".";
                sendMessage(chatId, message);
                break;
            case "/victor":
                message = "До возвращения Виктора осталось: " + getDurationMessage(victorReturningDate) + ".";
                sendMessage(chatId, message);
                break;
            case "/toggle_daily_messaging":
                subscribers.putIfAbsent(chatId, false);
                boolean sendMessage = subscribers.get(chatId);
                message = sendMessage ? "Ежедневная рассылка отключена" : "Вы включили ежедневную рассылку";
                subscribers.put(chatId, !sendMessage);
                sendMessage(chatId, message);
                break;
            case "/help":
                sendMessage(chatId, HELP_TEXT);
                break;
            default: break;
        }
    }

    private void startBot(long chatId, String userName) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Привет, " + userName + "! Я сосиска в тесте.");
        message.setReplyMarkup(Buttons.inlineMarkup());

        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e){
            log.error(e.getMessage());
        }
    }

    private String getDurationMessage(LocalDateTime future) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(now, future);
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        String daysString = StringUtils.getCorrectWordForm(days, "день", "дня", "дней");
        String hoursString = StringUtils.getCorrectWordForm(hours, "час", "часа", "часов");
        String minutesString = StringUtils.getCorrectWordForm(minutes, "минута", "минуты", "минут");
        String secondsString = StringUtils.getCorrectWordForm(seconds, "секунда", "секунды", "секунд");

        return String.format("%d %s, %d %s, %d %s, %d %s",
                days, daysString, hours, hoursString, minutes, minutesString, seconds, secondsString);
    }

    public void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

}
