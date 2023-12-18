package dto.interface_context;

import org.bukkit.Material;

public class TestContextDTO extends ContextDTO {
    public String inventoryName;
    public int inventoryRows;
    public Material material;

    public TestContextDTO(String inventoryName, int inventoryRows, Material material) {
        this.inventoryName = inventoryName;
        this.inventoryRows = inventoryRows;
        this.material = material;
    }
}
