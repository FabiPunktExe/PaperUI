package de.fabiexe.paperui;

import de.fabiexe.paperui.property.*;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DialogUI<T extends Audience> {
    protected final T audience;
    private Component title = Component.empty();
    private final List<Property<?>> properties = new ArrayList<>();
    private final List<Button> buttons = new ArrayList<>();
    private final List<Button> actionButtons = new ArrayList<>();

    public DialogUI(@NotNull T audience) {
        this.audience = audience;
    }

    public void open() {
        List<DialogInput> inputs = new ArrayList<>();
        for (Property<?> property : properties) {
            property.populate(inputs);
        }

        DialogType type;
        if (buttons.isEmpty()) {
            type = switch (actionButtons.size()) {
                case 0 -> DialogType.notice();
                case 1 -> DialogType.notice(createButton(actionButtons.getFirst()));
                default -> DialogType.confirmation(
                        createButton(actionButtons.getFirst()),
                        createButton(actionButtons.get(1)));
            };
        } else {
            type = DialogType.multiAction(buttons.stream().map(this::createButton).toList()).build();
        }

        audience.showDialog(Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(title)
                        .inputs(inputs)
                        .build())
                .type(type)));
    }

    private ActionButton createButton(Button button) {
        ActionButton.Builder builder = ActionButton.builder(button.label());
        if (button.action() != null) {
            builder.action(DialogAction.customClick((response, ignored) -> {
                for (Property<?> property : properties) {
                    property.read(response);
                }
                button.action().run();
            }, ClickCallback.Options.builder().uses(1).build()));
        }
        return builder.build();
    }

    private Component miniMessage(String text) {
        return MiniMessage.miniMessage().deserialize(text);
    }

    public void title(@NotNull Component title) {
        this.title = title;
    }

    public void title(@NotNull String title) {
        this.title = miniMessage(title);
    }

    public <P> Property<P> property(@NotNull Property<P> property) {
        properties.add(property);
        return property;
    }

    private String propertyKey() {
        return "property_" + properties.size();
    }

    public Property<Boolean> booleanProperty(@NotNull Component label) {
        return property(new BooleanProperty(propertyKey(), label, null));
    }

    public Property<Boolean> booleanProperty(@NotNull String label) {
        return booleanProperty(miniMessage(label));
    }

    public Property<Boolean> booleanProperty(@NotNull Component label, boolean value) {
        return property(new BooleanProperty(propertyKey(), label, value));
    }

    public Property<Boolean> booleanProperty(@NotNull String label, boolean value) {
        return booleanProperty(miniMessage(label), value);
    }

    public Property<Component> componentProperty(@NotNull Component label, int maxLines) {
        return property(new ComponentProperty(propertyKey(), label, 1, null));
    }

    public Property<Component> componentProperty(@NotNull String label, int maxLines) {
        return componentProperty(miniMessage(label), maxLines);
    }

    public Property<Component> componentProperty(@NotNull Component label) {
        return componentProperty(label, 1);
    }

    public Property<Component> componentProperty(@NotNull String label) {
        return componentProperty(miniMessage(label));
    }

    public Property<Component> componentProperty(@NotNull Component label, int maxLines, @Nullable Component value) {
        return property(new ComponentProperty(propertyKey(), label, maxLines, value));
    }

    public Property<Component> componentProperty(@NotNull String label, int maxLines, @Nullable Component value) {
        return componentProperty(miniMessage(label), maxLines, value);
    }

    public Property<Component> componentProperty(@NotNull Component label, @Nullable Component value) {
        return property(new ComponentProperty(propertyKey(), label, 1, value));
    }

    public Property<Component> componentProperty(@NotNull String label, @Nullable Component value) {
        return componentProperty(miniMessage(label), value);
    }

    public Property<Double> doubleProperty(@NotNull Component label, double min, double max) {
        return property(new DoubleProperty(propertyKey(), label, min, max, null));
    }

    public Property<Double> doubleProperty(@NotNull String label, double min, double max) {
        return doubleProperty(miniMessage(label), min, max);
    }

    public Property<Double> doubleProperty(@NotNull Component label, double min, double max, double value) {
        return property(new DoubleProperty(propertyKey(), label, min, max, value));
    }

    public Property<Double> doubleProperty(@NotNull String label, double min, double max, double value) {
        return doubleProperty(miniMessage(label), min, max, value);
    }

    public <E> Property<E> enumProperty(@NotNull Component label,
                                        @NotNull Function<E, Component> nameFunction,
                                        @NotNull E[] values) {
        return property(new EnumProperty<>(propertyKey(), label, values, nameFunction, null));
    }

    public <E> Property<E> enumProperty(@NotNull String label,
                                        @NotNull Function<E, Component> nameFunction,
                                        @NotNull E[] values) {
        return enumProperty(miniMessage(label), nameFunction, values);
    }

    public <E> Property<E> enumProperty(@NotNull Component label,
                                        @NotNull Function<E, Component> nameFunction,
                                        @NotNull E[] values,
                                        @NotNull E value) {
        return property(new EnumProperty<>(propertyKey(), label, values, nameFunction, value));
    }

    public <E> Property<E> enumProperty(@NotNull String label,
                                        @NotNull Function<E, Component> nameFunction,
                                        @NotNull E[] values,
                                        @NotNull E value) {
        return enumProperty(miniMessage(label), nameFunction, values, value);
    }

    @SuppressWarnings("unchecked")
    private <E> E[] values(Class<E> enumClass) {
        try {
            Method valuesMethod = enumClass.getMethod("values");
            valuesMethod.setAccessible(true);
            return (E[]) valuesMethod.invoke(null);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private <E extends Enum<E>> Component name(E value) {
        return Component.text(String.join(" ", List.of(value.name().split("_"))
                .stream()
                .filter(Predicate.not(String::isEmpty))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .toList()));
    }

    public <E extends Enum<E>> Property<E> enumProperty(@NotNull Component label, @NotNull Class<E> enumClass) {
        return property(new EnumProperty<>(propertyKey(), label, values(enumClass), this::name, null));
    }

    public <E extends Enum<E>> Property<E> enumProperty(@NotNull String label, @NotNull Class<E> enumClass) {
        return enumProperty(miniMessage(label), enumClass);
    }

    public <E extends Enum<E>> Property<E> enumProperty(@NotNull Component label, @NotNull Class<E> enumClass, @NotNull E value) {
        return property(new EnumProperty<>(propertyKey(), label, values(enumClass), this::name, value));
    }

    public <E extends Enum<E>> Property<E> enumProperty(@NotNull String label, @NotNull Class<E> enumClass, @NotNull E value) {
        return enumProperty(miniMessage(label), enumClass, value);
    }

    public Property<Float> floatProperty(@NotNull Component label, float min, float max) {
        return property(new FloatProperty(propertyKey(), label, min, max, null));
    }

    public Property<Float> floatProperty(@NotNull String label, float min, float max) {
        return floatProperty(miniMessage(label), min, max);
    }

    public Property<Float> floatProperty(@NotNull Component label, float min, float max, float value) {
        return property(new FloatProperty(propertyKey(), label, min, max, value));
    }

    public Property<Float> floatProperty(@NotNull String label, float min, float max, float value) {
        return floatProperty(miniMessage(label), min, max, value);
    }

    public Property<Integer> integerProperty(@NotNull Component label, int min, int max) {
        return property(new IntegerProperty(propertyKey(), label, min, max, null));
    }

    public Property<Integer> integerProperty(@NotNull String label, int min, int max) {
        return integerProperty(miniMessage(label), min, max);
    }

    public Property<Integer> integerProperty(@NotNull Component label, int min, int max, int value) {
        return property(new IntegerProperty(propertyKey(), label, min, max, value));
    }

    public Property<Integer> integerProperty(@NotNull String label, int min, int max, int value) {
        return integerProperty(miniMessage(label), min, max, value);
    }

    public Property<String> stringProperty(@NotNull Component label, int maxLines) {
        return property(new StringProperty(propertyKey(), label, 1, null));
    }

    public Property<String> stringProperty(@NotNull String label, int maxLines) {
        return stringProperty(miniMessage(label), maxLines);
    }

    public Property<String> stringProperty(@NotNull Component label) {
        return stringProperty(label, 1);
    }

    public Property<String> stringProperty(@NotNull String label) {
        return stringProperty(miniMessage(label));
    }

    public Property<String> stringProperty(@NotNull Component label, int maxLines, @Nullable String value) {
        return property(new StringProperty(propertyKey(), label, maxLines, value));
    }

    public Property<String> stringProperty(@NotNull String label, int maxLines, @Nullable String value) {
        return stringProperty(miniMessage(label), maxLines, value);
    }

    public Property<String> stringProperty(@NotNull Component label, @Nullable String value) {
        return property(new StringProperty(propertyKey(), label, 1, value));
    }

    public Property<String> stringProperty(@NotNull String label, @Nullable String value) {
        return stringProperty(miniMessage(label), value);
    }

    private String buttonKey() {
        if (actionButtons.size() > 1) {
            throw new IllegalStateException("You cannot have normal buttons and more than one action button");
        }
        return "button_" + buttons.size();
    }

    public void button(@NotNull Component label) {
        buttons.add(new Button(buttonKey(), label, null));
    }

    public void button(@NotNull String label) {
        button(miniMessage(label));
    }

    public void button(@NotNull Component label, @NotNull Runnable action) {
        buttons.add(new Button(buttonKey(), label, action));
    }

    public void button(@NotNull String label, @NotNull Runnable action) {
        button(miniMessage(label), action);
    }

    private String actionButtonKey() {
        if (actionButtons.size() == 1 && !buttons.isEmpty()) {
            throw new IllegalStateException("You cannot have normal buttons and more than one action button");
        }
        return "actionbutton_" + buttons.size();
    }

    public void actionButton(@NotNull Component label) {
        actionButtons.add(new Button(actionButtonKey(), label, null));
    }

    public void actionButton(@NotNull String label) {
        actionButton(miniMessage(label));
    }

    public void actionButton(@NotNull Component label, @NotNull Runnable action) {
        actionButtons.add(new Button(actionButtonKey(), label, action));
    }

    public void actionButton(@NotNull String label, @NotNull Runnable action) {
        actionButton(miniMessage(label), action);
    }
}
