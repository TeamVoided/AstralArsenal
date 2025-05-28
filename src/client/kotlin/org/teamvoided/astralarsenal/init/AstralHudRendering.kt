package org.teamvoided.astralarsenal.init

import com.mojang.blaze3d.systems.RenderSystem
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.render.DeltaTracker
import net.minecraft.item.ItemStack
import org.teamvoided.astralarsenal.AstralArsenal.id
import org.teamvoided.astralarsenal.util.getKosmogliphs
import org.teamvoided.astralarsenal.util.hasKosmogliph

object AstralHudRendering {
    private var rightIconTicks = 0
    private var leftIconTicks = 0
    private var lowerIconTicks = 0

    @JvmField
    var crimsonCrosshair = false

    fun init() = HudRenderCallback.EVENT.register(::hudRenderer)
    fun hudRenderer(graphics: GuiGraphics, @Suppress("UNUSED_PARAMETER") deltaTracker: DeltaTracker) {
        val client = MinecraftClient.getInstance() ?: return
        val player = client.player ?: return
        if (client.options.hudHidden) return
        if (!client.options.perspective.isFirstPerson) return

        if (rightIconTicks > 0) rightIconTicks--
        if (leftIconTicks > 0) leftIconTicks--
        if (lowerIconTicks > 0) lowerIconTicks--

        graphics.matrices.push()
        RenderSystem.enableBlend()

        graphics.renderRightIcon(player)
        graphics.renderLeftIcon(player)
        graphics.renderLowerIcon(player)

        RenderSystem.disableBlend()
        graphics.matrices.pop()
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
        val kosmogliphs = leggings.getKosmogliphs()
        if (kosmogliphs.has(AstralKosmogliphs.DASH))
            return leggings.get(AstralDataComponents.DASH_DATA)?.uses
        if (kosmogliphs.has(AstralKosmogliphs.DODGE))
            return leggings.get(AstralDataComponents.DODGE_DATA)?.uses
        return null
    }

    private fun GuiGraphics.renderLeftIcon(player: ClientPlayerEntity) {
        val boots = player.inventory.armor[0]
        if (boots.isEmpty) return

        if (!boots.hasKosmogliph(AstralKosmogliphs.JUMP)) return
        val uses = boots.get(AstralDataComponents.JUMP_DATA)?.uses ?: return

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

    private fun GuiGraphics.renderLowerIcon(player: ClientPlayerEntity) {
        val chestplate = player.inventory.armor[2]
        if (chestplate.isEmpty) return
        if (!chestplate.hasKosmogliph(AstralKosmogliphs.ENDURANCE)) return
        val uses = chestplate.get(AstralDataComponents.ENDURANCE_DATA)?.charges ?: return
        if (uses < 3) lowerIconTicks = 40
        if (lowerIconTicks <= 0) return

        repeat(3){
            val distance = (-10) + (10 * (it))
            this.drawGuiTexture(
                id("hud/endurance_charge_background"),
                (this.scaledWindowWidth / 2) - 8 + 3 + distance,
                (this.scaledWindowHeight / 2) - 5 - 12,
                9, 9
            )
        }

        repeat(uses){
            val distance = (-10) + (10 * (it))
            this.drawGuiTexture(
                id("hud/endurance_charge"),
                (this.scaledWindowWidth / 2) - 8 + 3 + distance,
                (this.scaledWindowHeight / 2) - 5 - 12,
                9, 9
            )
        }
    }
}