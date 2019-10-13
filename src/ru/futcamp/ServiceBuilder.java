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
import ru.futcamp.net.tgbot.ITelegramBot;
import ru.futcamp.net.tgbot.TelegramBot;
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
                        (IHttpServer)sc[4]);

            case CFG_SRV:
                return new Configs();

            case LOG_SRV:
                return new Logger();

            case TG_BOT_SRV:
                return new TelegramBot((ILogger)sc[0], (IConfigs)sc[1]);

            case TG_BOT_API_SRV:
                return new TelegramBotsApi();

            case WEB_SRV:
                return new WebServer();

            case HTTP_SRV:
                return new HttpServer((IWebServer)sc[0], (IHandlersBuilder)sc[1]);

            case WEB_HDL_SRV:
                return new HandlersBuilder();
        }
        return null;
    }
}
