package ru.baronessdev.free.editor.data;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.Inventory;

@Getter
@Setter
public class Project {

    private String name;
    private Inventory inventory;
    private String title;
}
