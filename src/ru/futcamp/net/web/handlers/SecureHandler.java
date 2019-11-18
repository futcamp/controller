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
import ru.futcamp.net.web.HttpResponse;
import ru.futcamp.utils.log.ILogger;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;

public class SecureHandler implements HttpHandler, IAppModule {
    private ILogger log;
    private IController ctrl;

    private String modName;

    public SecureHandler(String name, IAppModule... dep) {
        modName = name;
        this.log = (ILogger) dep[0];
        this.ctrl = (IController) dep[1];
    }

    @Override
    public void handle(HttpExchange ex) {
        int channel = 0;
        HttpResponse resp = new HttpResponse("security", ex);
        String inIP = ex.getRemoteAddress().getAddress().toString().split("/")[1];

        try {
            List<NameValuePair> params = URLEncodedUtils.parse(new URI(ex.getRequestURI().toString()), Charset.forName("UTF-8"));

            for (NameValuePair param : params) {
                if (param.getName().equals("chan")) {
                    channel = Integer.parseInt(param.getValue());
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

        ctrl.newSecureAction(inIP, channel);

        try {
            resp.simpleResult(true);
        } catch (Exception exc) {
            log.error("Fail to send secure response: " + exc.getMessage(), "SECUREHDL");
        }
    }

    public String getModName() {
        return modName;
    }
}
