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

package ru.futcamp;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import ru.futcamp.controller.IController;
import ru.futcamp.controller.modules.light.ILightControl;
import ru.futcamp.controller.modules.light.db.ILightDB;
import ru.futcamp.controller.modules.meteo.IMeteoDisplay;
import ru.futcamp.controller.modules.meteo.IMeteoStation;
import ru.futcamp.controller.modules.meteo.db.IMeteoDB;
import ru.futcamp.controller.modules.secure.IMainInHome;
import ru.futcamp.controller.modules.secure.ISecurity;
import ru.futcamp.controller.modules.secure.db.ISecureDB;
import ru.futcamp.controller.modules.therm.IThermControl;
import ru.futcamp.controller.modules.therm.db.IThermDB;
import ru.futcamp.net.INotifier;
import ru.futcamp.net.tgbot.ITelegramBot;
import ru.futcamp.net.tgbot.menu.IMenu;
import ru.futcamp.net.web.IHttpServer;
import ru.futcamp.net.web.handlers.IHandlersBuilder;
import ru.futcamp.net.web.server.IWebServer;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.log.ILogger;

import static ru.futcamp.Services.*;

/**
 * Application create objects
 */
public class Main {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        ServiceBuilder builder = ServiceBuilder.getInstance();

        ILogger log = (ILogger)builder.makeService(LOG_SRV);
        IConfigs cfg = (IConfigs)builder.makeService(CFG_SRV);
        INotifier notify = (INotifier)builder.makeService(NOTIFY_TASK_SRV, cfg);

        IMeteoDB meteoDB = (IMeteoDB)builder.makeService(METEO_DB_SRV);
        IMeteoDisplay meteoLcd = (IMeteoDisplay)builder.makeService(METEO_LCD_SRV);
        IMeteoStation meteo = (IMeteoStation)builder.makeService(METEO_SRV, meteoDB);
        Runnable meteoTask = (Runnable)builder.makeService(METEO_TSK_SRV, log, cfg, meteo, cfg, meteoLcd);

        IThermDB thermDb = (IThermDB)builder.makeService(THERM_DB_SRV);
        IThermControl thermCtrl = (IThermControl)builder.makeService(THERM_CTRL_SRV, thermDb);
        Runnable thermTask = (Runnable)builder.makeService(THERM_TASK_SRV, log, thermCtrl, meteo, cfg);

        ILightDB lightDb = (ILightDB)builder.makeService(LIGHT_DB_SRV);
        ILightControl light = (ILightControl)builder.makeService(LIGHT_CTRL_SRV, lightDb);
        Runnable lightTask = (Runnable)builder.makeService(LIGHT_TASK_SRV, log, cfg, light);

        ISecureDB secDb = (ISecureDB)builder.makeService(SECURE_DB_TASK_SRV);
        ISecurity secure = (ISecurity)builder.makeService(SECURE_SRV, secDb);
        IMainInHome mih = (IMainInHome)builder.makeService(MIH_SRV, secDb);
        Runnable secureTask = (Runnable)builder.makeService(SECURE_TASK_SRV, log, secure, notify, cfg, mih, light);

        IController ctrl = (IController)builder.makeService(CTRL_SRV, log, cfg, meteo, meteoTask, secure, secureTask,
                                                            thermCtrl, thermTask, mih, light, lightTask);

        IMenu mainMenu = (IMenu)builder.makeService(TG_BOT_MAIN_MENU_SRV, cfg);
        IMenu meteoMenu = (IMenu)builder.makeService(TG_BOT_METEO_MENU_SRV, ctrl);
        IMenu camMenu = (IMenu)builder.makeService(TG_BOT_CAM_MENU_SRV, cfg, ctrl, log);
        IMenu secMenu = (IMenu)builder.makeService(TG_BOT_SECURE_MENU_SRV, ctrl);
        IMenu meteoStatMenu = (IMenu)builder.makeService(TG_BOT_METEO_STAT_MENU_SRV, ctrl, cfg);
        IMenu thermMenu = (IMenu)builder.makeService(THERM_MENU_SRV, ctrl, cfg);
        IMenu thermCtrlMenu = (IMenu)builder.makeService(TG_BOT_THERM_CTRL_MENU_SRV, ctrl, log);
        IMenu lightMenu = (IMenu)builder.makeService(LIGHT_MENU_SRV);
        IMenu mihMenu = (IMenu)builder.makeService(TG_BOT_MIH_MENU, ctrl);
        IMenu lightStMenu = (IMenu)builder.makeService(LIGHT_STR_MENU_SRV, cfg, ctrl);
        TelegramBotsApi tgBotApi = (TelegramBotsApi)builder.makeService(TG_BOT_API_SRV);
        ITelegramBot tgBot = (ITelegramBot)builder.makeService(TG_BOT_SRV, log, cfg, mainMenu, meteoMenu, camMenu,
                                                                secMenu, meteoStatMenu, thermMenu, thermCtrlMenu, mihMenu,
                                                                lightMenu, lightStMenu);

        IWebServer server = (IWebServer)builder.makeService(WEB_SRV);
        IHandlersBuilder hdlBuilder = (IHandlersBuilder)builder.makeService(WEB_HDL_SRV, log, ctrl);
        IHttpServer httpSrv = (IHttpServer)builder.makeService(HTTP_SRV, server, hdlBuilder);

        IApp app = (IApp)builder.makeService(APP_SRV, log, cfg, tgBot, tgBotApi, httpSrv, ctrl);

        app.start();
    }
}
