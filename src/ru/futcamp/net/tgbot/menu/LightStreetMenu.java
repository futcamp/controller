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
import ru.futcamp.controller.modules.light.ILightDevice;
import ru.futcamp.utils.configs.IConfigs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Cams menu of tg bot
 */
public class LightStreetMenu implements IMenu {
    private IConfigs cfg;
    private IController ctrl;

    public LightStreetMenu(IConfigs cfg, IController ctrl) {
        this.cfg = cfg;
        this.ctrl = ctrl;
    }

    /**
     * Update telegram message
     * @param bot Telegram bot
     * @param upd Update message
     */
    public void updateMessage(TelegramLongPollingBot bot, Update upd, IBotMenu menu) throws Exception {
        SendMessage msg = new SendMessage().setChatId(upd.getMessage().getChatId());
        String txt = "";
        String inMsg = upd.getMessage().getText();

        if (!inMsg.equals("Уличное освещение") && !inMsg.equals("Обновить") && !inMsg.equals("Улица")) {

            if (inMsg.equals("Вкл все")) {
                for (ILightDevice device : ctrl.getLightDevicesGroup("street")) {
                    device.setStatus(true);
                    device.syncStates();
                }
            } else if (inMsg.equals("Откл все")) {
                for (ILightDevice device : ctrl.getLightDevicesGroup("street")) {
                    device.setStatus(false);
                    device.syncStates();
                }
            } else {
                ILightDevice device = ctrl.getLightDeviceByAlias(inMsg);

                boolean status = device.isStatus();
                device.setStatus(!status);
                ctrl.saveLightState(device);

                device.syncStates();
            }
        }

        txt += "Уличное освещение\n\n";
        for (ILightDevice device : ctrl.getLightDevices()) {
            txt += device.getAlias() + ": ";
            if (device.isStatus())
                txt += "<b>Работает</b>\n";
            else
                txt += "<b>Отключен</b>\n";
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
         * Add buttons to menu
         */
        for (String[] row : cfg.getTelegramCfg().getMenu().getLight()) {
            List<String> lamps = new LinkedList<>();

            for (String btn : row) {
                lamps.add(btn);
            }

            addButtonsRow(lamps, keyboard);
        }

        /*
         * Add back button to menu
         */
        List<String> backButton = new LinkedList<>();
        backButton.add("Вкл все");
        backButton.add("Откл все");
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
