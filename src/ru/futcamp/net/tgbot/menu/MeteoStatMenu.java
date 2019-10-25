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
import ru.futcamp.controller.modules.meteo.db.MeteoDBData;
import ru.futcamp.utils.TimeControl;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.configs.settings.TelegramMeteoStatSettings;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Meteo statistics menu of tg bot
 */
public class MeteoStatMenu implements IMenu {
    private IController ctrl;
    private IConfigs cfg;

    public MeteoStatMenu(IController ctrl, IConfigs cfg) {
        this.ctrl = ctrl;
        this.cfg = cfg;
    }

    /**
     * Update telegram message
     * @param upd Update message
     */
    public void updateMessage(TelegramLongPollingBot bot, Update upd, IBotMenu menu) throws Exception {
        String inMsg = upd.getMessage().getText();


        if (!inMsg.equals("Обновить") && !inMsg.equals("Статистика") && !inMsg.equals("Метео статистика")) {
            TimeControl.getPrevDate();
            List<MeteoDBData> prevData = ctrl.getMeteoDataByDate(inMsg, TimeControl.getPrevDate());
            if (prevData != null) {
                bot.execute(printData(upd, "Вчера", prevData));
            }
            List<MeteoDBData> curData = ctrl.getMeteoDataByDate(inMsg, TimeControl.getCurDate());
            if (curData != null) {
                bot.execute(printData(upd, "Сегодня", curData));
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
     * @param data Meteo data by date
     * @return Sending message
     */
    private SendMessage printData(Update upd, String date, List<MeteoDBData> data) {
        SendMessage msg = new SendMessage().setChatId(upd.getMessage().getChatId());
        String txt = "Место: <b>" + upd.getMessage().getText() + "</b> Дата: <b>" + date + "</b>\n\n";
        int tMin = data.get(0).getTemp();
        int tMax = data.get(0).getTemp();

        for (MeteoDBData datum : data) {
            txt += "Время: <b>";
            if (datum.getHour() < 10) {
                txt += " ";
            }
            txt += datum.getHour() + ":00</b> Темп: <b>" + datum.getTemp() + "°";
            txt += "</b> Влажн: <b>" + datum.getHum() + "%</b>\n";
            if (datum.getTemp() < tMin) {
                tMin = datum.getTemp();
            }
            if (datum.getTemp() > tMax) {
                tMax = datum.getTemp();
            }
        }

        txt += "\nМинимальная t: <b>" + tMin + "°</b>\n";
        txt += "Максимальная t: <b>" + tMax + "°</b>\n";

        msg.enableHtml(true);
        msg.setText(txt);
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

        for (TelegramMeteoStatSettings stat : cfg.getTelegramCfg().getMeteostat()) {
            List<String> statGroup = new LinkedList<>();

            for (String sensor : stat.getSensors()) {
                statGroup.add(sensor);
            }

            addButtonsRow(statGroup, keyboard);
        }

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
