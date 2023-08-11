package com.zhmyhpyh.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class BotConfig {
    @Value("${telegram.bot.name}")
    private String botName;
    @Value("${telegram.bot.token}")
    private String token;
    @Value("${telegram.bot.chatid}")
    private Long chatId;
}
