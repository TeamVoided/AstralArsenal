package org.teamvoided.astralarsenal.config

import me.fzzyhmstrs.fzzy_config.config.Config
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup
import me.fzzyhmstrs.fzzy_config.config.ConfigSection
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedMap
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedSet
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedRegistryType
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedChoice
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedColor
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum.WidgetType
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedDouble
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.util.ActionResult
import org.teamvoided.astralarsenal.AstralArsenal.id

class AAConfig : Config(id("common")) {
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

    // (ender) Kinda like a group instead of being inline it in a new screen
    var configSection = CustomSection()

    class CustomSection : ConfigSection() {

        var pair1 = ValidatedInt(1, 10, 0).pairWith(ValidatedInt(10, 20, 10))

        var intListConfig = ValidatedList.ofInt(1, 2, 5, 10)

        var listConfig = ValidatedList(defaultValue = listOf(2.2, 1.6), ValidatedDouble(1.0, 10.0, 0.0))

        var mapConfig = ValidatedMap.Builder<Item, Boolean>()
            .keyHandler(ValidatedRegistryType.of(Registries.ITEM))
            .valueHandler(ValidatedBoolean()) //next the value handler.
            .defaults(
                mapOf(
                    Items.APPLE to true,
                    Items.FLINT to false,
                    Items.DIRT to true
                )
            ) //default map values can be set a variety of ways
            .build() //build!

        var validatedWeightChoicesConfig =
            ValidatedSet.ofInt(1, 2, 5, 10, 20).toChoices(ValidatedChoice.WidgetType.CYCLING)
    }
}