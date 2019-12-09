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

package ru.futcamp.net;

import ru.futcamp.IAppModule;
import ru.futcamp.IBuilder;
import ru.futcamp.net.web.HttpServer;
import ru.futcamp.net.web.handlers.*;
import ru.futcamp.net.web.server.WebServer;

public class WebBuilder implements IBuilder {
    /**
     * Make new web module
     * @param name Name of module
     * @param dep Dependencies
     * @return Module
     */
    public IAppModule makeModule(String name, IAppModule ...dep) {
        if (name.equals("websrv")) {
            return new WebServer(name, dep);
        } else if (name.equals("idxh")) {
            return new IndexHandler(name, dep);
        } else if (name.equals("sech")) {
            return new SecureHandler(name, dep);
        } else if (name.equals("server")) {
            return new HttpServer(name, dep);
        } else if (name.equals("ligh")) {
            return new LightHandler(name, dep);
        } else if (name.equals("sockh")) {
            return new SocketHandler(name, dep);
        } else {
            return null;
        }
    }
}
