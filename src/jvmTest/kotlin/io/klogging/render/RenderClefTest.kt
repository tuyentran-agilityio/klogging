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

package io.klogging.render

import io.klogging.events.Level
import io.klogging.events.LogEvent
import io.klogging.events.currentContext
import io.klogging.randomString
import io.klogging.timestampNow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class RenderClefTest : DescribeSpec({
    describe("Render a `LogEvent` to CLEF") {
        it("omits @x if `stackTrace` is null") {
            val ts = timestampNow()
            val event = LogEvent(ts, "test.local", "Test", currentContext(), Level.INFO, null, "Message", null, mapOf())

            RENDER_CLEF(event) shouldBe """{
            |"@t":"${event.timestamp.isoString}",
            |"@l":"${event.level}",
            |"host":"${event.host}",
            |"logger":"${event.logger}",
            |"context":"${event.context}",
            |"@m":"${event.message}"
            |}""".trimMargin().replace("\n", "")
        }
        it("includes @x if `stackTrace` is present") {
            val ts = timestampNow()
            val trace = randomString()
            val event =
                LogEvent(ts, "test.local", "Test", currentContext(), Level.INFO, null, "Message", trace, mapOf())

            RENDER_CLEF(event) shouldBe """{
            |"@t":"${event.timestamp.isoString}",
            |"@l":"${event.level}",
            |"host":"${event.host}",
            |"logger":"${event.logger}",
            |"context":"${event.context}",
            |"@m":"${event.message}",
            |"@x":"${event.stackTrace}"
            |}""".trimMargin().replace("\n", "")
        }
        it("includes @m but not @mt if `template` is null") {
            val ts = timestampNow()
            val event = LogEvent(ts, "test.local", "Test", currentContext(), Level.INFO, null, "Message", null, mapOf())

            RENDER_CLEF(event) shouldBe """{
            |"@t":"${event.timestamp.isoString}",
            |"@l":"${event.level}",
            |"host":"${event.host}",
            |"logger":"${event.logger}",
            |"context":"${event.context}",
            |"@m":"${event.message}"
            |}""".trimMargin().replace("\n", "")
        }
        it("includes @mt but not @m if `template` is included") {
            val ts = timestampNow()
            val id = randomString()
            val event = LogEvent(
                ts, "test.local", "Test", currentContext(), Level.INFO, "Id={Id}", "Id={Id}", null,
                mapOf("Id" to id)
            )

            RENDER_CLEF(event) shouldBe """{
            |"@t":"${event.timestamp.isoString}",
            |"@l":"${event.level}",
            |"host":"${event.host}",
            |"logger":"${event.logger}",
            |"Id":"$id",
            |"context":"${event.context}",
            |"@mt":"${event.template}"
            |}""".trimMargin().replace("\n", "")
        }
        it("omits context if the event `context` is null") {
            val ts = timestampNow()
            val event = LogEvent(ts, "test.local", "Test", null, Level.INFO, null, "Message", null, mapOf())

            RENDER_CLEF(event) shouldBe """{
            |"@t":"${event.timestamp.isoString}",
            |"@l":"${event.level}",
            |"host":"${event.host}",
            |"logger":"${event.logger}",
            |"@m":"${event.message}"
            |}""".trimMargin().replace("\n", "")
        }
    }
})
