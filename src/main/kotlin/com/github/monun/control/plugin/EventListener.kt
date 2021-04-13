package com.github.monun.control.plugin

import io.papermc.paper.event.player.AsyncChatEvent
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.Cancellable
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.hanging.HangingBreakByEntityEvent
import org.bukkit.event.hanging.HangingPlaceEvent
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.player.*

class EventListener(
    private val plugin: ControlPlugin
) : Listener {

    private fun Cancellable.control(cc: Control<Boolean>, player: Player) {
        if (plugin.control(cc)) return
        if (player.hasPermission(cc.permission)) return

        isCancelled = true
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onAsyncChat(event: AsyncChatEvent) {
        event.control(Control.CHAT, event.player)
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onPlayerCommand(event: PlayerCommandPreprocessEvent) {
        event.control(Control.COMMAND, event.player)
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onPlayerMove(event: PlayerMoveEvent) {
        event.control(Control.MOVEMENT, event.player)
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onEntityDamage(event: EntityDamageEvent) {
        val entity = event.entity

        if (entity is Player) {
            event.control(Control.DAMAGE, entity)

            if (event.isCancelled)
                entity.fireTicks = 0
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        var damager: Any = event.damager

        if (damager is Projectile) {
            damager = damager.shooter ?: return
        }

        if (damager is Player) event.control(Control.ATTACK, damager)
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onPlayerInteract(event: PlayerInteractEvent) {
        event.control(Control.INTERACTION, event.player)
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onPlayerInteractEntity(event: PlayerInteractEntityEvent) {
        event.control(Control.INTERACTION, event.player)
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onBlockBreak(event: BlockBreakEvent) {
        event.control(Control.BREAKING, event.player)
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onPlayerBucketFill(event: PlayerBucketFillEvent) {
        event.control(Control.BREAKING, event.player)
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onHangingBreakByEntity(event: HangingBreakByEntityEvent) {
        var remover: Any = event.remover ?: return

        if (remover is Projectile) {
            remover = remover.shooter ?: return
        }
        if (remover is Player) {
            event.control(Control.BREAKING, remover)
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onHangingPlace(event: HangingPlaceEvent) {
        val player = event.player ?: return
        event.control(Control.PLACEMENT, player)
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onBlockPlace(event: BlockPlaceEvent) {
        event.control(Control.PLACEMENT, event.player)
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onPlayerBucketEmpty(event: PlayerBucketEmptyEvent) {
        event.control(Control.PLACEMENT, event.player)
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onCraftingItem(event: CraftItemEvent) {
        event.control(Control.CRAFTING, event.whoClicked as Player)
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onPlayerAttemptPickupItem(event: PlayerAttemptPickupItemEvent) {
        event.control(Control.PICKUP, event.player)
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onPlayerDropItem(event: PlayerDropItemEvent) {
        event.control(Control.DROP, event.player)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        if (plugin.control(Control.JOIN_MESSAGE)) return
        if (event.player.hasPermission(Control.JOIN_MESSAGE.permission)) return
        event.joinMessage(null)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        if (plugin.control(Control.QUIT_MESSAGE)) return
        if (event.player.hasPermission(Control.QUIT_MESSAGE.permission)) return
        event.quitMessage(null)
    }
}