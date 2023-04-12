package com.zhmyhpyh.bot.inlinebutton;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class InlineButtonsMaker {

    public InlineKeyboardMarkup inlineMarkup(List<InlineButton> buttons, int numberOfColumns) {
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        for (InlineButton button : buttons) {
            rowInline.add(button.getInlineKeyboardButton());
            if (rowInline.size() == numberOfColumns) {
                rowsInline.add(rowInline);
                rowInline = new ArrayList<>();
            }
        }
        if (rowInline.size() > 0) {
            rowsInline.add(rowInline);
        }

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rowsInline);

        return markup;
    }
}
