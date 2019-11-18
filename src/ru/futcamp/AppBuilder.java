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

import ru.futcamp.controller.ControllerBuilder;
import ru.futcamp.net.WebBuilder;
import ru.futcamp.tgbot.TgBuilder;

/**
 * Make builder for one of main module
 */
public class AppBuilder implements IAppBuilder {
    /**
     * Make app builder
     * @return Builder
     */
    public static IAppBuilder create() {
        return new AppBuilder();
    }

    /**
     * Make concrete module builder
     * @param name Name of app module
     * @return Builder
     */
    public IBuilder makeBuilder(String name) {
        switch (name) {
            case "common":
                return new CommonBuilder();
            case "telegram":
                return new TgBuilder();
            case "web":
                return new WebBuilder();
            case "controller":
                return new ControllerBuilder();
            default:
                return null;
        }
    }
}
