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

package ru.futcamp.controller.events;

import ru.futcamp.IAppModule;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Events observer
 */
public class EventManager implements IEventManager, IAppModule {
    private Map<Events, List<EventListener>> listeners = new HashMap<>();
    private String modName;

    public EventManager(String name, IAppModule ...dep) {
        this.modName = name;
    }

    /**
     * Add events listener
     * @param event Listen  event
     * @param listener Listener class
     */
    public void addListener(Events event, EventListener listener) {
        List<EventListener> list = listeners.get(event);

        if (list == null) {
            listeners.put(event, new LinkedList<>());
            list = listeners.get(event);
        }

        list.add(listener);
    }

    /**
     * Generate new event
     * @param event Event type
     * @param module Module name
     * @param ip Address of device
     * @param channel Device channel
     */
    public void genEvent(Events event, String module, String ip, int channel) {
        List<EventListener> lists = listeners.get(event);
        for (EventListener listener : lists) {
            listener.getEvent(event, module, ip, channel);
        }
    }

    public String getModName() {
        return modName;
    }
}
