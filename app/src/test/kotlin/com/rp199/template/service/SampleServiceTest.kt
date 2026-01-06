package com.rp199.template.service

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class SampleServiceTest : ShouldSpec({

    should("do stuff"){
        // given
        val input = "test input"
        // when
        val result = SampleService().doStuff(input)

        // then
        result shouldBe SampleResponse(input)
    }
})
