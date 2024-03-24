package ru.cornivella.discord.commands.math;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MathCosAdapter extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("cos")) {
            double radian = event.getOption("radians").getAsDouble();

            event.reply("cos " + radian + " = " + Math.cos(radian)).queue();
        }
    }
}
