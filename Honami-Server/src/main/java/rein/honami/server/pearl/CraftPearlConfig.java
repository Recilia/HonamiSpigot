package rein.honami.server.pearl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import rein.honami.api.pearl.PearlConfig;

public class CraftPearlConfig implements PearlConfig {

    private boolean cooldownEnabled = true;
    private int cooldownTicks = 20;
    private boolean passThroughFences = true;
    private boolean passThroughStairs = true;
    private boolean passThroughSlabs = true;
    private boolean passThroughCarpet = true;
    private boolean passThroughButtons = true;
    private boolean passThroughFlowers = true;
    private boolean passThroughGrass = true;
    private boolean passThroughSnow = true;
    private boolean passThroughRail = true;
    private boolean passThroughAnvil = true;
    private boolean passThroughEnchantTable = true;
    private double gravity = 0.03;
    private double speed = 1.5;

    private final Map<UUID, Long> cooldowns = new ConcurrentHashMap<>();

    @Override
    public boolean isEnderpearlCooldownEnabled() {
        return cooldownEnabled;
    }

    @Override
    public void setEnderpearlCooldownEnabled(boolean enabled) {
        this.cooldownEnabled = enabled;
    }

    @Override
    public int getEnderpearlCooldownTicks() {
        return cooldownTicks;
    }

    @Override
    public void setEnderpearlCooldownTicks(int ticks) {
        this.cooldownTicks = ticks;
    }

    @Override
    public boolean isEnderpearlPassThroughFences() {
        return passThroughFences;
    }

    @Override
    public void setEnderpearlPassThroughFences(boolean passThrough) {
        this.passThroughFences = passThrough;
    }

    @Override
    public boolean isEnderpearlPassThroughStairs() {
        return passThroughStairs;
    }

    @Override
    public void setEnderpearlPassThroughStairs(boolean passThrough) {
        this.passThroughStairs = passThrough;
    }

    @Override
    public boolean isEnderpearlPassThroughSlabs() {
        return passThroughSlabs;
    }

    @Override
    public void setEnderpearlPassThroughSlabs(boolean passThrough) {
        this.passThroughSlabs = passThrough;
    }

    @Override
    public boolean isEnderpearlPassThroughCarpet() {
        return passThroughCarpet;
    }

    @Override
    public void setEnderpearlPassThroughCarpet(boolean passThrough) {
        this.passThroughCarpet = passThrough;
    }

    @Override
    public boolean isEnderpearlPassThroughButtons() {
        return passThroughButtons;
    }

    @Override
    public void setEnderpearlPassThroughButtons(boolean passThrough) {
        this.passThroughButtons = passThrough;
    }

    @Override
    public boolean isEnderpearlPassThroughFlowers() {
        return passThroughFlowers;
    }

    @Override
    public void setEnderpearlPassThroughFlowers(boolean passThrough) {
        this.passThroughFlowers = passThrough;
    }

    @Override
    public boolean isEnderpearlPassThroughGrass() {
        return passThroughGrass;
    }

    @Override
    public void setEnderpearlPassThroughGrass(boolean passThrough) {
        this.passThroughGrass = passThrough;
    }

    @Override
    public boolean isEnderpearlPassThroughSnow() {
        return passThroughSnow;
    }

    @Override
    public void setEnderpearlPassThroughSnow(boolean passThrough) {
        this.passThroughSnow = passThrough;
    }

    @Override
    public boolean isEnderpearlPassThroughRail() {
        return passThroughRail;
    }

    @Override
    public void setEnderpearlPassThroughRail(boolean passThrough) {
        this.passThroughRail = passThrough;
    }

    @Override
    public boolean isEnderpearlPassThroughAnvil() {
        return passThroughAnvil;
    }

    @Override
    public void setEnderpearlPassThroughAnvil(boolean passThrough) {
        this.passThroughAnvil = passThrough;
    }

    @Override
    public boolean isEnderpearlPassThroughEnchantTable() {
        return passThroughEnchantTable;
    }

    @Override
    public void setEnderpearlPassThroughEnchantTable(boolean passThrough) {
        this.passThroughEnchantTable = passThrough;
    }

    @Override
    public double getEnderpearlGravity() {
        return gravity;
    }

    @Override
    public void setEnderpearlGravity(double gravity) {
        this.gravity = gravity;
    }

    @Override
    public double getEnderpearlSpeed() {
        return speed;
    }

    @Override
    public void setEnderpearlSpeed(double speed) {
        this.speed = speed;
    }

    @Override
    public void setCooldown(Player player, int ticks) {
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + (ticks * 50L));
    }

    @Override
    public int getCooldown(Player player) {
        Long expiry = cooldowns.get(player.getUniqueId());
        if (expiry == null) return 0;
        long remaining = expiry - System.currentTimeMillis();
        if (remaining <= 0) {
            cooldowns.remove(player.getUniqueId());
            return 0;
        }
        return (int) (remaining / 50L);
    }

    @Override
    public void clearCooldown(Player player) {
        cooldowns.remove(player.getUniqueId());
    }

    @Override
    public boolean hasCooldown(Player player) {
        return getCooldown(player) > 0;
    }
}
