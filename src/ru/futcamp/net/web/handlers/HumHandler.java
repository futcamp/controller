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

import com.alibaba.fastjson.JSON;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.futcamp.IAppModule;
import ru.futcamp.controller.events.Events;
import ru.futcamp.controller.IController;
import ru.futcamp.net.web.HttpResponse;
import ru.futcamp.net.web.handlers.data.MIHData;
import ru.futcamp.utils.log.ILogger;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class HumHandler implements HttpHandler, IAppModule {
    private ILogger log;
    private IController ctrl;

    private String modName;

    public HumHandler(String name, IAppModule... dep) {
        modName = name;
        this.log = (ILogger) dep[0];
        this.ctrl = (IController) dep[1];
    }

    @Override
    public void handle(HttpExchange ex) {
        String line;
        StringBuilder inBody = new StringBuilder();
        HttpResponse resp = new HttpResponse("hum", ex);
        String inIP = ex.getRemoteAddress().getAddress().toString().split("/")[1];

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(ex.getRequestBody()));
            while ((line = reader.readLine()) != null) {
                inBody.append(line);
            }
            MIHData data = JSON.parseObject(inBody.toString(), MIHData.class);

            switch (data.getEvent()) {
                case "sync":
                    ctrl.genEvent(Events.SYNC_EVENT, "hum", inIP, 0);
                    break;
            }
        } catch (Exception e) {
            log.error("Fail to process hum event: " + e.getMessage(), "HUMHDL");
            try {
                resp.simpleResult(false);
            } catch (Exception exc) {
                log.error("Fail to send hum response: " + exc.getMessage(), "HUMHDL");
            }
        }

        try {
            resp.simpleResult(true);
        } catch (Exception exc) {
            log.error("Fail to send hum response: " + exc.getMessage(), "HUMHDL");
        }
    }

    public String getModName() {
        return modName;
    }
}
