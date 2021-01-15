package org.kotrix.discrete

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.forAll
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.next

class CartesianProductTest: StringSpec({
    val listGen = Arb.list(Arb.int(0..100), 2..5)

    val listListGen = Arb.list(listGen, 2..5)

    "Cartesian Product: output size" {
        forAll(listListGen) { list ->
            val head = list[0]
            val tail = list.drop(1)
            head.cartesianProduct(*tail.toTypedArray())
                    .toList().size == list.map {
                it.size
            }.reduce(Int::times)
        }
    }

    "Cartesian Product: cartesian product of emptyList repeated is emptyList" {
        forAll(Arb.int(2..10)) { repeat ->
            emptyList<Int>().cartesianProduct(repeat).toList().isEmpty()
        }
    }

    "Cartesian Product: cartesian product of emptyList on non-emptyList is emptyList" {
        forAll(listGen) { int ->
            emptyList<Int>().cartesianProduct(int).toList().isEmpty()
        }
    }

    "Cartesian Product: cartesian product of non-emptyList on emptyList is emptyList" {
        forAll(listGen) { list ->
            list.cartesianProduct(emptyList()).toList().isEmpty()
        }
    }

    "Cartesian Product: set of all elements are the same" {
        forAll(listListGen) { list ->
            val (head, tail) = list[0] to list.drop(1)
            head.cartesianProduct(*tail.toTypedArray()).toList().flatten().toSet() ==
                    list.flatten().toSet()
        }
    }

    "Cartesian Product: IllegalArgumentException, size exceeds Int.MAX_VALUE" {
        shouldThrowExactly<IllegalArgumentException> {
            listOf(0,1,2,3,4,5,6,7,8,9).cartesianProduct(10).toList()
        }
    }

    "Cartesian Product: repeat value less than 1 should throw IllegalArgumentException" {
        shouldThrowExactly<IllegalArgumentException> {
            val repeat = Arb.int(-100..0).next()
            listOf(1,2,3).cartesianProduct(repeat)
        }
    }

    "Cartesian Product: repeat = 1" {
        listOf(1, 2, 3).cartesianProduct(1).toList() shouldBe listOf(
                listOf(1),
                listOf(2),
                listOf(3),
        )
    }

    "Cartesian Product: repeat = 2" {
        listOf(1,2,3).cartesianProduct(2).toList() shouldBe listOf(
                listOf(1,1),
                listOf(1,2),
                listOf(1,3),
                listOf(2,1),
                listOf(2,2),
                listOf(2,3),
                listOf(3,1),
                listOf(3,2),
                listOf(3,3),
        )
    }

    "Cartesian Product: listOf(1,2,3) cartesian product listOf(8, 9)" {
        listOf(1,2,3).cartesianProduct(listOf(8,9)).toList() shouldBe listOf(
                listOf(1,8),
                listOf(1,9),
                listOf(2,8),
                listOf(2,9),
                listOf(3,8),
                listOf(3,9),
        )
    }

    "Cartesian Product: listOf(1,2) cartesian product listOf(8,9)" {
        listOf(1,2).cartesianProduct(listOf(8,9)).toList() shouldBe listOf(
                listOf(1,8),
                listOf(1,9),
                listOf(2,8),
                listOf(2,9),
        )
    }

    "Cartesian Product: listOf(1,2) cartesian product listOf(3,4), listOf(5,6)" {
        listOf(1,2,).cartesianProduct(listOf(3,4,),listOf(5,6,),).toList() shouldBe listOf(
                listOf(1,3,5,),
                listOf(1,3,6,),
                listOf(1,4,5,),
                listOf(1,4,6,),
                listOf(2,3,5,),
                listOf(2,3,6,),
                listOf(2,4,5,),
                listOf(2,4,6,),
        )
    }

    "Cartesian Product: listOf(0,1,2) cartesian product listOf(3,4), listOf(5,6)" {
        listOf(0,1,2,).cartesianProduct(listOf(3,4,),listOf(5,6,),).toList() shouldBe listOf(
                listOf(0,3,5),
                listOf(0,3,6),
                listOf(0,4,5),
                listOf(0,4,6),
                listOf(1,3,5),
                listOf(1,3,6),
                listOf(1,4,5),
                listOf(1,4,6),
                listOf(2,3,5),
                listOf(2,3,6),
                listOf(2,4,5),
                listOf(2,4,6),
        )
    }

    "Cartesian Product: listOf(1,2,3) cartesian product listOf(\"A\", \",B\")" {
        listOf(1,2,3,).cartesianProduct(listOf("A","B",)).toList() shouldBe listOf(
                listOf(1,"A"),
                listOf(1,"B"),
                listOf(2,"A"),
                listOf(2,"B"),
                listOf(3,"A"),
                listOf(3,"B"),
        )
    }
})