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

package ru.futcamp.utils.configs.settings.vision;

public class VisionSettings {
    private VisionTimersSettings timers;
    private VisionDeviceSettings[] devices;

    public VisionDeviceSettings[] getDevices() {
        return devices;
    }

    public void setDevices(VisionDeviceSettings[] devices) {
        this.devices = devices;
    }

    public VisionTimersSettings getTimers() {
        return timers;
    }

    public void setTimers(VisionTimersSettings timers) {
        this.timers = timers;
    }
}
