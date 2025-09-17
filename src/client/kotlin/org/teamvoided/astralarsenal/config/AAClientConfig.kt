package org.teamvoided.astralarsenal.config

import me.fzzyhmstrs.fzzy_config.config.Config
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedColor
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum.WidgetType
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS
import net.minecraft.util.ActionResult
import org.teamvoided.astralarsenal.AstralArsenal.id

class AAClientConfig : Config(id("client")) {
    // (ender) this will be just on its own
    var enumConfig = ValidatedEnum(ActionResult.FAIL, WidgetType.CYCLING)

    // region EXAMPLE_GROUP
    @Suppress("unused") // (ender) this starts the group
    var exampleGroup = ConfigGroup("example_group_id", false/*(ender) this toggle if it should be closed by default */)

    // (ender) everything between the start and the end is in the group
    var intConfig = ValidatedInt(0, 10, -10, TEXTBOX_WITH_BUTTONS)

    @ConfigGroup.Pop // (ender) This ends the group
    var boolConfig = true
    // endregion

    // (ender) and this will also be on it own and not in a group
    var colorConfig = ValidatedColor(transparent = true)
}