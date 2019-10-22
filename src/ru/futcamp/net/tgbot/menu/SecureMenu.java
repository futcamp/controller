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
import ru.futcamp.controller.modules.secure.ISecureDevice;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Cams menu of tg bot
 */
public class SecureMenu implements IMenu {
    private IController ctrl;

    public SecureMenu(IController ctrl) {
        this.ctrl = ctrl;
    }

    /**
     * Update telegram message
     * @param bot Telegram bot
     * @param upd Update message
     * @param menu Telegram menu pointer
     */
    public void updateMessage(TelegramLongPollingBot bot, Update upd, IBotMenu menu) throws Exception {
        startAction(upd);

        SendMessage msg = new SendMessage().setChatId(upd.getMessage().getChatId());
        String txt = "Состояние\n\n";

        txt += "Сигнализация: ";
        if (ctrl.isSecureStatus())
            txt += "<b>Работает</b>\n";
        else
            txt += "<b>Отключена</b>\n";

        txt += "Сирена: ";
        if (ctrl.isSecureAlarm())
            txt += "<b>Работает</b>\n";
        else
            txt += "<b>Отключена</b>\n";

        msg.setText(txt);
        msg.enableHtml(true);
        bot.execute(msg);

        txt = "Датчики\n\n";
        for (ISecureDevice device : ctrl.getSecureDevices()) {
            txt += device.getAlias() + ": <b>" + stateToStr(device.isState(), device.getType()) + "</b>\n";
        }

        msg.setText(txt);
        msg.enableHtml(true);
        setButtons(msg);
        bot.execute(msg);
    }

    /**
     * Convert state to string
     * @param state State value
     * @param type Type of sensor
     * @return String value
     */
    private String stateToStr(boolean state, String type) {
        if (state) {
            if (type.equals("door"))
                return "Открыта";
            else
                return "Открыто";
        }
        else {
            if (type.equals("door"))
                return "Закрыта";
            else
                return "Закрыто";
        }
    }

    /**
     * Run action
     * @param upd Telegram bot update
     */
    private void startAction(Update upd) {
        if (upd.getMessage().getText().equals("Включить")) {
            ctrl.setSecureStatus(true);
            ctrl.saveSecureStates();
        }
        if (upd.getMessage().getText().equals("Отключить")) {
            ctrl.setSecureStatus(false);
            ctrl.saveSecureStates();
        }
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
         * Add buttons to menu
         */
        List<String> ctrlButtons = new LinkedList<>();
        if (ctrl.isSecureStatus()) {
            ctrlButtons.add("Отключить");
        } else {
            ctrlButtons.add("Включить");
        }
        addButtonsRow(ctrlButtons, keyboard);

        /*
         * Add back button to menu
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
