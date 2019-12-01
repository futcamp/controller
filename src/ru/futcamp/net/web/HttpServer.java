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

package ru.futcamp.net.web;

import com.sun.net.httpserver.HttpHandler;
import ru.futcamp.IAppModule;
import ru.futcamp.net.web.server.IWebServer;

import java.io.IOException;


public class HttpServer implements IHttpServer, IAppModule {
    private IWebServer server;

    private String modName;
    private String apiVer;

    public HttpServer(String name, IAppModule ...dep) {
        this.modName = name;
        this.server = (IWebServer) dep[0];
    }

    /**
     * Set api version
     * @throws IOException If fail to init server
     */
    public void setAPI(String api) throws IOException {
        this.apiVer = api;
    }

    /**
     * Add HTTP handler
     * @param name Name of handler
     * @param handler Handler object
     */
    public void addHandler(String name, HttpHandler handler) {
        if (name.equals("index")) {
            server.addHandler("/", handler);
        } else {
            server.addHandler("/api/" + apiVer + "/" + name, handler);
        }
    }

    /**
     * Start web server
     * @param port Web server tcp port
     * @param queue Number of queue
     * @param threads Fixed number of threads
     * @throws IOException If fail to start server
     */
    public void start(int port, int queue, int threads) throws IOException {
        server.start(port, queue, threads);
    }

    public String getModName() {
        return modName;
    }
}
