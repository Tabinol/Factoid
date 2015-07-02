package me.tabinol.factoid.minecraft.sponge;

import me.tabinol.factoid.Factoid;
import me.tabinol.factoid.commands.OnCommand;
import me.tabinol.factoid.minecraft.Commands;
import me.tabinol.factoid.minecraft.FSenderInterface;
import me.tabinol.factoid.minecraft.bukkit.FSenderBukkit;

import org.bukkit.entity.Player;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

public class CommandsSpongeFactoid implements Commands, CommandExecutor {
	
	private final OnCommand onCommand;

	public CommandsSpongeFactoid(OnCommand onCommand) {
	    
		this.onCommand = onCommand;
    }

	@Override
    public CommandResult execute(CommandSource src, CommandContext args)
            throws CommandException {
	    
		FSenderInterface fSender;
		
		if(src instanceof Player) {
			fSender = Factoid.getServerCache().getPlayer(((Player) src).getUniqueId());
		} else {
			fSender = new FSenderBukkit(null);
		}

		onCommand.onCommand(fSender, "factoid", args.<String>getAll("string").toArray(new String[0]));
		
		return CommandResult.success();
    }
}