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

package ru.futcamp.utils.configs.settings;

public class TelegramSettings {
    private String key;
    private String login;
    private String[] users;
    private String[] chats;
    private TelegramMenuSettings menu;
    private TelegramCamGroupSettings[] camgroups;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String[] getUsers() {
        return users;
    }

    public void setUsers(String[] users) {
        this.users = users;
    }

    public TelegramCamGroupSettings[] getCamgroups() {
        return camgroups;
    }

    public void setCamgroups(TelegramCamGroupSettings[] camgroups) {
        this.camgroups = camgroups;
    }

    public String[] getChats() {
        return chats;
    }

    public void setChats(String[] chats) {
        this.chats = chats;
    }

    public TelegramMenuSettings getMenu() {
        return menu;
    }

    public void setMenu(TelegramMenuSettings menu) {
        this.menu = menu;
    }
}
