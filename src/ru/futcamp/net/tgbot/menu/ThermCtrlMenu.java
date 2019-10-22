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
import ru.futcamp.controller.modules.therm.IThermDevice;
import ru.futcamp.utils.log.ILogger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Cams menu of tg bot
 */
public class ThermCtrlMenu implements IMenu {
    private IController ctrl;
    private ILogger log;

    public ThermCtrlMenu(IController ctrl, ILogger log) {
        this.ctrl = ctrl;
        this.log = log;
    }

    /**
     * Update telegram message
     * @param bot Telegram bot
     * @param upd Update message
     * @param menu Telegram menu pointer
     */
    public void updateMessage(TelegramLongPollingBot bot, Update upd, IBotMenu menu) throws Exception {
        SendMessage msg = new SendMessage().setChatId(upd.getMessage().getChatId());
        String inMsg = upd.getMessage().getText();

        if (!inMsg.equals("+") && !inMsg.equals("-") && !inMsg.equals("Обновить") &&
                !inMsg.equals("Включить") && !inMsg.equals("Отключить"))
        {
            menu.setDevice(inMsg);
        }

        IThermDevice device = ctrl.getThermDeviceByAlias(menu.getDevice());
        setAction(inMsg, device);

        String txt = "Настройка обогрева\n\n";
        txt += "<b>" + device.getAlias() + "</b>\n";
        txt += "Статус: <b>" + (device.isStatus() ? "Работает" : "Отключен") + "</b>\n";
        txt += "Обогреватель: <b>" + (device.isHeater() ? "Работает" : "Отключен") + "</b>\n";
        txt += "Держать температуру: <b>" + device.getThreshold() + "°</b>\n\n";

        msg.setText(txt);
        msg.enableHtml(true);
        setButtons(msg, menu.getDevice());
        bot.execute(msg);
    }

    /**
     * Make new action
     * @param msg Message
     */
    private void setAction(String msg, IThermDevice device) {
        if (msg.equals("+")) {
            int thresh = device.getThreshold();
            device.setThreshold(thresh + 1);
            saveStates(device);
        } else if (msg.equals("-")) {
            int thresh = device.getThreshold();
            device.setThreshold(thresh - 1);
            saveStates(device);
        } else if (msg.equals("Включить")) {
            device.setStatus(true);
            saveStates(device);
        } else if (msg.equals("Отключить")) {
            device.setStatus(false);
            saveStates(device);
        }
    }

    /**
     * Save states to db
     */
    private void saveStates(IThermDevice device) {
        try {
            ctrl.saveThermState(device);
        } catch (Exception e) {
            log.error("Fail to save therm state to db from device: " + device.getName(), "THERMCTRLMENU");
        }
    }

    /**
     * Set buttons for response
     * @param sendMessage Sending message
     */
    private void setButtons(SendMessage sendMessage, String alias) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<>();

        /*
         * Add back button to menu
         */
        IThermDevice device = ctrl.getThermDeviceByAlias(alias);

        List<String> row1 = new LinkedList<>();
        if (device.isStatus()) {
            row1.add("Отключить");
        } else {
            row1.add("Включить");
        }
        addButtonsRow(row1, keyboard);

        List<String> row2 = new LinkedList<>();
        row2.add("+");
        row2.add("-");
        addButtonsRow(row2, keyboard);

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
