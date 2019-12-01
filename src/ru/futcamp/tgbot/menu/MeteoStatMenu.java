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
import ru.futcamp.controller.subcontrollers.modules.meteo.MeteoInfo;
import ru.futcamp.utils.TimeControl;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.log.ILogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Meteo statistics menu of tg bot
 */
public class MeteoStatMenu implements IMenu, IAppModule {
    private IController ctrl;
    private IConfigs cfg;
    private ILogger log;

    private String modName;

    public MeteoStatMenu(String name, IAppModule ...dep) {
        modName = name;
        this.ctrl = (IController) dep[0];
        this.cfg = (IConfigs) dep[1];
        this.log = (ILogger) dep[2];
    }

    /**
     * Update telegram message
     * @param upd Update message
     */
    public void updateMessage(TelegramLongPollingBot bot, Update upd, IBotMenu menu) throws Exception {
        String inMsg = upd.getMessage().getText();

        if (!inMsg.equals("Обновить") && !inMsg.equals("Статистика") && !inMsg.equals("Метео статистика")) {
            TimeControl.getPrevDate();
            List<MeteoInfo> prevData = ctrl.getMeteo().getMeteoInfoByDate(inMsg, TimeControl.getPrevDate());
            if (prevData != null) {
                try {
                    bot.execute(printData(upd, "Вчера", prevData));
                } catch (Exception e) {
                    log.error("Fail to print statistics: " + e.getMessage(), "METEOSTATMENU");
                }
            }
            List<MeteoInfo> curData = ctrl.getMeteo().getMeteoInfoByDate(inMsg, TimeControl.getCurDate());
            if (curData != null) {
                try {
                    bot.execute(printData(upd, "Сегодня", curData));
                } catch (Exception e) {
                    log.error("Fail to print statistics: " + e.getMessage(), "METEOSTATMENU");
                }
            }
        } else {
            SendMessage msg = new SendMessage().setChatId(upd.getMessage().getChatId());
            msg.setText("Метео статистика");
            setButtons(msg);
            bot.execute(msg);
        }
    }

    /**
     * Print data for date
     * @param upd Telegram update
     * @param date Date
     * @param infoList Meteo info list
     * @return Sending message
     */
    private SendMessage printData(Update upd, String date, List<MeteoInfo> infoList) {
        SendMessage msg = new SendMessage().setChatId(upd.getMessage().getChatId());
        StringBuilder txt = new StringBuilder("Место: <b>" + upd.getMessage().getText() + "</b> Дата: <b>" + date + "</b>\n\n");
        int tMin = infoList.get(0).getTemp();
        int tMax = infoList.get(0).getTemp();

        for (MeteoInfo info : infoList) {
            txt.append("Время: <b>");
            if (info.getHour() < 10) {
                txt.append(" ");
            }
            txt.append(info.getHour()).append(":00</b> Темп: <b>").append(info.getTemp()).append("°");
            txt.append("</b> Влажн: <b>").append(info.getHum()).append("%</b>\n");
            if (info.getTemp() < tMin) {
                tMin = info.getTemp();
            }
            if (info.getTemp() > tMax) {
                tMax = info.getTemp();
            }
        }

        txt.append("\nМинимальная t: <b>").append(tMin).append("°</b>\n");
        txt.append("Максимальная t: <b>").append(tMax).append("°</b>\n");

        msg.enableHtml(true);
        msg.setText(txt.toString());
        setButtons(msg);

        return msg;
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

        for (String[] row : cfg.getTelegramCfg().getMenu().getMeteostat()) {
            List<String> statGroup = new LinkedList<>();
            statGroup.addAll(Arrays.asList(row));
            addButtonsRow(statGroup, keyboard);
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
