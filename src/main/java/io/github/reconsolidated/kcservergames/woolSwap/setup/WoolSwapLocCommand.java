package io.github.reconsolidated.kcservergames.woolSwap.setup;

import io.github.reconsolidated.kcservergames.commandManagement.InformException;
import io.github.reconsolidated.kcservergames.commandManagement.SubCommand;
import io.github.reconsolidated.kcservergames.woolSwap.WoolSwapArenaService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WoolSwapLocCommand implements SubCommand {
    private final WoolSwapArenaService woolSwapArenaService;

    public WoolSwapLocCommand(WoolSwapArenaService woolSwapArenaService) {
        this.woolSwapArenaService = woolSwapArenaService;
    }

    @Override
    public String getName() {
        return "loc";
    }

    @Override
    public String getDescription() {
        return "Set location";
    }

    @Override
    public String getSyntax() {
        return "/woolswap loc <1 or 2>";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        String loc = args[1];
        try {
            int locInt = Integer.parseInt(loc);
            if (locInt == 1 || locInt == 2) {
                woolSwapArenaService.setLoc((Player) sender, locInt);
                sender.sendMessage(ChatColor.GREEN + "Location %d set!".formatted(locInt));
            } else {
                throw new InformException("Invalid loc number. Pick 1 or 2");
            }
        } catch (NumberFormatException e) {
            throw new InformException("Invalid number: %s".formatted(loc));
        }
    }
}
