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
import ru.futcamp.controller.subcontrollers.modules.light.LightInfo;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.configs.settings.light.LightGroupSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Light group menu of tg bot
 */
public class LightGroupMenu implements IMenu, IAppModule {
    private IConfigs cfg;
    private IController ctrl;

    private String modName;

    public LightGroupMenu(String name, IAppModule...dep) {
        modName = name;
        this.cfg = (IConfigs) dep[0];
        this.ctrl = (IController) dep[1];
    }

    /**
     * Update telegram message
     * @param bot Telegram bot
     * @param upd Update message
     */
    public void updateMessage(TelegramLongPollingBot bot, Update upd, IBotMenu menu) throws Exception {
        SendMessage msg = new SendMessage().setChatId(upd.getMessage().getChatId());
        StringBuilder txt = new StringBuilder();
        String inMsg = upd.getMessage().getText();
        LightGroupSettings lightGroup = getLightGroup(menu.getGroup());

        if (!inMsg.equals(menu.getGroup()) && !inMsg.equals("Обновить")) {

            if (inMsg.equals("Вкл все")) {
                ctrl.getLight().setGroupStatus(lightGroup.getGroup(), true);
            } else if (inMsg.equals("Откл все")) {
                ctrl.getLight().setGroupStatus(lightGroup.getGroup(), false);
            } else {
                ctrl.getLight().switchLightStatus(inMsg);
            }
        }

        txt.append("Управление освещением\n\n");

        for (LightInfo info : ctrl.getLight().getLightGroupInfo(lightGroup.getGroup())) {
            txt.append(info.getAlias()).append(": ");
            if (info.isStatus())
                txt.append("<b>Работает</b>\n");
            else
                txt.append("<b>Отключен</b>\n");
        }

        msg.enableHtml(true);
        msg.setText(txt.toString());
        setButtons(msg, menu.getGroup());
        bot.execute(msg);
    }

    private LightGroupSettings getLightGroup(String group) {
        for (LightGroupSettings grp : cfg.getTelegramCfg().getMenu().getLight().getGroups()) {
            if (group.equals(grp.getCaption())) {
                return grp;
            }
        }
        return null;
    }

    /**
     * Set buttons for response
     * @param sendMessage Sending message
     */
    private void setButtons(SendMessage sendMessage, String group) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<>();

        /*
         * Add buttons to menu
         */
        for (String[] btnGrp : getLightGroup(group).getDevice()) {
            List<String> lamps = new LinkedList<>(Arrays.asList(btnGrp));
            addButtonsRow(lamps, keyboard);
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
