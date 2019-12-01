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

package ru.futcamp.tgbot;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.futcamp.tgbot.menu.IMenu;
import ru.futcamp.tgbot.menu.LevelMenu;

public interface ITelegramBot {
    void setData(String key, String user);
    void addMenu(LevelMenu level, IMenu menu);
}
