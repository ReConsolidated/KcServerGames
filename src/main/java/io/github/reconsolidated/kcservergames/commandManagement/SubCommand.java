package io.github.reconsolidated.kcservergames.commandManagement;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface SubCommand {

    //name of the subcommand ex. /prank <subcommand> <-- that
    String getName();

    //ex. "This is a subcommand that let's a shark eat someone"
    String getDescription();

    //How to use command ex. /prank freeze <player>
    String getSyntax();

    //code for the subcommand
    void perform(CommandSender sender, String[] args);

    default String getPermission() {
        return null;
    }

    default List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
