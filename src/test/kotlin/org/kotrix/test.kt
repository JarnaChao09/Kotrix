package org.kotrix

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.startWith

/**
 * Testing out Kotest framework and Kotest Plugin
 */
class MyTests : StringSpec({
    "length should return size of string" {
        "hello".length shouldBe 5
    }
    "startsWith should test for a prefix" {
        "world" should startWith("wor")
    }
    "Testing double matcher with tolerance" {
        10.00000000001.shouldBe(10.0 plusOrMinus 1E-10)
    }
})