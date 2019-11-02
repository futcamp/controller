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

package ru.futcamp.controller.modules.secure;

import ru.futcamp.controller.modules.light.ILightControl;
import ru.futcamp.controller.modules.light.ILightDevice;
import ru.futcamp.net.INotifier;
import ru.futcamp.utils.TimeControl;
import ru.futcamp.utils.configs.IConfigs;
import ru.futcamp.utils.log.ILogger;

import java.util.TimerTask;

/**
 * Secure task
 */
public class SecureTask extends TimerTask {
    private ILogger log;
    private ISecurity secure;
    private INotifier notify;
    private IConfigs cfg;
    private IMainInHome mih;
    private ILightControl light;

    private int counter = 0;

    public SecureTask(ILogger log, ISecurity secure, INotifier notify, IConfigs cfg, IMainInHome mih, ILightControl light) {
        this.log = log;
        this.secure = secure;
        this.notify = notify;
        this.cfg = cfg;
        this.mih = mih;
        this.light = light;
    }

    @Override
    public void run() {
        counter++;

        if (counter != cfg.getSecureCfg().getInterval())
            return;
        counter = 0;

        processSecure();
        processMIH();
    }

    /**
     * Process Secure
     */
    private void processSecure() {
        for (ISecureDevice device : secure.getDevices()) {
            if (secure.isStatus()) {
                if (device.isState()) {
                    if (!secure.isAlarm()) {
                        log.warning("Security sensor " + device.getName() + " detected!", "SECURETASK");
                        /*
                         * Turn on all street lamps
                         */
                        for (ILightDevice lightDev : light.getDevicesGroup("street")) {
                            lightDev.setStatus(true);
                            try {
                                lightDev.syncStates();
                            } catch (Exception e) {
                                log.error("Fail to sync with light device \"" + lightDev.getName() + "\"", "SECURETASK");
                            }
                        }
                        /*
                         * Send notification to telegram
                         */
                        try {
                            notify.sendNotify("Внимание! Проникновение! Открыта: " + device.getAlias());
                        } catch (Exception e) {
                            log.error("Fail to send notify: " + e.getMessage(), "SECURETASK");
                        }
                        /*
                         * Save states to db
                         */
                        try {
                            secure.setAlarm(true);
                            secure.saveStates();
                            continue;
                        } catch (Exception e) {
                            log.error("Fail to save states to db: " + e.getMessage(), "SECURETASK");
                        }
                    }
                    secure.setAlarm(true);
                }
            } else {
                if (secure.isAlarm()) {
                    try {
                        secure.setAlarm(false);
                        secure.saveStates();
                    } catch (Exception e) {
                        log.error("Fail to save states to db: " + e.getMessage(), "SECURETASK");
                    }
                }
                secure.setAlarm(false);
                device.setState(false);
            }

            /*
             * Sync alarm state
             */
            try {
                device.syncSecureAlarm(secure.isAlarm());
            } catch (Exception e) {
                log.error("Fail to sync alarm state with device " + device.getName() + ": " +
                        e.getMessage(), "SECURETASK");
            }
        }
    }

    /**
     * Process Man In Home subsystem
     */
    private void processMIH() {
        int curTime = TimeControl.getCurHour();

        if (mih.isStatus()) {
            /*
             * Switch on street lamps
             */
            for (String lamp : cfg.getSecureCfg().getLamps()) {
                ILightDevice lightDev = light.getDevice(lamp);
                lightDev.setStatus(true);
            }

            if (curTime >= mih.getTimeOn() &&
                    curTime < mih.getTimeOff()) {

                mih.setLamp(true);
                /*
                 * Switch on radio
                 */
                if (curTime % 2 == 0) {
                    mih.setRadio(false);
                } else {
                    mih.setRadio(true);
                }
            } else {
                /*
                 * Switch off street lamps
                 */
                for (String lamp : cfg.getSecureCfg().getLamps()) {
                    ILightDevice lightDev = light.getDevice(lamp);
                    lightDev.setStatus(false);
                }

                mih.setLamp(false);
                mih.setRadio(false);
            }
        } else {
            mih.setLamp(false);
            mih.setRadio(false);
        }

        /*
         * Sync states with device
         */
        try {
            mih.syncStates();
        } catch (Exception e) {
            log.error("Fail to sync MIH states: " + e.getMessage(), "SECURETASK");
        }
    }
}
