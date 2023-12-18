package interfaces.general;

import dto.interface_context.ClickDTO;
import dto.interface_context.ContextDTO;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class ClickableItem {
    private final BiPredicate<Integer, ContextDTO> condition;
    private final BiFunction<Integer, ContextDTO, ItemStack> itemStackCreator;
    private final BiConsumer<ContextDTO, ClickDTO> action;

    public ClickableItem(BiPredicate<Integer, ContextDTO> condition, BiFunction<Integer, ContextDTO, ItemStack> itemStackCreator, BiConsumer<ContextDTO, ClickDTO> action) {
        this.condition = condition;
        this.itemStackCreator = itemStackCreator;
        this.action = action;
    }

    public boolean testCondition(int slot, ContextDTO context) {
        return condition.test(slot, context);
    }

    public ItemStack createItemStack(ContextDTO context, int slot) {
        return itemStackCreator.apply(slot, context);
    }

    public void performAction(ContextDTO context, ClickDTO click) {
        action.accept(context, click);
    }
}
