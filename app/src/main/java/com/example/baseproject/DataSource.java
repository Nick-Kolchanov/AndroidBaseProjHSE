package com.example.baseproject;

import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataSource {

    public static List<GroupEntry> GetHseGroups() {
        return Arrays.asList(
                new GroupEntry("Бизнес информатика", Year.parse("2021"), 2),
                new GroupEntry("Программная инженерия", Year.parse("2021"), 3),
                new GroupEntry("Экономика", Year.parse("2021"), 2),
                new GroupEntry("Управление бизнесом", Year.parse("2021"), 3),
                new GroupEntry("История", Year.parse("2021"), 1),
                new GroupEntry("Юриспруденция", Year.parse("2021"), 1),
                new GroupEntry("Иностранные языки", Year.parse("2021"), 3));
    }

}

