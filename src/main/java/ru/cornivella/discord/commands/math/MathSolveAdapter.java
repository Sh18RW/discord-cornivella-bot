package ru.cornivella.discord.commands.math;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import ru.cornivella.discord.math.ArithmeticErrorException;
import ru.cornivella.discord.math.Solver;

public class MathSolveAdapter extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("solve")) {
            String expression = event.getOption("expression").getAsString();
            event.deferReply();

            try {
                double result = Solver.solve(expression);
                event.reply(expression + " = " + result).queue();
            } catch (ArithmeticErrorException e) {
                event.reply("Can't solve '" + expression + "'. Error: " + e.getMessage()).queue();
            }
        }
    }
}
