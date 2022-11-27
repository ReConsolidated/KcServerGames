package io.github.reconsolidated.kcservergames.woolSwap.setup;

import io.github.reconsolidated.kcservergames.commandManagement.InformException;
import io.github.reconsolidated.kcservergames.commandManagement.SubCommand;
import io.github.reconsolidated.kcservergames.woolSwap.WoolSwapArenaService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class WoolSwapSetRegionCommand implements SubCommand {
    private final WoolSwapArenaService woolSwapArenaService;

    public WoolSwapSetRegionCommand(WoolSwapArenaService woolSwapArenaService) {
        this.woolSwapArenaService = woolSwapArenaService;
    }

    @Override
    public String getName() {
        return "set_region";
    }

    @Override
    public String getDescription() {
        return "Set the region for a Wool Swap arena";
    }

    @Override
    public String getSyntax() {
        return "/wool_swap set_region <arena_name> <entire/play/wool/display>";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length == 3) {
            String arenaName = args[1];
            String regionType = args[2];
            List<String> allowedRegionTypes = List.of("entire", "play", "wool", "display");
            if (allowedRegionTypes.contains(regionType.toLowerCase())) {
                woolSwapArenaService.setRegion((Player) sender, arenaName, regionType);
                sender.sendMessage(ChatColor.GREEN + "Region %s set!".formatted(regionType));
            } else {
                throw new InformException("Invalid region type. Allowed types: %s".formatted(allowedRegionTypes));
            }
        }
    }

}
