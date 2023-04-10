package com.zhmyhpyh.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class Buttons {
    private static final InlineKeyboardButton START_BUTTON = new InlineKeyboardButton("Start \uD83C\uDF2D");
    private static final InlineKeyboardButton TOGGLE_BUTTON = new InlineKeyboardButton(
            "Ежедневные сообщения \uD83D\uDCE3");
    private static final InlineKeyboardButton VYACHESLAV_BUTTON = new InlineKeyboardButton(
            "Возвращение Вячеслава \uD83E\uDEE1");
    private static final InlineKeyboardButton VICTOR_BUTTON = new InlineKeyboardButton(
            "Возвращение Виктора \uD83C\uDF46");
    private static final InlineKeyboardButton HELP_BUTTON = new InlineKeyboardButton("Help \u2753");

    public static InlineKeyboardMarkup inlineMarkup() {
        START_BUTTON.setCallbackData("/start");
        VYACHESLAV_BUTTON.setCallbackData("/vyacheslave");
        VICTOR_BUTTON.setCallbackData("/victor");
        TOGGLE_BUTTON.setCallbackData("/toggle_daily_messaging");
        HELP_BUTTON.setCallbackData("/help");

        List<InlineKeyboardButton> rowInline1 = List.of(START_BUTTON, TOGGLE_BUTTON);
        List<InlineKeyboardButton> rowInline2 = List.of(VYACHESLAV_BUTTON, VICTOR_BUTTON);
        List<InlineKeyboardButton> rowInline3 = List.of(HELP_BUTTON);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(rowInline1, rowInline2, rowInline3);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }
}
