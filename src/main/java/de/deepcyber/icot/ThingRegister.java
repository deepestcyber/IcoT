package de.deepcyber.icot;

import de.deepcyber.icot.block.TileEntitySubscriberThing;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class ThingRegister {
    private class BlockRef {
        public final World world;
        public final BlockPos pos;

        BlockRef(World world, BlockPos pos) {
            this.world = world;
            this.pos = pos;
        }

        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof BlockRef) {
                final BlockRef other = (BlockRef) obj;
                return Objects.equals(this.world, other.world) && Objects.equals(this.pos, other.pos);
            }
            return false;
        }

        public int hashCode() {
            return (world == null ? 0 : world.hashCode()) ^ (pos == null ? 0 : pos.hashCode());
        }

        public TileEntity getTileEntity() {
            if (world == null || pos == null) {
                return null;
            }
            return world.getTileEntity(pos);
        }
    }

    private Map<String, Set<BlockRef>> map = new HashMap<>();

    synchronized public void register(String name, World world, BlockPos pos) {
        Set<BlockRef> set = map.computeIfAbsent(name, v -> new HashSet<>());
        set.add(new BlockRef(world, pos));
    }

    synchronized public void unregister(String name, World world, BlockPos pos) {
        Set<BlockRef> set = map.get(name);
        if (set != null) {
            set.remove(new BlockRef(world, pos));
            if (set.isEmpty()) {
                map.remove(name);
            }
        }
    }

    synchronized public void apply(String name, BiFunction<World, BlockPos, Void> foo) {
        Set<BlockRef> set = map.get(name);
        if (set != null) {
            set.forEach(r -> {
                foo.apply(r.world, r.pos);
            });
        }
    }

    synchronized public <T extends TileEntity> void apply(Class<T> clazz, Consumer<T> m, String name) {
        Set<BlockRef> set = map.get(name);
        if (set != null) {
            set.forEach(r -> {
                TileEntity te = r.getTileEntity();
                if ( clazz.isInstance(te)) {
                    T tet = clazz.cast(te);
                    m.accept(tet);
                }
            });
        }
    }

    void tt() {
        // TODO: delete this
        apply(TileEntitySubscriberThing.class, TileEntitySubscriberThing::notifyCheckNeeded, "hugo");
    }
}