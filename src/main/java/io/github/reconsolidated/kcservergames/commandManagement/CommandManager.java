package io.github.reconsolidated.kcservergames.commandManagement;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager implements SubCommand, CommandExecutor, TabCompleter {
    protected final String name;
    protected final String description;
    protected final Map<String, SubCommand> subcommands = new HashMap<>();

    public CommandManager(String name, String description){
        this.name = name;
        this.description = description;
    }

    public void addSubCommand(SubCommand subcommand){
        subcommands.put(subcommand.getName(), subcommand);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        try {
            perform(sender, args);
        } catch (NoCommandException e) {
            sender.sendMessage(getSyntax());
        } catch (InformException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1){
            List<String> cmds = new ArrayList<>();
            for (SubCommand subcommand : subcommands.values()){
                cmds.add(subcommand.getName());
            }
            return cmds;
        } else {
            SubCommand subcommand = subcommands.get(args[0]);
            if (subcommand != null){
                return subcommand.onTabComplete(sender, command, alias, args);
            }
        }
        return null;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getSyntax() {
        StringBuilder bd = new StringBuilder(ChatColor.GOLD + "--------------------------------\n");
        for (SubCommand subcommand : subcommands.values()) {
            bd.append(ChatColor.AQUA).append(subcommand.getSyntax()).append(ChatColor.WHITE).append(" - ")
                    .append(subcommand.getDescription()).append("\n");
        }
        bd.append(ChatColor.GOLD).append("--------------------------------\n");
        return bd.toString();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length > 0){
            String commandName = args[0];
            if (subcommands.containsKey(commandName)){
                try {
                    subcommands.get(commandName).perform(sender, args);
                } catch (IndexOutOfBoundsException e) {
                    throw new InformException("Incorrect number of arguments");
                }
            } else {
                throw new NoCommandException();
            }
        }else {
            sender.sendMessage(getSyntax());
        }
    }


}

