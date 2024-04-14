package it.killernik.scarletairdrop.Utils;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HologramsUtils {

    public static double LINE_HEIGHT = 0.25;
    private Location location;
    private List<String> lines = new ArrayList<>();
    private List<ArmorStand> entities;
    private boolean spawned;

    public HologramsUtils(Location location, List<String> lines) {
        this.lines = lines;
        this.entities = new LinkedList<ArmorStand>();
        this.spawned = false;
        this.location = location;
    }

    public void spawn() {
        Chunk cnk = this.location.getChunk();
        if (!cnk.isLoaded()) {
            cnk.load();
        }
        for (int i = 0; i < this.lines.size(); ++i) {
            Location spawn = this.location.clone().subtract(0.0, i * 0.20, 0.0);
            ArmorStand entity = (ArmorStand) this.location.getWorld().spawnEntity(spawn, EntityType.ARMOR_STAND);
            entity.setGravity(false);
            entity.setBasePlate(false);
            entity.setVisible(false);
            entity.setCustomNameVisible(true);
            entity.setSmall(true);
            entity.setCustomName(MessageUtil.message((String) this.lines.get(i)));
            this.entities.add(entity);
        }
        this.spawned = true;
    }

    public void update() {
        this.remove();
        this.spawn();
    }

    public void destroy() {
        this.remove();
        this.lines.clear();
    }

    public ArmorStand get(int index) {
        return this.entities.get(index);
    }

    public HologramsUtils add(String line) {
        this.lines.add(line);
        if (this.spawned) {
            this.update();
        }
        return this;
    }

    public HologramsUtils set(int index, String line) {
        this.lines.set(index, line);
        if (this.spawned) {
            this.update();
        }
        return this;
    }

    private boolean removeEntity(ArmorStand entity) {
        this.lines.remove(entity.getCustomName());
        entity.remove();
        boolean removed = this.entities.remove(entity);
        if (removed && this.spawned) {
            this.update();
        }
        return removed;
    }

    public void remove() {
        for (ArmorStand stand : this.entities) {
            stand.remove();
        }
    }

    public boolean remove(String line) {
        for (ArmorStand entity : this.entities) {
            if (entity.getCustomName().equals(line)) {
                return this.removeEntity(entity);
            }
        }
        return false;
    }

}
