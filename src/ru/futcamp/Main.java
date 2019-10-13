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
import ru.futcamp.net.tgbot.ITelegramBot;
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

        TelegramBotsApi tgBotApi = (TelegramBotsApi)builder.makeService(TG_BOT_API_SRV);
        ITelegramBot tgBot = (ITelegramBot)builder.makeService(TG_BOT_SRV, log, cfg);

        IWebServer server = (IWebServer)builder.makeService(WEB_SRV);
        IHandlersBuilder hdlBuilder = (IHandlersBuilder)builder.makeService(WEB_HDL_SRV);
        IHttpServer httpSrv = (IHttpServer)builder.makeService(HTTP_SRV, server, hdlBuilder);

        IApp app = (IApp)builder.makeService(APP_SRV, log, cfg, tgBot, tgBotApi, httpSrv);

        app.start();
    }
}
