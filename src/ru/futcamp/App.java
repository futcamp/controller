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
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.futcamp.net.tgbot.ITelegramBot;
import ru.futcamp.net.tgbot.TelegramBot;
import ru.futcamp.net.web.IHttpServer;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.configs.settings.HttpSettings;
import ru.futcamp.utils.configs.settings.TelegramSettings;
import ru.futcamp.utils.log.ILogger;

import static ru.futcamp.DefaultPath.*;
import static ru.futcamp.utils.configs.settings.SettingsType.HTTP_SET;
import static ru.futcamp.utils.configs.settings.SettingsType.TGBOT_SET;

/**
 * Main application class
 */
public class App implements IApp {
    private ILogger log;
    private IConfigs cfg;
    private ITelegramBot bot;
    private TelegramBotsApi botApi;
    private IHttpServer httpSrv;

    App(ILogger log, IConfigs cfg, ITelegramBot bot, TelegramBotsApi botApi,
        IHttpServer httpSrv) {
        this.log = log;
        this.cfg = cfg;
        this.bot = bot;
        this.botApi = botApi;
        this.httpSrv = httpSrv;
    }

    /**
     * Application main start function
     */
    public void start() {
        Path path = Path.getInstance();

        log.setPath(path.getPath(LOG_PATH));

        /**
         * Loading all modules configs
         */
        try {
            log.info("Loading configs", "APP");
            cfg.readFromFile(path.getPath(TG_CFG_PATH), TGBOT_SET);
            cfg.readFromFile(path.getPath(HTTP_PATH), HTTP_SET);
        } catch (Exception e) {
            log.error("Fail to read configs: " + e.getMessage(), "APP");
            return;
        }

        /**
         * Start web server
         */
        HttpSettings httpCfg = cfg.getHttpCfg();
        log.info("Starting http server", "APP");
        try {
            httpSrv.prepare();
            httpSrv.start(httpCfg.getPort(), httpCfg.getQueue(), httpCfg.getThreads());
        } catch (Exception e) {
            log.error("Fail to start web server: " + e.getMessage(), "APP");
            return;
        }

        /**
         * Start bot
         */
        TelegramSettings tgCfg = cfg.getTelegramCfg();
        bot.setData(tgCfg.getKey(), tgCfg.getLogin());
        log.info("Starting telegram bot", "APP");
        try {
            botApi.registerBot((TelegramBot)this.bot);
        } catch (TelegramApiException e) {
            log.error("Fail to start telegram bot: " + e.getMessage(), "APP");
        }
    }
}
