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
import ru.futcamp.controller.IController;
import ru.futcamp.net.web.HttpResponse;
import ru.futcamp.utils.log.ILogger;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;

public class SecureHandler implements HttpHandler {
    private ILogger log;
    private IController ctrl;

    SecureHandler(ILogger log, IController ctrl) {
        this.log = log;
        this.ctrl = ctrl;
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

        ctrl.setSecureState(inIP, channel, true);

        try {
            resp.simpleResult(true);
        } catch (Exception exc) {
            log.error("Fail to send secure response: " + exc.getMessage(), "SECUREHDL");
        }
    }
}
