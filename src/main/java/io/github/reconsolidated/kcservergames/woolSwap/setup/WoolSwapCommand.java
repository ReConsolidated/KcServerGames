package io.github.reconsolidated.kcservergames.woolSwap.setup;

import io.github.reconsolidated.kcservergames.commandManagement.CommandManager;
import io.github.reconsolidated.kcservergames.woolSwap.WoolSwapArenaService;

public class WoolSwapCommand extends CommandManager {
    public WoolSwapCommand(WoolSwapArenaService woolSwapArenaService) {
        super("woolswap", "Commands related to woolswap command");

        addSubCommand(new WoolSwapCreateCommand(woolSwapArenaService));
        addSubCommand(new WoolSwapLocCommand(woolSwapArenaService));
    }
}
