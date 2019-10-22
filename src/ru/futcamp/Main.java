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
import ru.futcamp.controller.modules.meteo.IMeteoStation;
import ru.futcamp.controller.modules.meteo.db.IMeteoDB;
import ru.futcamp.controller.modules.secure.ISecurity;
import ru.futcamp.controller.modules.secure.db.ISecureDB;
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
        IMeteoStation meteo = (IMeteoStation)builder.makeService(METEO_SRV, meteoDB);
        Runnable meteoTask = (Runnable)builder.makeService(METEO_TSK_SRV, log, cfg, meteo, cfg);

        ISecureDB secDb = (ISecureDB)builder.makeService(SECURE_DB_TASK_SRV);
        ISecurity secure = (ISecurity)builder.makeService(SECURE_SRV, secDb);
        Runnable secureTask = (Runnable)builder.makeService(SECURE_TASK_SRV, log, secure, notify, cfg);

        IController ctrl = (IController)builder.makeService(CTRL_SRV, log, cfg, meteo, meteoTask, secure, secureTask);

        IMenu mainMenu = (IMenu)builder.makeService(TG_BOT_MAIN_MENU_SRV, cfg);
        IMenu meteoMenu = (IMenu)builder.makeService(TG_BOT_METEO_MENU_SRV, ctrl);
        IMenu camMenu = (IMenu)builder.makeService(TG_BOT_CAM_MENU_SRV, cfg);
        IMenu secMenu = (IMenu)builder.makeService(TG_BOT_SECURE_MENU_SRV, ctrl);
        IMenu meteoStatMenu = (IMenu)builder.makeService(TG_BOT_METEO_STAT_MENU_SRV, ctrl, cfg);
        TelegramBotsApi tgBotApi = (TelegramBotsApi)builder.makeService(TG_BOT_API_SRV);
        ITelegramBot tgBot = (ITelegramBot)builder.makeService(TG_BOT_SRV, log, cfg, mainMenu, meteoMenu, camMenu, secMenu, meteoStatMenu);

        IWebServer server = (IWebServer)builder.makeService(WEB_SRV);
        IHandlersBuilder hdlBuilder = (IHandlersBuilder)builder.makeService(WEB_HDL_SRV, log, ctrl);
        IHttpServer httpSrv = (IHttpServer)builder.makeService(HTTP_SRV, server, hdlBuilder);

        IApp app = (IApp)builder.makeService(APP_SRV, log, cfg, tgBot, tgBotApi, httpSrv, ctrl);

        app.start();
    }
}
