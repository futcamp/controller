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

import com.sun.net.httpserver.HttpExchange;

import java.io.OutputStream;

/**
 * Http response for web server
 */
public class HttpResponse {
    private String module;
    private HttpExchange ex;

    public HttpResponse(String module, HttpExchange ex) {
        this.module = module;
        this.ex = ex;
    }

    /**
     * Simple http response
     * @throws Exception If fail to send response
     */
    public void simpleResult(boolean isOk) throws Exception {
        String response = "{\"module\":\"" + module + "\",\"result\":" + isOk + "}";

        if (isOk)
            ex.sendResponseHeaders(200, response.length());
        else
            ex.sendResponseHeaders(403, response.length());
        ex.setAttribute("Content-Type", "application/json");

        OutputStream os = ex.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
