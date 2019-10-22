///////////////////////////////////////////////////////////////////////
//
// Future Camp Project
//
// Copyright(C) 2019 Sergey Denisov.
//
// Written by Sergey Denisov aka LittleBuster(DenisovS21@gmail.com)
// Github:  https://github.com/LittleBuster
//          https://github.com/futcamp
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public Licence 3
// as published by the Free Software Foundation; either version 3
// of the Licence, or(at your option) any later version.
//
///////////////////////////////////////////////////////////////////////

package ru.futcamp.net.tgbot.menu;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.futcamp.controller.IController;
import ru.futcamp.controller.modules.meteo.IMeteoDevice;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Meteo menu of tg bot
 */
public class MeteoMenu implements IMenu {
    private IController ctrl;

    public MeteoMenu(IController ctrl) {
        this.ctrl = ctrl;
    }

    /**
     * Update telegram message
     * @param upd Update message
     */
    public void updateMessage(TelegramLongPollingBot bot, Update upd, IBotMenu menu) throws Exception {
        String txt = "";
        int pressure = 0;
        SendMessage msg = new SendMessage().setChatId(upd.getMessage().getChatId());

        List<IMeteoDevice> devices = ctrl.getMeteoDevices();
        txt += "Метео данные\n\n";
        for (IMeteoDevice device : devices) {
            txt += "<b>" + device.getAlias() + "</b>\nТемпература: <b>" + device.getTemp() + "°</b> ";
            if (device.getHumidity() != 0) {
                txt += "Влажность: <b>" + device.getHumidity() + "%</b>\n\n";
            } else {
                txt += "\n\n";
            }
            if (device.getPressure() != 0) {
                pressure = device.getPressure();
            }
        }
        if (pressure != 0) {
            txt += "Атм. давление: <b>" + pressure + "mm</b>";
        }

        msg.enableHtml(true);
        msg.setText(txt);
        setButtons(msg);

        bot.execute(msg);
    }

    /**
     * Set buttons for response
     * @param sendMessage Sending message
     */
    private void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<>();

        /*
         * Add meteo stat button
         */
        List<String> statButton = new LinkedList<>();
        statButton.add("Статистика");
        addButtonsRow(statButton, keyboard);

        /*
         * Add update and back button to menu
         */
        List<String> backButton = new LinkedList<>();
        backButton.add("Обновить");
        backButton.add("Назад");
        addButtonsRow(backButton, keyboard);

        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    /**
     * Add row with text buttons
     * @param buttons Text buttons
     * @param keyboard Keyboard row
     */
    private void addButtonsRow(List<String> buttons, List<KeyboardRow> keyboard) {
        KeyboardRow row = new KeyboardRow();

        for (String button : buttons) {
            row.add(new KeyboardButton(button));
        }

        keyboard.add(row);
    }
}
