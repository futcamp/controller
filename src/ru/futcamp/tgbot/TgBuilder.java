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

package ru.futcamp.tgbot;

import ru.futcamp.IAppModule;
import ru.futcamp.IBuilder;
import ru.futcamp.tgbot.menu.*;

public class TgBuilder implements IBuilder {
    /**
     * Make telegram modules
     * @param name Name of module
     * @param dep Dependencies
     * @return Module
     */
    public IAppModule makeModule(String name, IAppModule ...dep) {
        if (name.equals("mainm")) {
            return new MainMenu(name, dep);
        } else if (name.equals("meteom")) {
            return new MeteoMenu(name, dep);
        } else if (name.equals("camm")) {
            return new CamMenu(name, dep);
        } else if (name.equals("securem")) {
            return new SecureMenu(name, dep);
        } else if (name.equals("meteosm")) {
            return new MeteoStatMenu(name, dep);
        } else if (name.equals("thermm")) {
            return new ThermMenu(name, dep);
        } else if (name.equals("thermcm")) {
            return new ThermCtrlMenu(name, dep);
        } else if (name.equals("lightm")) {
            return new LightMenu(name, dep);
        } else if (name.equals("mihm")) {
            return new MIHMenu(name, dep);
        } else if (name.equals("lightsm")) {
            return new LightGroupMenu(name, dep);
        } else if (name.equals("tgbotapi")) {
            return new FcBotApi(name, dep);
        } else if (name.equals("tgbot")) {
            return new TelegramBot(name, dep);
        } else if (name.equals("humm")) {
            return new HumMenu(name, dep);
        } else if (name.equals("humcm")) {
            return new HumCtrlMenu(name, dep);
        } else {
            return null;
        }
    }
}
