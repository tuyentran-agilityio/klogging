/*

   Copyright 2021 Michael Strasser.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/

package io.klogging

import io.klogging.impl.KloggerImpl
import kotlin.reflect.KClass

public expect fun classNameOf(ownerClass: KClass<*>): String?

private val LOGGERS: MutableMap<String, Klogger> = mutableMapOf()

internal fun loggerFor(name: String?): Klogger {
    val loggerName = name ?: "Klogging"
    return LOGGERS.getOrPut(loggerName) { KloggerImpl(loggerName) }
}

public fun logger(name: String): Klogger = loggerFor(name)

public fun logger(ownerClass: KClass<*>): Klogger = loggerFor(classNameOf(ownerClass))

public interface Klogging {
    public val logger: Klogger
        get() = loggerFor(classNameOf(this::class))
}