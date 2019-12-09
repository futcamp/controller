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
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import ru.futcamp.IAppModule;
import ru.futcamp.controller.IController;
import ru.futcamp.controller.subcontrollers.Events;
import ru.futcamp.controller.subcontrollers.modules.socket.SocketInfo;
import ru.futcamp.net.web.HttpResponse;
import ru.futcamp.utils.log.ILogger;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;

public class SocketHandler implements HttpHandler, IAppModule {
    private ILogger log;
    private IController ctrl;

    private String modName;

    public SocketHandler(String name, IAppModule... dep) {
        modName = name;
        this.log = (ILogger) dep[0];
        this.ctrl = (IController) dep[1];
    }

    @Override
    public void handle(HttpExchange ex) {
        String line;
        StringBuilder inBody = new StringBuilder();
        HttpResponse resp = new HttpResponse("socket", ex);
        String inIP = ex.getRemoteAddress().getAddress().toString().split("/")[1];

        int channel = 0;
        String event = null;

        System.out.println("DEVICE>>" + ex.getRequestURI().toString());

        try {
            List<NameValuePair> params = URLEncodedUtils.parse(new URI(ex.getRequestURI().toString()), Charset.forName("UTF-8"));

            for (NameValuePair param : params) {
                if (param.getName().equals("chan")) {
                    channel = Integer.parseInt(param.getValue());
                }
                if (param.getName().equals("event")) {
                    event = param.getValue();
                }
            }
        } catch (Exception e) {
            log.error("Fail to parse secure request: " + e.getMessage(), "SECUREHDL");
            try {
                resp.simpleResult(false);
            } catch (Exception exc) {
                log.error("Fail to send secure response: " + exc.getMessage(), "SECUREHDL");
            }
            return;
        }

        try {
            switch (event) {
                case "switch":
                    log.info("Switching socket \"" + inIP + "\"", "SOCKHDL");

                    SocketInfo info = ctrl.getMisc().getSocketInfo(inIP, channel);
                    Thread thread = new Thread(() -> {
                        try {
                            switch (info.getModule()) {
                                case "hum":
                                    ctrl.getMeteo().genHumEvent(info.getAlias(), Events.SWITCH_STATUS_EVENT);
                                    break;

                                case "therm":
                                    ctrl.getMeteo().genThermEvent(info.getAlias(), Events.SWITCH_STATUS_EVENT);
                                    break;
                            }
                        } catch (Exception e) {
                            log.error("Fail to switch socket: " + e.getMessage(), "SOCKHDL");
                        }
                    });
                    thread.start();

                    break;

                case "sync":
                    log.info("Syncing socket \"" + inIP + "\"", "SOCKHDL");
                    Thread thread2 = new Thread(() -> {
                        ctrl.getMisc().genSocketEvent(inIP, 0, Events.SYNC_EVENT);
                    });
                    thread2.start();
                    break;
            }
        } catch (Exception e) { e.printStackTrace();}









        try {
            resp.simpleResult(true);
        } catch (Exception exc) {
            log.error("Fail to send socket response: " + exc.getMessage(), "SOCKHDL");
        }
    }

    public String getModName() {
        return modName;
    }
}
