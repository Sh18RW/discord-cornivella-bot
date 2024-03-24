package ru.cornivella.discord.math;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MathExecutor {
    public static void cosCommand(MessageReceivedEvent event, String[] command) {
        event.getMessage().reply("Command haven't been released.").queue();
    }

    public static void sumCommand(MessageReceivedEvent event, String[] command) {
        if (command.length == 3) {
            try {
                float first = Float.parseFloat(command[1]);
                float second = Float.parseFloat(command[2]);

                event.getMessage().reply(first + " + " + second + " = " + (first + second)).queue();;
            } catch (NumberFormatException e) {
                event.getMessage().reply("Can't parse arguments correctly! " + e.getMessage()).queue();;
            }
        } else {
            event.getMessage().reply("Wrong argument count!").queue();
        }
    }
}
