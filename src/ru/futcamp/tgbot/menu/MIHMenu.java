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
import ru.futcamp.controller.ActMgmt;
import ru.futcamp.controller.IController;
import ru.futcamp.controller.TimeMgmt;
import ru.futcamp.controller.modules.secure.MIHInfo;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.log.ILogger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Cams menu of tg bot
 */
public class MIHMenu implements IMenu, IAppModule {
    private IController ctrl;
    private ILogger log;
    private IConfigs cfg;

    private String modName;

    public MIHMenu(String name, IAppModule ...dep) {
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
        MIHInfo info = ctrl.getMIHInfo();

        String txt = "Система \"Человек в доме\"\n\n";

        txt += "Имитация присутствия: ";
        if (info.isStatus())
            txt += "<b>Работает</b>\n";
        else
            txt += "<b>Отключена</b>\n";

        txt += "Время включения: <b>" + info.getTimeOn() + ":00</b>\n";
        txt += "Время отключения: <b>" + info.getTimeOff() + ":00</b>\n";

        txt += "Радио: ";
        if (info.isRadio())
            txt += "<b>Работает</b>\n";
        else
            txt += "<b>Отключена</b>\n";

        txt += "Лампа: ";
        if (info.isLamp())
            txt += "<b>Работает</b>\n";
        else
            txt += "<b>Отключена</b>\n";

        msg.setText(txt);
        msg.enableHtml(true);
        setButtons(msg);
        bot.execute(msg);
    }

    /**
     * Run action
     * @param upd Telegram bot update
     */
    private void startAction(Update upd) {
        String inMsg = upd.getMessage().getText();

        if (inMsg.equals("Включить") || inMsg.equals("Отключить")) {
            try {
                ctrl.switchMIHStatus();
            } catch (Exception e) {
                log.error("Fail to switch status: " + e.getMessage(), "MIHMENU");
            }
        } else if (upd.getMessage().getText().equals("+ час вкл")) {
            try {
                ctrl.changeMIHTime(TimeMgmt.TIME_MGMT_ON, ActMgmt.SET_MGMT_HOUR_PLUS);
            } catch (Exception e) {
                log.error("Fail to change time: " + e.getMessage(), "MIHMENU");
            }
        } else if (upd.getMessage().getText().equals("- час вкл")) {
            try {
                ctrl.changeMIHTime(TimeMgmt.TIME_MGMT_ON, ActMgmt.SET_MGMT_HOUR_MINUS);
            } catch (Exception e) {
                log.error("Fail to change time: " + e.getMessage(), "MIHMENU");
            }
        } else if (upd.getMessage().getText().equals("+ час откл")) {
            try {
                ctrl.changeMIHTime(TimeMgmt.TIME_MGMT_OFF, ActMgmt.SET_MGMT_HOUR_PLUS);
            } catch (Exception e) {
                log.error("Fail to change time: " + e.getMessage(), "MIHMENU");
            }
        } else if (upd.getMessage().getText().equals("- час откл")) {
            try {
                ctrl.changeMIHTime(TimeMgmt.TIME_MGMT_OFF, ActMgmt.SET_MGMT_HOUR_MINUS);
            } catch (Exception e) {
                log.error("Fail to change time: " + e.getMessage(), "MIHMENU");
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

        MIHInfo info = ctrl.getMIHInfo();

        /*
         * Add buttons to menu
         */
        for (String[] row : cfg.getTelegramCfg().getMenu().getMih()) {
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
