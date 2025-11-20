package de.fabiexe.paperui;

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityTeleport;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import de.fabiexe.paperui.property.MutableProperty;
import de.fabiexe.paperui.property.SpatialUIProperty;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Interaction;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpatialUI {
    protected final Player audience;
    private final PacketEventsAPI<?> packetEventsAPI;
    private final Location location;
    private final BlockFace direction;
    private Runnable content = () -> {};
    private boolean shown = false;
    private final List<SpatialText> texts = new CopyOnWriteArrayList<>();
    private final List<SpatialButton> buttons = new CopyOnWriteArrayList<>();
    private final List<Display.TextDisplay> textDisplays = new CopyOnWriteArrayList<>();
    private final List<Interaction> interactions = new CopyOnWriteArrayList<>();

    public SpatialUI(@NotNull Player audience,
                     @NotNull PacketEventsAPI<?> packetEventsAPI,
                     @NotNull Location location,
                     @NotNull BlockFace direction) {
        this.audience = audience;
        this.packetEventsAPI = packetEventsAPI;
        packetEventsAPI.getEventManager().registerListener(new InteractionListener(), PacketListenerPriority.NORMAL);
        this.location = location;
        this.direction = direction;
    }

    public void show() {
        if (shown) {
            return;
        }
        render();
        shown = true;
    }

    public void hide() {
        if (!shown) {
            return;
        }

        // Remove text displays
        for (Display.TextDisplay textDisplay : textDisplays) {
            packetEventsAPI.getPlayerManager().sendPacket(audience, new WrapperPlayServerDestroyEntities(
                    textDisplay.getId()));
        }
        textDisplays.clear();

        // Remove interactions
        for (Interaction interaction : interactions) {
            packetEventsAPI.getPlayerManager().sendPacket(audience, new WrapperPlayServerDestroyEntities(
                    interaction.getId()));
        }
        interactions.clear();
        shown = false;
    }

    public void render() {
        texts.clear();
        buttons.clear();
        content.run();

        // Texts
        int textDisplayIndex = 0;
        for (SpatialText text : texts) {
            Vector offset = text.position.clone().add(new Vector(0, 0, 0.001));
            offset = new Vector(
                    offset.getX() * direction.getModZ() + offset.getZ() * direction.getModX(),
                    offset.getY(),
                    -offset.getX() * direction.getModX() + offset.getZ() * direction.getModZ()
            );
            offset.add(new Vector(
                    0,
                    switch (text.verticalAlignment) {
                        case TOP -> -0.25;
                        case CENTER -> -0.125;
                        case BOTTOM -> 0;
                    },
                    0));
            Location textLocation = location.clone().setDirection(direction.getDirection()).add(offset);

            TextDisplay textDisplay = (TextDisplay) prepareTextDisplay(textDisplayIndex, textLocation).getBukkitEntity();
            boolean changed = false;
            if (!textDisplay.text().equals(text.text)) {
                textDisplay.text(text.text);
                changed = true;
            }
            if (textDisplay.getAlignment() != text.textAlignment) {
                textDisplay.setAlignment(text.textAlignment);
                changed = true;
            }
            Integer backgroundColor = textDisplay.getBackgroundColor() != null ? textDisplay.getBackgroundColor().asARGB() : null;
            if (!Objects.equals(backgroundColor, text.backgroundColor)) {
                if (text.backgroundColor == null) {
                    textDisplay.setBackgroundColor(null);
                } else {
                    textDisplay.setBackgroundColor(Color.fromRGB(text.backgroundColor));
                }
                changed = true;
            }
            if (changed) {
                packetEventsAPI.getPlayerManager().sendPacket(audience, new WrapperPlayServerEntityMetadata(
                        textDisplay.getEntityId(),
                        SpigotReflectionUtil.getEntityMetadata(textDisplay)));
            }

            textDisplayIndex++;
        }

        // Buttons
        int interactionIndex = 0;
        for (SpatialButton button : buttons) {
            Vector offset = button.position.clone().add(new Vector(0, 0, button.width / -2 + 0.001));
            offset = new Vector(
                    offset.getX() * direction.getModZ() + offset.getZ() * direction.getModX(),
                    offset.getY(),
                    -offset.getX() * direction.getModX() + offset.getZ() * direction.getModZ()
            );
            Location location = this.location.clone().setDirection(direction.getDirection()).add(offset);

            Interaction interaction = prepareInteraction(interactionIndex, location);
            boolean changed = false;
            if (interaction.getWidth() != button.width) {
                interaction.setWidth((float) button.width);
                changed = true;
            }
            if (interaction.getHeight() != button.height) {
                interaction.setHeight((float) button.height);
                changed = true;
            }
            if (changed) {
                packetEventsAPI.getPlayerManager().sendPacket(audience, new WrapperPlayServerEntityMetadata(
                        interaction.getId(),
                        SpigotReflectionUtil.getEntityMetadata(interaction.getBukkitEntity())));
            }

            interactionIndex++;
        }

        // Remove unused text displays
        while (textDisplayIndex < textDisplays.size()) {
            Display.TextDisplay textDisplay = textDisplays.remove(textDisplayIndex);
            packetEventsAPI.getPlayerManager().sendPacket(audience, new WrapperPlayServerDestroyEntities(
                    textDisplay.getId()));
        }

        // Remove unused interactions
        while (interactionIndex < interactions.size()) {
            Interaction interaction = interactions.remove(interactionIndex);
            packetEventsAPI.getPlayerManager().sendPacket(audience, new WrapperPlayServerDestroyEntities(
                    interaction.getId()));
        }
    }

    private Display.TextDisplay prepareTextDisplay(int i, Location location) {
        if (i < textDisplays.size()) {
            Display.TextDisplay textDisplay = textDisplays.get(i);
            textDisplay.setPos(location.getX(), location.getY(), location.getZ());
            packetEventsAPI.getPlayerManager().sendPacket(audience, new WrapperPlayServerEntityTeleport(
                    textDisplay.getId(),
                    SpigotConversionUtil.fromBukkitLocation(location),
                    false));
            return textDisplays.get(i);
        } else {
            ServerLevel level = ((CraftWorld) location.getWorld()).getHandle();
            Display.TextDisplay textDisplay = new Display.TextDisplay(EntityType.TEXT_DISPLAY, level);
            textDisplays.add(textDisplay);
            packetEventsAPI.getPlayerManager().sendPacket(audience, new WrapperPlayServerSpawnEntity(
                    textDisplay.getId(),
                    textDisplay.getUUID(),
                    EntityTypes.TEXT_DISPLAY,
                    SpigotConversionUtil.fromBukkitLocation(location),
                    0,
                    0,
                    new Vector3d()));
            return textDisplay;
        }
    }

    private Interaction prepareInteraction(int i, Location location) {
        if (i < interactions.size()) {
            Interaction interaction = interactions.get(i);
            interaction.setPos(location.getX(), location.getY(), location.getZ());
            interaction.setRot(location.getYaw(), location.getPitch());
            packetEventsAPI.getPlayerManager().sendPacket(audience, new WrapperPlayServerEntityTeleport(
                    interaction.getId(),
                    SpigotConversionUtil.fromBukkitLocation(location),
                    false));
            return interactions.get(i);
        } else {
            ServerLevel level = ((CraftWorld) location.getWorld()).getHandle();
            Interaction interaction = new Interaction(EntityType.INTERACTION, level);
            interactions.add(interaction);
            packetEventsAPI.getPlayerManager().sendPacket(audience, new WrapperPlayServerSpawnEntity(
                    interaction.getId(),
                    interaction.getUUID(),
                    EntityTypes.INTERACTION,
                    SpigotConversionUtil.fromBukkitLocation(location),
                    0,
                    0,
                    new Vector3d()));
            return interaction;
        }
    }

    public @NotNull Runnable content() {
        return content;
    }

    public void content(@NotNull Runnable content) {
        this.content = content;
    }

    public <T> MutableProperty<T> property(T value) {
        return new SpatialUIProperty<>(this, value);
    }

    public <T> MutableProperty<T> property() {
        return property(null);
    }

    private Component miniMessage(String text) {
        return MiniMessage.miniMessage().deserialize(text);
    }

    public @NotNull SpatialText text(double x, double y, @NotNull Component text, @Nullable Integer backgroundColor) {
        SpatialText spatialText = new SpatialText(
                new Vector(x, y, 0),
                VerticalAlignment.BOTTOM,
                text,
                TextDisplay.TextAlignment.CENTER,
                backgroundColor);
        texts.add(spatialText);
        return spatialText;
    }

    public @NotNull SpatialText text(double x, double y, @NotNull String text, @Nullable Integer backgroundColor) {
        return text(x, y, miniMessage(text), backgroundColor);
    }

    public @NotNull SpatialText text(double x, double y, @NotNull Component text) {
        return text(x, y, text, null);
    }

    public @NotNull SpatialText text(double x, double y, @NotNull String text) {
        return text(x, y, miniMessage(text), null);
    }

    public @NotNull SpatialButton button(double x, double y, double width, double height, @NotNull Runnable onClick) {
        SpatialButton spatialButton = new SpatialButton(
                new Vector(x, y, 0),
                width,
                height,
                onClick);
        buttons.add(spatialButton);
        return spatialButton;
    }

    private class InteractionListener implements PacketListener {
        @Override
        public void onPacketReceive(@NotNull PacketReceiveEvent event) {
            if (!shown) {
                return;
            }
            if (event.<Player>getPlayer() != audience) {
                return;
            }
            if (event.getPacketType() != PacketType.Play.Client.INTERACT_ENTITY) {
                return;
            }

            WrapperPlayClientInteractEntity packet = new WrapperPlayClientInteractEntity(event);
            if (packet.getAction() != WrapperPlayClientInteractEntity.InteractAction.INTERACT) {
                return;
            }

            int entityId = packet.getEntityId();
            for (int i = 0; i < interactions.size(); i++) {
                if (interactions.get(i).getId() == entityId) {
                    buttons.get(i).onClick.run();
                    audience.swingMainHand();
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    public static enum VerticalAlignment {
        TOP, CENTER, BOTTOM
    }
}
