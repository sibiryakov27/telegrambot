package com.zhmyhpyh.bot.inlinebutton;

import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@AllArgsConstructor
public class InlineButton {
    private String text;
    private String callbackData;

    public InlineKeyboardButton getInlineKeyboardButton() {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(text);
        inlineKeyboardButton.setCallbackData(callbackData);
        return inlineKeyboardButton;
    }
}
