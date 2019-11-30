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

import java.util.HashMap;
import java.util.Map;

/**
 * Application super fabric
 */
public class FutureCamp implements IFutureCamp {
    private Map<String, IAppModule> modules = new HashMap<>();

    /**
     * Make builder object
     * @return Builder
     */
    static IFutureCamp create() {
        return new FutureCamp();
    }

    /**
     * Build app modules
     */
    public void build() {
        ApiContextInitializer.init();
        IAppBuilder builder = AppBuilder.create();

        buildCommon(builder.makeBuilder("common"));
        buildController(builder.makeBuilder("controller"));
        buildWeb(builder.makeBuilder("web"));
        buildTelegram(builder.makeBuilder("telegram"));
        buildApp(builder.makeBuilder("common"));
    }

    /**
     * Start application
     */
    public void start() {
        ((IApplication)getMod("app")).start();
    }

    /**
     * Make common objects
     * @param builder Common fabric
     */
    private void buildCommon(IBuilder builder) {
        addModule(builder.makeModule("log"));
        addModule(builder.makeModule("cfg"));
        addModule(builder.makeModule("ntf", getMod("cfg")));
    }

    /**
     * Make controller module objects
     * @param builder Controller fabric
     */
    private void buildController(IBuilder builder) {
        addModule(builder.makeModule("evmngr"));
        addModule(builder.makeModule("meteolcd"));
        addModule(builder.makeModule("meteo", getMod("log")));
        addModule(builder.makeModule("meteotsk", getMod("log"), getMod("meteo"), getMod("cfg"), getMod("meteolcd")));
        addModule(builder.makeModule("therm", getMod("log"), getMod("meteo"), getMod("cfg")));
        addModule(builder.makeModule("thermtsk", getMod("therm"), getMod("cfg")));
        addModule(builder.makeModule("hum", getMod("log"), getMod("meteo"), getMod("cfg")));
        addModule(builder.makeModule("humtsk", getMod("hum"), getMod("cfg")));
        addModule(builder.makeModule("light", getMod("cfg"), getMod("log")));
        addModule(builder.makeModule("vision", getMod("light"), getMod("log")));
        addModule(builder.makeModule("vistask", getMod("vision"), getMod("cfg")));
        addModule(builder.makeModule("secure", getMod("log"), getMod("ntf"), getMod("cfg"), getMod("light"), getMod("vision")));
        addModule(builder.makeModule("mih", getMod("log"), getMod("light"), getMod("cfg")));
        addModule(builder.makeModule("securetsk", getMod("mih"), getMod("cfg"), getMod("secure")));
        addModule(builder.makeModule("monitor", getMod("log"), getMod("ntf"), getMod("cfg")));
        addModule(builder.makeModule("montsk", getMod("monitor"), getMod("cfg")));
        addModule(builder.makeModule("ctrl", getMod("log"), getMod("cfg"), getMod("meteo"), getMod("meteotsk"), getMod("secure"), getMod("securetsk"), getMod("therm"), getMod("thermtsk"), getMod("mih"), getMod("light"), getMod("vision"), getMod("vistask"), getMod("monitor"), getMod("montsk"), getMod("hum"), getMod("humtsk"), getMod("evmngr")));
    }

    /**
     * Make builder objects
     * @param builder Web fabric
     */
    private void buildWeb(IBuilder builder) {
        addModule(builder.makeModule("websrv"));
        addModule(builder.makeModule("idxh"));
        addModule(builder.makeModule("ligh", getMod("log"), getMod("ctrl")));
        addModule(builder.makeModule("sech", getMod("log"), getMod("ctrl")));
        addModule(builder.makeModule("mihh", getMod("log"), getMod("ctrl")));
        addModule(builder.makeModule("thermh", getMod("log"), getMod("ctrl")));
        addModule(builder.makeModule("humh", getMod("log"), getMod("ctrl")));
        addModule(builder.makeModule("server", getMod("websrv"), getMod("idxh"), getMod("sech"), getMod("ligh"), getMod("mihh"), getMod("thermh"), getMod("humh")));
    }

    /**
     * Make telegram objects
     * @param builder Telegram fabric
     */
    private void buildTelegram(IBuilder builder) {
        addModule(builder.makeModule("mainm", getMod("cfg")));
        addModule(builder.makeModule("meteom", getMod("ctrl"), getMod("cfg")));
        addModule(builder.makeModule("camm", getMod("cfg"), getMod("ctrl"), getMod("log")));
        addModule(builder.makeModule("securem", getMod("ctrl"), getMod("log"), getMod("cfg")));
        addModule(builder.makeModule("meteosm", getMod("ctrl"), getMod("cfg"), getMod("log")));
        addModule(builder.makeModule("thermm", getMod("ctrl"), getMod("cfg")));
        addModule(builder.makeModule("thermcm", getMod("ctrl"), getMod("log"), getMod("cfg")));
        addModule(builder.makeModule("humm", getMod("ctrl"), getMod("cfg")));
        addModule(builder.makeModule("humcm", getMod("ctrl"), getMod("log"), getMod("cfg")));
        addModule(builder.makeModule("lightm", getMod("cfg")));
        addModule(builder.makeModule("mihm", getMod("ctrl"), getMod("log"), getMod("cfg")));
        addModule(builder.makeModule("lightsm", getMod("cfg"), getMod("ctrl")));
        addModule(builder.makeModule("tgbotapi"));
        addModule(builder.makeModule("tgbot", getMod("log"), getMod("cfg"), getMod("mainm"), getMod("meteom"), getMod("camm"), getMod("securem"), getMod("meteosm"), getMod("thermm"), getMod("thermcm"), getMod("mihm"), getMod("lightm"), getMod("lightsm"), getMod("humm"), getMod("humcm")));
    }

    /**
     * Make application object
     * @param builder Common fabric
     */
    private void buildApp(IBuilder builder) {
        addModule(builder.makeModule("app", getMod("log"), getMod("cfg"), getMod("tgbot"), getMod("tgbotapi"), getMod("server"), getMod("ctrl")));
    }

    /**
     * Add new module to application
     * @param module App module
     */
    private void addModule(IAppModule module) {
        modules.put(module.getModName(), module);
    }

    /**
     * Get application module
     * @param name Name of module
     * @return App module
     */
    private IAppModule getMod(String name) {
        return modules.get(name);
    }
}
