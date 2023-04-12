package com.zhmyhpyh.bot;

import com.zhmyhpyh.bot.inlinebutton.InlineButton;
import com.zhmyhpyh.bot.inlinebutton.InlineButtonsMaker;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@EnableScheduling
public class ZhmyhpyhTelegramBot extends TelegramLongPollingBot {

    private final BotConfig config;
    private final Map<Long, Boolean> subscribers;
    private final List<InlineButton> commandButtons;
    private InlineButtonsMaker inlineButtonsMaker;

    private static final LocalDateTime VYACHESLAV_RETURNING_DATE = LocalDateTime.of(2023, 6, 27, 0, 0, 0);
    private static final LocalDateTime VICTOR_RETURNING_DATE = LocalDateTime.of(2023, 12, 26, 0, 0, 0);

    public ZhmyhpyhTelegramBot(BotConfig config, InlineButtonsMaker inlineButtonsMaker) {
        this.config = config;
        this.inlineButtonsMaker = inlineButtonsMaker;

        subscribers = new HashMap<>();
        commandButtons = new ArrayList<>();
        for (CommandEnum commandEnum : CommandEnum.values()) {
            commandButtons.add(new InlineButton(commandEnum.getButtonText(), commandEnum.getCommand()));
        }

        try {
            List<BotCommand> botCommands = new ArrayList<>();
            for (CommandEnum commandEnum : CommandEnum.values()) {
                botCommands.add(new BotCommand(commandEnum.getCommand(), commandEnum.getDescription()));
            }
            this.execute(new SetMyCommands(botCommands, new BotCommandScopeDefault(), null));
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

    private void botAnswerUtils(String receivedMessage, long chatId, String userName) {
        for (CommandEnum commandEnum : CommandEnum.values()) {
            if (receivedMessage.startsWith(commandEnum.getCommand())) {
                executeCommand(commandEnum, chatId, userName);
            }
        }
    }

    private void executeCommand(CommandEnum commandEnum, long chatId, String userName) {
        String waitingForTimeUserName;
        switch (commandEnum) {
            case START:
                startBotCommand(chatId, userName);
                break;
            case TOGGLE:
                toggleCommand(chatId);
                break;
            case VICTOR:
                victorCommand(chatId);
                break;
            case VYACHESLAVE:
                vyacheslavCommand(chatId);
                break;
            case HELP:
                helpCommand(chatId);
                break;
            default: break;
        }
    }

    // ==========================КОМАНДЫ=========================================================

    private void startBotCommand(long chatId, String userName) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Привет, " + userName + "! Я сосиска в тесте.");
        message.setReplyMarkup(inlineButtonsMaker.inlineMarkup(commandButtons, 2));

        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e){
            log.error(e.getMessage());
        }
    }

    private void toggleCommand(long chatId) {
        subscribers.putIfAbsent(chatId, false);
        boolean subscription = subscribers.get(chatId);
        String message = subscription ? "Ежедневная рассылка отключена" : "Ежедневная рассылка включена";
        subscribers.put(chatId, !subscription);
        sendMessage(chatId, message);
    }

    private void vyacheslavCommand(long chatId) {
        String message = "До возвращения Вячеслава осталось: " + getDurationMessage(VYACHESLAV_RETURNING_DATE) + ".";
        sendMessage(chatId, message);
    }

    private void victorCommand(long chatId) {
        String message = "До возвращения Виктора осталось: " + getDurationMessage(VICTOR_RETURNING_DATE) + ".";
        sendMessage(chatId, message);
    }

    private void helpCommand(long chatId) {
        StringBuilder message = new StringBuilder("Я сосиска в тесте. " +
                "Меня создали, чтобы считать дни до возвращения Вячеслава и Виктора. " +
                "Но вскоре я могу научиться чему-то ещё...\n\n" +
                "А пока вам доступны следующие команды:\n");
        for (CommandEnum commandEnum : CommandEnum.values()) {
            message.append(commandEnum.getCommand()).append(" - ").append(commandEnum.getDescription()).append(".\n");
        }
        sendMessage(chatId, message.toString().strip());
    }

    // ============================================================================================

    private void sendMessage(long chatId, String text) {
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

    @Scheduled(cron = "${bot.dailymessage.cron}")
    private void myScheduledMethod() {
        if (subscribers.containsValue(true)) {
            String message = "Доброе утро!\n\n" +
                    "До возвращения Вячеслава осталось: " + getDurationMessage(VYACHESLAV_RETURNING_DATE) + ".\n\n" +
                    "До возвращения Виктора осталось: " + getDurationMessage(VICTOR_RETURNING_DATE) + ".\n\n" +
                    "Хорошего дня!";
            for (Map.Entry<Long, Boolean> entry : subscribers.entrySet()) {
                if (entry.getValue()) {
                    sendMessage(entry.getKey(), message);
                }
            }
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

}
