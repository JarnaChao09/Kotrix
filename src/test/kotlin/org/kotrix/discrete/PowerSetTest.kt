package org.kotrix.discrete

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlin.math.pow

class PowerSetTest: StringSpec({
    "PowerSet: emptyList powerSet is only emptyList" {
        setOf(emptyList<Int>().toSet()) shouldBe emptyList<Int>().toSet().powerSet
    }

    "PowerSet: P({1,2,3})" {
        setOf(1,2,3).powerSet shouldBe setOf(
                setOf(1,2,3,),
                setOf(1,2,),
                setOf(1,3,),
                setOf(2,3,),
                setOf(1,),
                setOf(2,),
                setOf(3,),
                setOf(),
        )
    }

    "PowerSet: sizeof(P({...}) is 2^n" {
        (1..6).forEach {
            val set = (1..it).toSet()
            val size = (2.0).pow(it).toInt()
            size shouldBe set.powerSet.size
            size shouldBe set.powerSetSize
        }
    }
})