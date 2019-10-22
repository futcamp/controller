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

import org.telegram.telegrambots.meta.TelegramBotsApi;
import ru.futcamp.controller.Controller;
import ru.futcamp.controller.IController;
import ru.futcamp.controller.modules.meteo.IMeteoStation;
import ru.futcamp.controller.modules.meteo.MeteoStation;
import ru.futcamp.controller.modules.meteo.MeteoTask;
import ru.futcamp.controller.modules.meteo.db.IMeteoDB;
import ru.futcamp.controller.modules.meteo.db.MeteoDB;
import ru.futcamp.controller.modules.secure.ISecurity;
import ru.futcamp.controller.modules.secure.SecureTask;
import ru.futcamp.controller.modules.secure.Security;
import ru.futcamp.controller.modules.secure.db.ISecureDB;
import ru.futcamp.controller.modules.secure.db.SecureDB;
import ru.futcamp.net.INotifier;
import ru.futcamp.net.Notifier;
import ru.futcamp.net.tgbot.ITelegramBot;
import ru.futcamp.net.tgbot.TelegramBot;
import ru.futcamp.net.tgbot.menu.*;
import ru.futcamp.net.web.HttpServer;
import ru.futcamp.net.web.IHttpServer;
import ru.futcamp.net.web.handlers.HandlersBuilder;
import ru.futcamp.net.web.handlers.IHandlersBuilder;
import ru.futcamp.net.web.server.IWebServer;
import ru.futcamp.net.web.server.WebServer;
import ru.futcamp.utils.configs.Configs;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.log.ILogger;
import ru.futcamp.utils.log.Logger;

/**
 * Application services objects builder
 */
class ServiceBuilder {
    private static ServiceBuilder sync;

    /**
     * Make new service builder object
     * @return Created object
     */
    static synchronized ServiceBuilder getInstance() {
        if (sync == null)
            sync = new ServiceBuilder();
        return sync;
    }

    /**
     * Make new service object
     * @param name Service name
     * @param sc Services objects
     * @return Service object
     */
    Object makeService(Services name, Object... sc) {
        switch (name) {
            case APP_SRV:
                return new App((ILogger)sc[0],(IConfigs)sc[1],
                        (ITelegramBot)sc[2],(TelegramBotsApi)sc[3],
                        (IHttpServer)sc[4], (IController)sc[5]);

            case CFG_SRV:
                return new Configs();

            case LOG_SRV:
                return new Logger();

            case TG_BOT_SRV:
                return new TelegramBot((ILogger)sc[0], (IConfigs)sc[1], (IMenu)sc[2], (IMenu)sc[3], (IMenu)sc[4],
                                        (IMenu)sc[5], (IMenu)sc[6]);

            case TG_BOT_API_SRV:
                return new TelegramBotsApi();

            case WEB_SRV:
                return new WebServer();

            case HTTP_SRV:
                return new HttpServer((IWebServer)sc[0], (IHandlersBuilder)sc[1]);

            case WEB_HDL_SRV:
                return new HandlersBuilder((ILogger)sc[0], (IController)sc[1]);

            case CTRL_SRV:
                return new Controller((ILogger)sc[0], (IConfigs)sc[1], (IMeteoStation)sc[2], (Runnable)sc[3],
                                        (ISecurity) sc[4], (Runnable)sc[5]);

            case METEO_SRV:
                return new MeteoStation((IMeteoDB)sc[0]);

            case METEO_TSK_SRV:
                return new MeteoTask((ILogger)sc[0], (IMeteoStation)sc[2], (IConfigs)sc[3]);

            case TG_BOT_MAIN_MENU_SRV:
                return new MainMenu((IConfigs)sc[0]);

            case TG_BOT_METEO_MENU_SRV:
                return new MeteoMenu((IController)sc[0]);

            case TG_BOT_CAM_MENU_SRV:
                return new CamMenu((IConfigs)sc[0]);

            case SECURE_SRV:
                return new Security((ISecureDB)sc[0]);

            case SECURE_TASK_SRV:
                return new SecureTask((ILogger)sc[0], (ISecurity)sc[1], (INotifier)sc[2], (IConfigs)sc[3]);

            case TG_BOT_SECURE_MENU_SRV:
                return new SecureMenu((IController)sc[0]);

            case NOTIFY_TASK_SRV:
                return new Notifier((IConfigs)sc[0]);

            case SECURE_DB_TASK_SRV:
                return new SecureDB();

            case METEO_DB_SRV:
                return new MeteoDB();

            case TG_BOT_METEO_STAT_MENU_SRV:
                return new MeteoStatMenu((IController)sc[0], (IConfigs)sc[1]);
        }
        return null;
    }
}
