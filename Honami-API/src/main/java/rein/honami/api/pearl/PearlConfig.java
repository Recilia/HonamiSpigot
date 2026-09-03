package rein.honami.api.pearl;

import org.bukkit.entity.Player;

public interface PearlConfig {

    boolean isEnderpearlCooldownEnabled();

    void setEnderpearlCooldownEnabled(boolean enabled);

    int getEnderpearlCooldownTicks();

    void setEnderpearlCooldownTicks(int ticks);

    boolean isEnderpearlPassThroughFences();

    void setEnderpearlPassThroughFences(boolean passThrough);

    boolean isEnderpearlPassThroughStairs();

    void setEnderpearlPassThroughStairs(boolean passThrough);

    boolean isEnderpearlPassThroughSlabs();

    void setEnderpearlPassThroughSlabs(boolean passThrough);

    boolean isEnderpearlPassThroughCarpet();

    void setEnderpearlPassThroughCarpet(boolean passThrough);

    boolean isEnderpearlPassThroughButtons();

    void setEnderpearlPassThroughButtons(boolean passThrough);

    boolean isEnderpearlPassThroughFlowers();

    void setEnderpearlPassThroughFlowers(boolean passThrough);

    boolean isEnderpearlPassThroughGrass();

    void setEnderpearlPassThroughGrass(boolean passThrough);

    boolean isEnderpearlPassThroughSnow();

    void setEnderpearlPassThroughSnow(boolean passThrough);

    boolean isEnderpearlPassThroughRail();

    void setEnderpearlPassThroughRail(boolean passThrough);

    boolean isEnderpearlPassThroughAnvil();

    void setEnderpearlPassThroughAnvil(boolean passThrough);

    boolean isEnderpearlPassThroughEnchantTable();

    void setEnderpearlPassThroughEnchantTable(boolean passThrough);

    double getEnderpearlGravity();

    void setEnderpearlGravity(double gravity);

    double getEnderpearlSpeed();

    void setEnderpearlSpeed(double speed);

    void setCooldown(Player player, int ticks);

    int getCooldown(Player player);

    void clearCooldown(Player player);

    boolean hasCooldown(Player player);
}
