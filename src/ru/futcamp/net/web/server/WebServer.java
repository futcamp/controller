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

package ru.futcamp.net.web.server;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.futcamp.IAppModule;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Application web server
 */
public class WebServer implements IWebServer, IAppModule {
    private HttpServer server;

    private String modName;

    public WebServer(String name, IAppModule ...dep) {
        this.modName = name;
        try {
            server = HttpServer.create();
        } catch (Exception ignored) {}
    }

    /**
     * Add new request handler to server
     * @param path Web URI
     * @param handler Handler class
     */
    public void addHandler(String path, HttpHandler handler) {
        server.createContext(path, handler);
    }

    /**
     * Start web server
     * @param port Web server tcp port
     * @param queue Number of queue
     * @param threads Fixed number of threads
     * @throws IOException If fail to start server
     */
    public void start(int port, int queue, int threads) throws IOException {
        final ExecutorService executor = Executors.newFixedThreadPool(threads);
        server.bind(new InetSocketAddress(port), queue);
        server.setExecutor(executor);
        server.start();
    }

    public String getModName() {
        return modName;
    }
}
