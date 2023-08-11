package com.zhmyhpyh.bot;

public enum CommandEnum {

    START(
            "/start",
            "приветственное сообщение + меню с командами",
            "Start \uD83C\uDF2D"
    ),
    TOGGLE(
            "/toggle_daily",
            "включить/выключить ежедневную рассылку",
            "Ежедневные сообщения \uD83D\uDCE3"
    ),
    VYACHESLAVE(
            "/vyacheslave_info",
            "вывести информацию о времени, оставшемся до возвращения Вячеслава",
            "Возвращение Вячеслава \uD83E\uDEE1"
    ),
    VICTOR(
            "/victor_info",
            "вывести информацию о времени, оставшемся до возвращения Виктора",
            "Возвращение Виктора \uD83C\uDF46"
    ),
    HELP(
            "/help",
            "вывести описание всех команд",
            "Help \u2753"
    ),
    MSG_COUNT(
            "/msg_count",
            "вывести кол-во написанных сообщений",
            "Счётчик сообщений \uD83D\uDCAC"
    );

    private final String command;
    private final String description;
    private final String buttonText;

    CommandEnum(String command, String description, String buttonText) {
        this.command = command;
        this.description = description;
        this.buttonText = buttonText;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public String getButtonText() {
        return buttonText;
    }
}
