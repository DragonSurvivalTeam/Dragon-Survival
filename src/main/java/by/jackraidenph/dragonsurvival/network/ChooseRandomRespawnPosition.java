package by.jackraidenph.dragonsurvival.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Sets a spawn point for a player
 */
public class ChooseRandomRespawnPosition implements IMessage<ChooseRandomRespawnPosition> {
    BlockPos position;

    public ChooseRandomRespawnPosition() {
    }

    public ChooseRandomRespawnPosition(BlockPos position) {
        this.position = position;
    }

    @Override
    public void encode(ChooseRandomRespawnPosition message, PacketBuffer buffer) {
        buffer.writeBlockPos(message.position);
    }

    @Override
    public ChooseRandomRespawnPosition decode(PacketBuffer buffer) {
        return new ChooseRandomRespawnPosition(buffer.readBlockPos());
    }

    @Override
    public void handle(ChooseRandomRespawnPosition message, Supplier<NetworkEvent.Context> supplier) {
        ServerPlayerEntity serverPlayerEntity = supplier.get().getSender();
        BlockPos.Mutable spawnPosition = new BlockPos.Mutable(message.position);
        ServerWorld serverWorld = serverPlayerEntity.getServerWorld();
        serverWorld.getChunkProvider().registerTicket(TicketType.POST_TELEPORT, new ChunkPos(spawnPosition), 1, serverPlayerEntity.getEntityId());
        spawnPosition.setY(200);
        while (serverWorld.getBlockState(spawnPosition).isAir(serverWorld, spawnPosition)) {
            spawnPosition.setY(spawnPosition.getY() - 1);
        }
        spawnPosition.add(0, 1, 0);
        serverPlayerEntity.connection.setPlayerLocation(spawnPosition.getX() + 0.5, spawnPosition.getY(), spawnPosition.getZ() + 0.5, 0, 0);
        serverPlayerEntity.setSpawnPoint(spawnPosition, true, false, DimensionType.OVERWORLD);
    }
}