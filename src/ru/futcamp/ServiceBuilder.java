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
import ru.futcamp.controller.modules.light.ILightControl;
import ru.futcamp.controller.modules.light.LightControl;
import ru.futcamp.controller.modules.light.LightTask;
import ru.futcamp.controller.modules.light.db.ILightDB;
import ru.futcamp.controller.modules.light.db.LightDB;
import ru.futcamp.controller.modules.meteo.*;
import ru.futcamp.controller.modules.meteo.db.IMeteoDB;
import ru.futcamp.controller.modules.meteo.db.MeteoDB;
import ru.futcamp.controller.modules.secure.*;
import ru.futcamp.controller.modules.secure.db.ISecureDB;
import ru.futcamp.controller.modules.secure.db.SecureDB;
import ru.futcamp.controller.modules.therm.IThermControl;
import ru.futcamp.controller.modules.therm.ThermControl;
import ru.futcamp.controller.modules.therm.ThermTask;
import ru.futcamp.controller.modules.therm.db.IThermDB;
import ru.futcamp.controller.modules.therm.db.ThermDB;
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
                return new App((ILogger)sc[0],(IConfigs)sc[1], (ITelegramBot)sc[2],(TelegramBotsApi)sc[3],
                        (IHttpServer)sc[4], (IController)sc[5]);

            case CFG_SRV:
                return new Configs();

            case LOG_SRV:
                return new Logger();

            case TG_BOT_SRV:
                return new TelegramBot((ILogger)sc[0], (IConfigs)sc[1], (IMenu)sc[2], (IMenu)sc[3], (IMenu)sc[4],
                        (IMenu)sc[5], (IMenu)sc[6], (IMenu)sc[7], (IMenu)sc[8], (IMenu)sc[9], (IMenu)sc[10],
                        (IMenu)sc[11]);

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
                        (ISecurity) sc[4], (Runnable)sc[5], (IThermControl)sc[6], (Runnable)sc[7],
                        (IMainInHome)sc[8], (ILightControl)sc[9], (Runnable)sc[10]);

            case METEO_SRV:
                return new MeteoStation((IMeteoDB)sc[0]);

            case METEO_TSK_SRV:
                return new MeteoTask((ILogger)sc[0], (IMeteoStation)sc[2], (IConfigs)sc[3], (IMeteoDisplay)sc[4]);

            case TG_BOT_MAIN_MENU_SRV:
                return new MainMenu((IConfigs)sc[0]);

            case TG_BOT_METEO_MENU_SRV:
                return new MeteoMenu((IController)sc[0]);

            case TG_BOT_CAM_MENU_SRV:
                return new CamMenu((IConfigs)sc[0], (IController)sc[1], (ILogger)sc[2]);

            case SECURE_SRV:
                return new Security((ISecureDB)sc[0]);

            case SECURE_TASK_SRV:
                return new SecureTask((ILogger)sc[0], (ISecurity)sc[1], (INotifier)sc[2], (IConfigs)sc[3],
                        (IMainInHome)sc[4], (ILightControl)sc[5]);

            case TG_BOT_SECURE_MENU_SRV:
                return new SecureMenu((IController)sc[0]);

            case NOTIFY_TASK_SRV:
                return new Notifier((IConfigs)sc[0]);

            case SECURE_DB_TASK_SRV:
                return new SecureDB();

            case METEO_DB_SRV:
                return new MeteoDB();

            case TG_BOT_METEO_STAT_MENU_SRV:
                return new MeteoStatMenu((IController)sc[0], (IConfigs)sc[1], (ILogger)sc[2]);

            case THERM_DB_SRV:
                return new ThermDB();

            case THERM_CTRL_SRV:
                return new ThermControl((IThermDB)sc[0]);

            case THERM_TASK_SRV:
                return new ThermTask((ILogger)sc[0], (IThermControl)sc[1], (IMeteoStation)sc[2],
                        (IConfigs)sc[3]);

            case THERM_MENU_SRV:
                return new ThermMenu((IController)sc[0], (IConfigs)sc[1]);

            case TG_BOT_THERM_CTRL_MENU_SRV:
                return new ThermCtrlMenu((IController)sc[0], (ILogger)sc[1]);

            case MIH_SRV:
                return new ManInHome((ISecureDB)sc[0]);

            case TG_BOT_MIH_MENU:
                return new MIHMenu((IController)sc[0]);

            case METEO_LCD_SRV:
                return new MeteoDisplay();

            case LIGHT_DB_SRV:
                return new LightDB();

            case LIGHT_CTRL_SRV:
                return new LightControl((ILightDB)sc[0]);

            case LIGHT_TASK_SRV:
                return new LightTask((ILogger)sc[0], (IConfigs)sc[1], (ILightControl)sc[2]);

            case LIGHT_MENU_SRV:
                return new LightMenu();

            case LIGHT_STR_MENU_SRV:
                return new LightStreetMenu((IConfigs)sc[0], (IController)sc[1]);
        }
        return null;
    }
}
