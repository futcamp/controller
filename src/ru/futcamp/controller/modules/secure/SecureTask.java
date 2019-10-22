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

import ru.futcamp.net.INotifier;
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

    private int counter = 0;

    public SecureTask(ILogger log, ISecurity secure, INotifier notify, IConfigs cfg) {
        this.log = log;
        this.secure = secure;
        this.notify = notify;
        this.cfg = cfg;
    }

    @Override
    public void run() {
        counter++;

        if (counter != cfg.getSecureCfg().getInterval())
            return;
        counter = 0;

        for (ISecureDevice device : secure.getDevices()) {
            if (secure.isStatus()) {
                if (device.isState()) {
                    if (!secure.isAlarm()) {
                        log.warning("Security sensor " + device.getName() + " detected!", "SECURETASK");
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
}
