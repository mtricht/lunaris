package dev.tricht.lunaris.com.pathofexile.itemtransformer

import dev.tricht.lunaris.com.pathofexile.request.*
import dev.tricht.lunaris.item.Item

object TypeFilterSetter {
    private val mapping = mapOf(
            "Bow" to "weapon.bow",
            "Claw" to "weapon.claw",
            "One Handed Axe" to "weapon.oneaxe",
            "One Handed Mace" to "weapon.onemace",
            "One Handed Sword" to "weapon.onesword",
            "Two Handed Axe" to "weapon.twoaxe",
            "Two Handed Mace" to "weapon.twomace",
            "Two Handed Sword" to "weapon.twosword",
            "Wand" to "weapon.wand",
            "Body Armour" to "armour.chest",
            "Boots" to "armour.boots",
            "Gloves" to "armour.gloves",
            "Helmet" to "armour.helmet",
            "Shield" to "armour.shield",
            "Quiver" to "armour.quiver",
            "Amulet" to "accessory.amulet",
            "Belt" to "accessory.belt",
            "Ring" to "accessory.ring"
    )

    @JvmStatic
    operator fun set(item: Item, query: Query) {
        val typeFilters = Filters.NestedFilters()
        val deeperTypeFilters = Filters.DeeperFilters()
        typeFilters.filters = deeperTypeFilters
        query.filters?.typeFilters = typeFilters
        if (item.category != null && mapping.containsKey(item.category!!)) {
            val category = Option()
            category.option = mapping[item.category!!]
            deeperTypeFilters.category = category
        }
    }
}