package com.github.monun.control.plugin

import com.github.monun.kommand.KommandContext
import com.github.monun.kommand.KommandDispatcherBuilder
import com.github.monun.kommand.argument.KommandArgument
import com.github.monun.kommand.argument.suggestions
import com.github.monun.kommand.sendFeedback

object KommandCTL {
    private lateinit var plugin: ControlPlugin

    internal fun register(plugin: ControlPlugin, builder: KommandDispatcherBuilder) {
        KommandCTL.plugin = plugin

        builder.register("ctl") {
            then("control" to ControlArgument, "value" to ValueArgument) {
                executes {
                    val sender = it.sender
                    val control = it.parseArgument<Control<*>>("control")
                    val value = it.parseArgument<Any>("value")

                    plugin.unsafeControl(control, value)
                    sender.sendFeedback("${control.name} = $value")
                }
            }
        }
    }

    object ControlArgument : KommandArgument<Control<*>> {
        override fun parse(context: KommandContext, param: String): Control<*>? {
            return Control.values.find { it.name.equals(param, true) }
        }

        override fun listSuggestion(context: KommandContext, target: String): Collection<String> {
            return Control.values.suggestions(target) { it.name }
        }
    }

    object ValueArgument : KommandArgument<Any> {
        override fun parse(context: KommandContext, param: String): Any? {
            return context.parseOrNullArgument<Control<*>>("control")?.parse(param)
        }

        override fun listSuggestion(context: KommandContext, target: String): Collection<String> {
            val control = context.parseOrNullArgument<Control<*>>("control") ?: return emptyList()
            val type = control.type

            return (if (type == Boolean::class.java) listOf("true", "false") else emptyList()).suggestions(target)
        }
    }
}