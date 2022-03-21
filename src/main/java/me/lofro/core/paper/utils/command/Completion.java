package me.lofro.core.paper.utils.command;

import co.aikar.commands.PaperCommandManager;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Completion {

    public static void addCommandCompletion(PaperCommandManager commandManager, String id, String[] array) {
        commandManager.getCommandCompletions().registerCompletion(id, c -> ImmutableList.copyOf(array));
    }

    public static void addCommandCompletion(PaperCommandManager commandManager, String id, Collection<String> collection) {
        commandManager.getCommandCompletions().registerCompletion(id, c -> collection);
    }

    public static void addAll(Object[] array, ArrayList<String> completer) {
        Arrays.asList(array).forEach(val -> completer.add(val.toString()));
    }

}
