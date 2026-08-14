package com.github.noamm9.features.impl.dungeon

import com.github.noamm9.event.impl.TickEvent
import com.github.noamm9.features.Feature
import com.github.noamm9.ui.clickgui.components.impl.ToggleSetting
import com.github.noamm9.utils.PlayerUtils
import net.minecraft.core.BlockPos
import net.minecraft.world.phys.BlockHitResult

object LeverTriggerbot : Feature("Automatically clicks dungeon levers.") {

    private val forGate by ToggleSetting("For gate levers")
    private val forDevice by ToggleSetting("For device levers")

    private val gateLevers = listOf(
        BlockPos(106, 124, 113),
        BlockPos(94, 124, 113),
        BlockPos(23, 132, 138),
        BlockPos(27, 124, 127),
        BlockPos(2, 122, 55),
        BlockPos(14, 122, 55),
        BlockPos(84, 121, 34),
        BlockPos(86, 128, 46),
    )

    private val deviceLevers = listOf(
        BlockPos(62, 133, 142),
        BlockPos(58, 133, 142),
        BlockPos(60, 134, 142),
        BlockPos(60, 135, 142),
        BlockPos(62, 136, 142),
        BlockPos(58, 136, 142),
    )

    private val clicked = mutableMapOf<BlockPos, Int>()

    override fun init() {
        register<TickEvent.Start> {
            if (mc.screen != null || player == null) return@register

            clicked.entries.removeIf { --it.value <= 0 }

            val hit = mc.hitResult as? BlockHitResult ?: return@register
            val pos = hit.blockPos

            if (player.distanceToSqr(
                    pos.x + 0.5,
                    pos.y + 0.5,
                    pos.z + 0.5
                ) > 4.5 * 4.5
            ) return@register

            if (pos in clicked) return@register

            if (forGate.value && pos in gateLevers) {
                click(pos)
            } else if (forDevice.value && pos in deviceLevers) {
                click(pos)
            }
        }
    }

    private fun click(pos: BlockPos) {
        PlayerUtils.rightClick()
        clicked[pos] = 20
    }
}
