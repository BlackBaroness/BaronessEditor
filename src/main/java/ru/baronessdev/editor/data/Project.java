package ru.baronessdev.editor.data;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.Inventory;

public class Project {

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private Inventory inventory;

    @Getter
    @Setter
    private String title;
}
