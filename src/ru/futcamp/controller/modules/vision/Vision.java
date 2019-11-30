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

package ru.futcamp.controller.modules.vision;

import com.sun.org.apache.bcel.internal.generic.RET;
import ru.futcamp.IAppModule;
import ru.futcamp.controller.modules.light.ILightControl;
import ru.futcamp.controller.modules.light.LightInfo;
import ru.futcamp.utils.log.ILogger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Web cameras control class
 */
public class Vision implements IVision, IAppModule {
    private Map<String, ICamDevice> devices = new HashMap<>();
    private String modName;

    private ILightControl light;
    private ILogger log;

    private static int RETRIES = 3;

    public Vision(String name, IAppModule ...dep) {
        this.modName = name;
        this.light = (ILightControl) dep[0];
        this.log = (ILogger) dep[1];
    }

    /**
     * Get photo from camera
     * @param alias Alias of camera
     * @param fileName Path to photo
     * @throws Exception If fail to get photo
     */
    public void getPhoto(String alias, String fileName) throws Exception {
        ICamDevice device = devices.get(alias);

        if (device == null) {
            throw new Exception("Device \"" + alias + "\" not found!");
        }

        device.savePhoto(fileName);
    }

    /**
     * Get photo from camera with light
     * @param alias Alias of camera
     * @param fileName Path to photo
     * @param isLight Switch on/off light
     * @throws Exception If fail to get photo
     */
    public void getPhoto(String alias, String fileName, boolean isLight) throws Exception {
        ICamDevice device = devices.get(alias);
        List<Boolean> states = new LinkedList<>();

        if (device == null) {
            throw new Exception("Cam device \"" + alias + "\" not found!");
        }

        if (isLight) {
            /*
             * Switch on lamps
             */
            for (String lamp : device.getLamps()) {
                LightInfo info = light.getLightInfo(lamp);

                if (info == null) {
                    throw new Exception("Light device \"" + lamp + "\" not found");
                }

                states.add(info.isStatus());
                light.setLightStatus(lamp, true);
            }
        }

        Thread.sleep(500);
        device.savePhoto(fileName);

        if (isLight) {
            /*
             * Restore lamps status
             */
            int i = 0;
            for (String lamp : device.getLamps()) {
                light.setLightStatus(lamp, states.get(i));
                i++;
            }
        }
    }

    /**
     * Add new camera device
     * @param cam Cam device
     */
    public void addCamera(ICamDevice cam) {
        this.devices.put(cam.getAlias(), cam);
    }

    /**
     * Warming cameras
     */
    public void warmCameras() {
        for (Map.Entry<String, ICamDevice> entry : devices.entrySet()) {
            ICamDevice device = entry.getValue();
            if (device.isWarm()) {
                log.info("Starting warming up camera \"" + device.getName() + "\"", "VISION");

                Exception ex = null;
                for (int i = 0; i < RETRIES; i++) {
                    try {
                        device.savePhoto("/tmp/warm.jpg");
                        break;
                    } catch (Exception e) {
                        ex = e;
                        try {
                            Thread.sleep(1000);
                        } catch (Exception ignored) {}
                    }
                }
                if (ex != null) {
                    log.error("Fail to warm up camera \"" + device.getAlias() + "\": " + ex.getMessage(), "VISION");
                }
            }
        }
    }

    @Override
    public String getModName() {
        return modName;
    }
}
