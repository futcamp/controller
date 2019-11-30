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
    private HttpHandler idxHandler;
    private HttpHandler secHandler;
    private HttpHandler lightHandler;
    private HttpHandler mihHandler;
    private HttpHandler thermHandler;
    private HttpHandler humHandler;

    private String modName;

    public HttpServer(String name, IAppModule ...dep) {
        this.modName = name;
        this.server = (IWebServer) dep[0];
        this.idxHandler = (HttpHandler) dep[1];
        this.secHandler = (HttpHandler) dep[2];
        this.lightHandler = (HttpHandler) dep[3];
        this.mihHandler = (HttpHandler) dep[4];
        this.thermHandler = (HttpHandler) dep[5];
        this.humHandler = (HttpHandler) dep[6];
    }

    /**
     * Prepare requests and server
     * @throws IOException If fail to init server
     */
    public void prepare(String api) throws IOException {
        server.init();
        server.addHandler("/", idxHandler);
        server.addHandler("/api/" + api + "/security", secHandler);
        server.addHandler("/api/" + api + "/light", lightHandler);
        server.addHandler("/api/" + api + "/mih", mihHandler);
        server.addHandler("/api/" + api + "/therm", thermHandler);
        server.addHandler("/api/" + api + "/hum", humHandler);
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
