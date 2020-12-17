package ru.baronessdev.editor.data;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class MainData {

    // todo придумать что-нибудь получше

    @Getter
    private final HashMap<Player, Project> data = new HashMap<>();
}
