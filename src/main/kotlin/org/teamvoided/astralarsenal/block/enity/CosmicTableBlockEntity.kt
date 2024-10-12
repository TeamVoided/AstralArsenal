package org.teamvoided.astralarsenal.block.enity

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.block.BlockState
import net.minecraft.block.entity.LootableContainerBlockEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.HolderLookup
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import org.teamvoided.astralarsenal.init.AstralBlocks
import org.teamvoided.astralarsenal.menu.CosmicTableData
import org.teamvoided.astralarsenal.menu.CosmicTableMenu

class CosmicTableBlockEntity(
    pos: BlockPos,
    state: BlockState,
) : LootableContainerBlockEntity(AstralBlocks.COSMIC_TABLE_BLOCK_ENTITY, pos, state),
    ExtendedScreenHandlerFactory<CosmicTableData> {
    private val inventory = DefaultedList.ofSize(2, ItemStack.EMPTY)
    var itemRotation = 0f
    var nextItemRotation = 0f
    var targetItemRotation = 0f

    override fun getContainerName(): Text = Text.translatable("container.cosmic_table")

    public override fun getInventory(): DefaultedList<ItemStack> = inventory

    override fun setInventory(defaultedList: DefaultedList<ItemStack>) {
        inventory.clear()
        inventory.addAll(defaultedList)
    }

    override fun createScreenHandler(
        syncId: Int,
        playerInventory: PlayerInventory
    ): ScreenHandler {
        return CosmicTableMenu(syncId, playerInventory, CosmicTableData(this))
    }

    override fun size(): Int = inventory.size

    override fun writeNbt(nbt: NbtCompound, lookupProvider: HolderLookup.Provider) {
        super.writeNbt(nbt, lookupProvider)
        Inventories.writeNbt(nbt, inventory, lookupProvider)
    }

    override fun readNbtImpl(nbt: NbtCompound, lookupProvider: HolderLookup.Provider) {
        super.readNbtImpl(nbt, lookupProvider)
        Inventories.readNbt(nbt, inventory, lookupProvider)
    }

    override fun getScreenOpeningData(player: ServerPlayerEntity): CosmicTableData {
        return CosmicTableData(this)
    }

    companion object {
        fun tick(world: World, pos: BlockPos, ignore: BlockState, entity: CosmicTableBlockEntity) {
            entity.itemRotation = entity.nextItemRotation
            val playerEntity = world.getClosestPlayer(
                pos.x.toDouble() + 0.5,
                pos.y.toDouble() + 0.5,
                pos.z.toDouble() + 0.5,
                3.0,
                false
            )
            if (playerEntity != null) {
                val d = playerEntity.x - (pos.x.toDouble() + 0.5)
                val e = playerEntity.z - (pos.z.toDouble() + 0.5)
                entity.targetItemRotation = MathHelper.atan2(e, d).toFloat()
            } else {
                entity.targetItemRotation += 0.02f
            }

            while (entity.nextItemRotation >= Math.PI.toFloat()) {
                entity.nextItemRotation -= (Math.PI * 2).toFloat()
            }

            while (entity.nextItemRotation < -Math.PI.toFloat()) {
                entity.nextItemRotation += (Math.PI * 2).toFloat()
            }

            while (entity.targetItemRotation >= Math.PI.toFloat()) {
                entity.targetItemRotation -= (Math.PI * 2).toFloat()
            }

            while (entity.targetItemRotation < -Math.PI.toFloat()) {
                entity.targetItemRotation += (Math.PI * 2).toFloat()
            }

            var g: Float = entity.targetItemRotation - entity.nextItemRotation

            while (g >= Math.PI.toFloat()) {
                g -= (Math.PI * 2).toFloat()
            }

            while (g < -Math.PI.toFloat()) {
                g += (Math.PI * 2).toFloat()
            }

            entity.nextItemRotation += g * 0.4f
        }
    }
}