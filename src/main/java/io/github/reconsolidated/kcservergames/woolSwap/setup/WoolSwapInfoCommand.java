package io.github.reconsolidated.kcservergames.woolSwap.setup;

import io.github.reconsolidated.kcservergames.commandManagement.SubCommand;
import io.github.reconsolidated.kcservergames.woolSwap.WoolSwapArena;
import io.github.reconsolidated.kcservergames.woolSwap.WoolSwapArenaService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class WoolSwapInfoCommand implements SubCommand {
    private final WoolSwapArenaService woolSwapArenaService;

    public WoolSwapInfoCommand(WoolSwapArenaService woolSwapArenaService) {
        this.woolSwapArenaService = woolSwapArenaService;
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "Get info about a woolswap arena";
    }

    @Override
    public String getSyntax() {
        return "/woolswap info <name>";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        String arenaName = args[1];
        WoolSwapArena arena = woolSwapArenaService.getArena(arenaName);
        if (arena == null) {
            sender.sendMessage(ChatColor.RED + "Arena %s does not exist!".formatted(arenaName));
            return;
        }
        sender.sendMessage(ChatColor.GREEN + "Arena %s:".formatted(arenaName));
        sender.sendMessage(ChatColor.AQUA + "  Entire region:" + ChatColor.GRAY + arena.getEntireRegion());
        sender.sendMessage(ChatColor.AQUA + "  Play region:" + ChatColor.GRAY + arena.getPlayRegion());
        sender.sendMessage(ChatColor.AQUA + "  Wool region:" + ChatColor.GRAY + arena.getWoolRegion());
        sender.sendMessage(ChatColor.AQUA + "  Min players:" + ChatColor.GRAY + arena.getMinPlayers());
        sender.sendMessage(ChatColor.AQUA + "  Is ready to play?:" + ChatColor.GRAY + arena.isReady());
        sender.sendMessage(ChatColor.AQUA + "  State:" + ChatColor.GRAY + arena.getState());
    }
}
