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

package ru.futcamp.controller.subcontrollers.modules.vision.mod;

import ru.futcamp.net.web.HttpClient;

/**
 * Camera communication
 */
public class CamModule {
    private String ip;
    private int channel;

    public CamModule(String ip, int chan) {
        this.ip = ip;
        this.channel = chan;
    }

    /**
     * Save photo from web response
     * @param path Path to photo
     * @throws Exception If fail to get photo
     */
    public void savePhoto(String path) throws Exception {
        HttpClient client = new HttpClient("http://" + ip + "/camera?dev=" + channel);
        client.saveImage(path);
    }
}
