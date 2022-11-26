package io.github.reconsolidated.kcservergames.woolSwap.setup;

import io.github.reconsolidated.kcservergames.commandManagement.SubCommand;
import io.github.reconsolidated.kcservergames.woolSwap.WoolSwapArenaService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WoolSwapCreateCommand implements SubCommand {
    private final WoolSwapArenaService woolSwapArenaService;

    public WoolSwapCreateCommand(WoolSwapArenaService woolSwapArenaService) {
        this.woolSwapArenaService = woolSwapArenaService;
    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getDescription() {
        return "Create a woolswap arena";
    }

    @Override
    public String getSyntax() {
        return "/woolswap create <name>";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            String name = args[1];
            woolSwapArenaService.createArena(name);
            player.sendMessage(ChatColor.GREEN + "Arena %s created!".formatted(name));
        }
    }
}
