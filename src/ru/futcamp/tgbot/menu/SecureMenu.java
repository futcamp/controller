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

package ru.futcamp.tgbot.menu;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.futcamp.IAppModule;
import ru.futcamp.controller.IController;
import ru.futcamp.controller.subcontrollers.modules.secure.SecureInfo;
import ru.futcamp.controller.subcontrollers.modules.secure.SecureModInfo;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.log.ILogger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Cams menu of tg bot
 */
public class SecureMenu implements IMenu, IAppModule {
    private IController ctrl;
    private ILogger log;
    private IConfigs cfg;

    private String modName;

    public SecureMenu(String name, IAppModule...dep) {
        modName = name;
        this.ctrl = (IController) dep[0];
        this.log = (ILogger) dep[1];
        this.cfg = (IConfigs) dep[2];
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
        SecureInfo info = ctrl.getSecure().getSecureInfo();

        StringBuilder txt = new StringBuilder("<b>Состояние</b>\n");

        txt.append("Сигнализация: ");
        if (info.isStatus())
            txt.append("<b>Работает</b>\n");
        else
            txt.append("<b>Отключена</b>\n");

        txt.append("Сирена: ");
        if (info.isAlarm())
            txt.append("<b>Работает</b>\n");
        else
            txt.append("<b>Отключена</b>\n");

        msg.setText(txt.toString());
        msg.enableHtml(true);
        bot.execute(msg);

        txt = new StringBuilder("<b>Датчики</b>\n");
        for (SecureModInfo modInf : info.getModules()) {
            txt.append(modInf.getAlias()).append(": <b>").append(stateToStr(modInf.isState())).append("</b>\n");
        }

        msg.setText(txt.toString());
        msg.enableHtml(true);
        setButtons(msg);
        bot.execute(msg);
    }

    /**
     * Convert state to string
     * @param state State value
     * @return String value
     */
    private String stateToStr(boolean state) {
        if (state) {
            return "Открыта";
        }
        else {
            return "Закрыта";
        }
    }

    /**
     * Run action
     * @param upd Telegram bot update
     */
    private void startAction(Update upd) {
        String inMsg = upd.getMessage().getText();
        if (inMsg.equals("Включить") || inMsg.equals("Отключить")) {
            try {
                ctrl.getSecure().switchSecureStatus();
            } catch (Exception e) {
                log.error("Fail to switch secure state: " + e.getMessage(), "SECUREMENU");
            }
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

        SecureInfo info = ctrl.getSecure().getSecureInfo();

        /*
         * Add buttons to menu
         */
        for (String[] row : cfg.getTelegramCfg().getMenu().getSecurity()) {
            List<String> group = new LinkedList<>();

            for (String btn : row) {
                if (btn.equals("%status%")) {
                    if (info.isStatus()) {
                        group.add("Отключить");
                    } else {
                        group.add("Включить");
                    }
                } else {
                    group.add(btn);
                }
            }

            addButtonsRow(group, keyboard);
        }

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

    public String getModName() {
        return modName;
    }
}
