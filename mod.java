package com.letssukitv.randomblockeffect;

import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod(RandomBlockEffectMod.MODID)
public class RandomBlockEffectMod {
    public static final String MODID = "randomblockeffect";
    private final Random random = new Random();
    private boolean challengeStarted = false;
    private boolean timerRunning = false;
    private int timerTicks = 0;

    public RandomBlockEffectMod() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!challengeStarted) return;

        Player player = event.getPlayer();
        if (player == null) return;

        Level world = player.level();
        MobEffect[] effects = {
            MobEffects.MOVEMENT_SPEED,
            MobEffects.SLOW_FALLING,
            MobEffects.DAMAGE_BOOST,
            MobEffects.REGENERATION,
            MobEffects.NIGHT_VISION,
            MobEffects.FIRE_RESISTANCE,
            MobEffects.WATER_BREATHING,
            MobEffects.JUMP,
            MobEffects.WEAKNESS,
            MobEffects.POISON,
            MobEffects.WITHER
        };

        MobEffect randomEffect = effects[random.nextInt(effects.length)];
        player.addEffect(new MobEffectInstance(randomEffect, 200, 1));

        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0f, 1.0f);

        for (int i = 0; i < 20; i++) {
            double offsetX = (random.nextDouble() - 0.5);
            double offsetY = random.nextDouble() + 0.5;
            double offsetZ = (random.nextDouble() - 0.5);
            world.addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() + offsetX, player.getY() + offsetY, player.getZ() + offsetZ, 0, 0.1, 0);
        }

        player.showTitle(Component.translatable(randomEffect.getDescriptionId()), Component.empty(), 10, 60, 10);
        player.sendSystemMessage(Component.literal("§aDu hast den Effekt bekommen: " + randomEffect.getDescriptionId()));
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!timerRunning) return;

        timerTicks++;

        int seconds = (timerTicks / 20) % 60;
        int minutes = (timerTicks / 20) / 60;
        String time = String.format("§eTimer: %02d:%02d", minutes, seconds);

        for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
            player.displayClientMessage(Component.literal(time), true);
        }
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
            Commands.literal("gamestart")
                .requires(source -> source.hasPermission(2))
                .executes(context -> {
                    challengeStarted = true;
                    timerRunning = true;
                    timerTicks = 0;

                    context.getSource().getServer().getPlayerList().broadcastSystemMessage(Component.literal("§aChallenge gestartet!"), false);
                    return 1;
                })
        );

        event.getDispatcher().register(
            Commands.literal("timer")
                .then(Commands.literal("pause")
                    .requires(source -> source.hasPermission(2))
                    .executes(context -> {
                        timerRunning = !timerRunning;
                        String msg = timerRunning ? "§aTimer fortgesetzt." : "§cTimer pausiert.";
                        context.getSource().getServer().getPlayerList().broadcastSystemMessage(Component.literal(msg), false);
                        return 1;
                    }))
        );

        event.getDispatcher().register(
            Commands.literal("challenge")
                .then(Commands.literal("reset")
                    .requires(source -> source.hasPermission(2))
                    .executes(context -> {
                        challengeStarted = false;
                        timerRunning = false;
                        timerTicks = 0;

                        for (ServerPlayer player : context.getSource().getServer().getPlayerList().getPlayers
::contentReference[oaicite:0]{index=0}
 
