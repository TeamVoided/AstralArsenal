package org.teamvoided.astralarsenal

import com.mojang.blaze3d.platform.InputUtil
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBind
import org.lwjgl.glfw.GLFW
import org.teamvoided.astralarsenal.AstralArsenal.DEFAULT_KEY_CATEGORY
import org.teamvoided.astralarsenal.data.gen.prov.AstralEnTranslationProvider

object AstralKeyBindings {
    val dashAbility = key("dash", keycode = GLFW.GLFW_KEY_R)

    fun key(name: String, type: InputUtil.Type = InputUtil.Type.KEYSYM, keycode: Int, category: String = DEFAULT_KEY_CATEGORY): KeyBind {
        val id = AstralArsenal.id(name)
        val bind = KeyBind(id.toTranslationKey("key"), type, keycode, category)
        AstralEnTranslationProvider.registerKeybindForDataGen(id, name)
        return KeyBindingHelper.registerKeyBinding(bind)
    }
}