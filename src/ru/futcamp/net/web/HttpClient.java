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

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {
    private String request;

    public HttpClient(String request) {
        this.request = request;
    }

    /**
     * Get request
     * @return Response
     * @throws Exception If fail to get request
     */
    private String getReq(int timeout) throws Exception {
        final URL url = new URL(request);
        final HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setConnectTimeout(timeout);

        try (final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            final StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            return content.toString();
        } catch (final Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    /**
     * Get request
     * @return Response
     * @throws Exception If fail to get request
     */
    public String getRequest(int timeout) throws Exception {
        String resp;

        for (int i = 0; i < 4; i++) {
            try {
                resp = getReq(timeout);
                return resp;
            } catch (Exception e) {
                if (i == 3)
                    throw new Exception(e.getMessage());
            }
            Thread.sleep(1000);
        }

        return "";
    }

    /**
     * Save image from url
     * @param destFile Path to image file on disk
     * @throws IOException If fail to get image
     */
    public void saveImage(String destFile) throws IOException {
        URL url = new URL(request);
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(destFile);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }

        is.close();
        os.close();
    }
}
