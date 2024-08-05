package org.teamvoided.astralarsenal.init

import com.mojang.blaze3d.systems.RenderSystem
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.network.ClientPlayerEntity
import org.teamvoided.astralarsenal.AstralArsenal.id
import org.teamvoided.astralarsenal.util.getKosmogliphsOnStack

object AstralHudRendering {
    var renderDashTicks = 0
    var renderJumpTicks = 0

    fun init() {
        HudRenderCallback.EVENT.register noRender@{ graphics, deltaTracker ->
            val client = MinecraftClient.getInstance() ?: return@noRender
            val player = client.player ?: return@noRender

            // (ender) ig debugger
//            player.sendMessage(Text.literal("Delay: $renderDashTicks"), true)

            if (renderDashTicks > 0) renderDashTicks--
            if (renderJumpTicks > 0) renderJumpTicks--

            graphics.matrices.push()
            RenderSystem.enableBlend()

            graphics.renderDash(player)
            graphics.renderJump(player)

            RenderSystem.disableBlend()
            graphics.matrices.pop()
        }
    }

    private fun GuiGraphics.renderDash(player: ClientPlayerEntity) {
        val leggings = player.inventory.armor[1]
        if (leggings.isEmpty) return

        if (!getKosmogliphsOnStack(leggings).contains(AstralKosmogliphs.DASH)) return
        val dashData = leggings.get(AstralItemComponents.DASH_DATA) ?: return

        if (dashData.uses < 3) renderDashTicks = 40
        if (renderDashTicks <= 0) return

        this.drawGuiTexture(
            id("hud/dash_bg"),
            (this.scaledWindowWidth / 2) + 8,
            (this.scaledWindowHeight / 2) - 5,
            15,
            9
        )
        repeat(dashData.uses) {
            this.drawGuiTexture(
                id("hud/dash_2"),
                (this.scaledWindowWidth / 2) + 8 + (3 * it),
                (this.scaledWindowHeight / 2) - 5,
                9,
                9
            )
        }
    }

    private fun GuiGraphics.renderJump(player: ClientPlayerEntity) {
        val boots = player.inventory.armor[0]
        if (boots.isEmpty) return

        if (!getKosmogliphsOnStack(boots).contains(AstralKosmogliphs.JUMP)) return
        val jumpData = boots.get(AstralItemComponents.JUMP_DATA) ?: return

        if (jumpData.uses < 3) renderJumpTicks = 40
        if (renderJumpTicks <= 0) return

        this.drawGuiTexture(
            id("hud/jump_bg"),
            (this.scaledWindowWidth / 2) - 8 - 15,
            (this.scaledWindowHeight / 2) - 5,
            15,
            9
        )
        repeat(jumpData.uses) {
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