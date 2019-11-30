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

import ru.futcamp.net.notifier.Notifier;
import ru.futcamp.utils.configs.Configs;
import ru.futcamp.utils.log.Logger;

public class CommonBuilder implements IBuilder {
    /**
     * Make app common module
     * @param name Name of module
     * @param dep Dependencies
     * @return Module
     */
    public IAppModule makeModule(String name, IAppModule ...dep) {
        switch (name) {
            case "log":
                return new Logger(name, dep);
            case "cfg":
                return new Configs(name, dep);
            case "ntf":
                return new Notifier(name, dep);
            case "app":
                return new Application(name, dep);
            default:
                return null;
        }
    }
}
