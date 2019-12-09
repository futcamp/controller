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

package ru.futcamp.net.web.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.futcamp.IAppModule;

/**
 * Index request handler
 */
public class IndexHandler implements HttpHandler, IAppModule {
    private String modName;

    public IndexHandler(String name, IAppModule ...dep) {
        modName = name;
    }

    @Override
    public void handle(HttpExchange ex) {
    }

    @Override
    public String getModName() {
        return modName;
    }
}
