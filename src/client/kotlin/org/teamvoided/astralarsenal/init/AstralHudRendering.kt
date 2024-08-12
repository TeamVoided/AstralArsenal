package org.teamvoided.astralarsenal.init

import com.mojang.blaze3d.systems.RenderSystem
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.item.ItemStack
import org.teamvoided.astralarsenal.AstralArsenal.id
import org.teamvoided.astralarsenal.util.getKosmogliphsOnStack

object AstralHudRendering {
    var rightIconTicks = 0
    var leftIconTicks = 0

    fun init() {
        HudRenderCallback.EVENT.register noRender@{ graphics, deltaTracker ->
            val client = MinecraftClient.getInstance() ?: return@noRender
            val player = client.player ?: return@noRender
            if (client.options.hudHidden) return@noRender

            // (ender) in game debugger
//            player.sendMessage(Text.literal("Delay: $renderDashTicks"), true)

            if (rightIconTicks > 0) rightIconTicks--
            if (leftIconTicks > 0) leftIconTicks--

            graphics.matrices.push()
            RenderSystem.enableBlend()

            graphics.renderRightIcon(player)
            graphics.renderLeftIcon(player)

            RenderSystem.disableBlend()
            graphics.matrices.pop()
        }
    }

    private fun GuiGraphics.renderRightIcon(player: ClientPlayerEntity) {
        val leggings = player.inventory.armor[1]
        if (leggings.isEmpty) return

        val uses = getRightIconUses(leggings) ?: return

        if (uses < 3) rightIconTicks = 40
        if (rightIconTicks <= 0) return

        this.drawGuiTexture(
            id("hud/dash_bg"),
            (this.scaledWindowWidth / 2) + 8,
            (this.scaledWindowHeight / 2) - 5,
            15,
            9
        )
        repeat(uses) {
            this.drawGuiTexture(
                id("hud/dash_2"),
                (this.scaledWindowWidth / 2) + 8 + (3 * it),
                (this.scaledWindowHeight / 2) - 5,
                9,
                9
            )
        }
    }

    private fun getRightIconUses(leggings: ItemStack): Int? {
        val kosmo = getKosmogliphsOnStack(leggings)
        if (kosmo.contains(AstralKosmogliphs.DASH))
            return leggings.get(AstralItemComponents.DASH_DATA)?.uses
        if (kosmo.contains(AstralKosmogliphs.DODGE))
            return leggings.get(AstralItemComponents.DODGE_DATA)?.uses
        return null
    }

    private fun GuiGraphics.renderLeftIcon(player: ClientPlayerEntity) {
        val boots = player.inventory.armor[0]
        if (boots.isEmpty) return

        if (!getKosmogliphsOnStack(boots).contains(AstralKosmogliphs.JUMP)) return
        val uses = boots.get(AstralItemComponents.JUMP_DATA)?.uses ?: return

        if (uses < 3) leftIconTicks = 40
        if (leftIconTicks <= 0) return

        this.drawGuiTexture(
            id("hud/jump_bg"),
            (this.scaledWindowWidth / 2) - 8 - 15,
            (this.scaledWindowHeight / 2) - 5,
            15,
            9
        )
        repeat(uses) {
            this.drawGuiTexture(
                id("hud/jump_1"),
                (this.scaledWindowWidth / 2) - 8 - 9 - (3 * it),
                (this.scaledWindowHeight / 2) - 5,
                9,
                9
            )
        }
    }
}