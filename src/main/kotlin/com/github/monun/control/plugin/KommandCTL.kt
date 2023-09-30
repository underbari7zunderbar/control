package com.github.monun.control.plugin

import io.github.monun.kommand.PluginKommand
import io.github.monun.kommand.getValue
import net.kyori.adventure.text.Component.text

object KommandCTL {
    private lateinit var plugin: ControlPlugin

    internal fun register(plugin: ControlPlugin, kommand: PluginKommand) {
        this.plugin = plugin

        kommand.register("ctl") {

            val controlArgument = dynamic { _, input ->
                Control.values.find { it.name == input }
            }.apply {
                suggests {
                    suggest(Control.values.map { it.name })
                }
            }

            val valueArgument = dynamic { context, input ->
                val control: Control<*> by context
                val type = control.type

                if (java.lang.Boolean::class.java == type) input.toBoolean()
                else null
            }

            then("control" to controlArgument) {
                executes {
                    val control: Control<*> by it
                    feedback(text("${control.name} = ${plugin.control(control)}"))
                }

                then("value" to valueArgument) {
                    executes {
                        val control: Control<*> by it
                        val value: Any by it
                        plugin.unsafeControl(control, value)
                        broadcast(text("${control.name} = $value"))
                    }
                }
            }
        }
    }
}