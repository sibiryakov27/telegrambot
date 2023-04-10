package com.zhmyhpyh.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class Buttons {
    private static final InlineKeyboardButton START_BUTTON = new InlineKeyboardButton("Start \uD83C\uDF2D");
    private static final InlineKeyboardButton VYACHESLAV_BUTTON = new InlineKeyboardButton(
            "Вовращение Вячеслава \uD83E\uDEE1");
    private static final InlineKeyboardButton TOGGLE_BUTTON = new InlineKeyboardButton(
            "Ежедневные сообщения \uD83D\uDCE3");
    private static final InlineKeyboardButton HELP_BUTTON = new InlineKeyboardButton("Help \u2753");

    public static InlineKeyboardMarkup inlineMarkup() {
        START_BUTTON.setCallbackData("/start");
        VYACHESLAV_BUTTON.setCallbackData("/vyacheslave");
        TOGGLE_BUTTON.setCallbackData("/toggle_daily_messaging");
        HELP_BUTTON.setCallbackData("/help");

        List<InlineKeyboardButton> rowInline1 = List.of(START_BUTTON, VYACHESLAV_BUTTON);
        List<InlineKeyboardButton> rowInline2 = List.of(TOGGLE_BUTTON, HELP_BUTTON);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(rowInline1, rowInline2);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }
}