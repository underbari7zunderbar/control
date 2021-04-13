package com.github.monun.control.plugin

import com.google.common.collect.ImmutableList

sealed class Control<T>(val name: String, val type: Class<*>, val default: T) {
    object CHAT : Control<Boolean>("chat", java.lang.Boolean::class.java, true)
    object COMMAND : Control<Boolean>("command", java.lang.Boolean::class.java, true)
    object MOVEMENT : Control<Boolean>("movement", java.lang.Boolean::class.java, true)
    object ATTACK : Control<Boolean>("attack", java.lang.Boolean::class.java, true)
    object DAMAGE : Control<Boolean>("damage", java.lang.Boolean::class.java, true)
    object INTERACTION : Control<Boolean>("interaction", java.lang.Boolean::class.java, true)
    object BREAKING : Control<Boolean>("breaking", java.lang.Boolean::class.java, true)
    object PLACEMENT : Control<Boolean>("placement", java.lang.Boolean::class.java, true)
    object CRAFTING : Control<Boolean>("crafting", java.lang.Boolean::class.java, true)
    object DROP : Control<Boolean>("drop", java.lang.Boolean::class.java, true)
    object PICKUP : Control<Boolean>("pickup", java.lang.Boolean::class.java, true)
    object JOIN_MESSAGE : Control<Boolean>("join-message", java.lang.Boolean::class.java, true)
    object QUIT_MESSAGE : Control<Boolean>("quit-message", java.lang.Boolean::class.java, true)

    val permission = "control.$name"

    @Suppress("UNCHECKED_CAST")
    fun parse(s: String): T? {
        if (type == java.lang.Boolean::class.java) {
            if (s.equals("true", true)) return java.lang.Boolean.TRUE as T
            if (s.equals("false", true)) return java.lang.Boolean.FALSE as T
        } else if (type == String::class.java) {
            return s as T
        }

        return null
    }

    companion object {
        val values by lazy {
            ImmutableList.of(
                CHAT,
                COMMAND,
                MOVEMENT,
                ATTACK,
                DAMAGE,
                INTERACTION,
                BREAKING,
                PLACEMENT,
                CRAFTING,
                DROP,
                PICKUP,
                JOIN_MESSAGE,
                QUIT_MESSAGE
            )
        }
    }
}