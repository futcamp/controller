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

import ru.futcamp.net.web.handlers.IHandlersBuilder;
import ru.futcamp.net.web.server.IWebServer;

import java.io.IOException;

import static ru.futcamp.net.web.handlers.Handlers.*;

public class HttpServer implements IHttpServer {
    private IWebServer server;
    private IHandlersBuilder builder;

    public HttpServer(IWebServer server, IHandlersBuilder builder) {
        this.server = server;
        this.builder = builder;
    }

    /**
     * Prepare requests and server
     * @throws IOException If fail to init server
     */
    public void prepare(String api) throws IOException {
        server.init();
        server.addHandler("/", builder.makeHandler(INDEX_HDL));
        server.addHandler("/api/" + api + "/security", builder.makeHandler(SECURE_HDL));
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
}
