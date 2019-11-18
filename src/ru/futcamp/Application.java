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
import ru.futcamp.controller.IController;
import ru.futcamp.tgbot.ITelegramBot;
import ru.futcamp.tgbot.TelegramBot;
import ru.futcamp.net.web.IHttpServer;
import ru.futcamp.utils.TimeControl;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.configs.settings.HttpSettings;
import ru.futcamp.utils.configs.settings.TelegramSettings;
import ru.futcamp.utils.log.ILogger;

import static ru.futcamp.utils.configs.settings.SettingsType.*;

/**
 * Main application class
 */
public class Application implements IApplication, IAppModule {
    private ILogger log;
    private IConfigs cfg;
    private ITelegramBot bot;
    private TelegramBotsApi botApi;
    private IHttpServer httpSrv;
    private IController ctrl;

    private String modName;

    Application(String name, IAppModule ...dep) {
        this.modName = name;
        this.log = (ILogger) dep[0];
        this.cfg = (IConfigs) dep[1];
        this.bot = (ITelegramBot) dep[2];
        this.botApi = (TelegramBotsApi) dep[3];
        this.httpSrv = (IHttpServer) dep[4];
        this.ctrl = (IController) dep[5];
    }

    /**
     * Application main start function
     */
    public void start() {
        Path path = Path.getInstance();

        log.setPath(path.getPath("log"));

        /*
         * Loading all modules configs
         */
        try {
            log.info("Loading configs", "APP");
            cfg.readFromFile(path.getPath("modules"), MOD_SET);
            cfg.readFromFile(path.getPath("ctrl"), CTRL_SET);
            cfg.readFromFile(path.getPath("tgbot"), TGBOT_SET);
            cfg.readFromFile(path.getPath("http"), HTTP_SET);
            if (cfg.getModCfg("meteo")) {
                cfg.readFromFile(path.getPath("meteo"), METEO_SET);
            }
            if (cfg.getModCfg("security")) {
                cfg.readFromFile(path.getPath("secure"), SECURE_SET);
            }
            if (cfg.getModCfg("therm")) {
                cfg.readFromFile(path.getPath("therm"), THERM_SET);
            }
            if (cfg.getModCfg("light")) {
                cfg.readFromFile(path.getPath("light"), LIGHT_SET);
            }
        } catch (Exception e) {
            log.error("Fail to read configs: " + e.getMessage(), "APP");
            return;
        }

        /*
         * Apply correct timezone
         */
        TimeControl.setTimeZone(cfg.getCtrlCfg().getTimezone());

        /*
         * Start controller modules
         */
        log.info("Starting controller modules", "APP");
        ctrl.startModules();

        /*
         * Start web server
         */
        HttpSettings httpCfg = cfg.getHttpCfg();
        log.info("Starting http server", "APP");
        try {
            httpSrv.prepare(httpCfg.getApi());
            httpSrv.start(httpCfg.getPort(), httpCfg.getQueue(), httpCfg.getThreads());
        } catch (Exception e) {
            log.error("Fail to start web server: " + e.getMessage(), "APP");
            return;
        }

        /*
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

    public String getModName() {
        return modName;
    }
}
