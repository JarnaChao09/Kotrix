package org.kotrix.vector

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.types.shouldBeTypeOf
import java.lang.IllegalArgumentException

class VectorTest : StringSpec({
    /** Constructors **/
    "Vector(length) {}: Vector(5) { 0 } should be [5, 5, 5, 5, 5] of type Vector<Int>" {
        val vector = Vector(5) { 0 }

        vector.shouldContainExactly(0, 0, 0, 0, 0)
        vector.shouldBeTypeOf<Vector<Int>>()
    }

    "Vector(length, initValue): Vector(5, 10) should be [10, 10, 10, 10, 10] of type Vector<Int>" {
        val vector = Vector(5, 10)

        vector.shouldContainExactly(10, 10, 10, 10, 10)
        vector.shouldBeTypeOf<Vector<Int>>()
    }

    "Vector(list): Vector(listOf(1, 2, 3, 4)) should be [1, 2, 3, 4] of type Vector<Int>" {
        val list = listOf(1, 2, 3, 4)
        val vector = Vector(list)

        vector.shouldContainExactly(list)
        vector.shouldBeTypeOf<Vector<Int>>()
    }

    "Vector(vec): Vector(Vector(5, 0)) should be [0, 0, 0, 0, 0] of type Vector<Int>" {
        val vector = Vector(Vector(5, 0))

        vector.shouldContainExactly(0, 0, 0, 0, 0)
        vector.shouldBeTypeOf<Vector<Int>>()
    }

    /** fields **/

    "Vector(5, 0) should be size of 5" {
        Vector(5, 0).size shouldBe 5
    }

    "Vector(5, \"hello\") should be of type kotlin.String" {
        Vector(5, "hello").type shouldBe String::class
    }

    /** property functions
     * list, indices, withIndices
     */

    "Vector(5, 2).list should be List(5) { 2 } and type List<Int>" {
        val vectorList = Vector(5, 2).list

        vectorList.shouldBeInstanceOf<List<Int>>()
        vectorList.shouldContainExactly(List(5) { 2 })
    }

    "Vector(5, 2).indices should be of type IntRange and 0..4" {
        val vectorIndices = Vector(5, 2).indices

        vectorIndices.shouldBeTypeOf<IntRange>()
        vectorIndices shouldBe 0..4
    }

    "Vector(5, 2).withIndices should be of type Iterator<IndexedValue<Int>> and return (index, 5)" {
        val vectorWithIndices = Vector(5, 2).withIndices

        vectorWithIndices.shouldBeInstanceOf<Iterator<IndexedValue<Int>>>()

        var currentIndex = 0
        for ((index, value) in vectorWithIndices) {
            index shouldBe currentIndex
            currentIndex++
            value shouldBe 2
        }
    }

    /** Companion Object
     * empty, of, nulls,
     */

    "Vector.empty<String>(5) should be of size 5 and of type Vector<String>" {
        val vector = Vector.empty<String>(5)

        vector.size shouldBe 5
        vector.shouldBeTypeOf<Vector<String>>()
    }

    "Vector.empty<Int>(5) should throw IllegalArgumentException" {
        shouldThrowExactly<IllegalArgumentException> {
            Vector.empty<Int>(5)
        }
    }

    "Vector.of(1, 2, 3, 4, 5) should be of size 5, [1, 2, 3, 4, 5], and of type Vector<Int>" {
        val vector = Vector.of(1, 2, 3, 4, 5)

        vector.shouldBeTypeOf<Vector<Int>>()
        vector.shouldContainExactly(1, 2, 3, 4, 5)
        vector.size shouldBe 5
    }

    "Vector.nulls<Int>(5) should be of size 5 and of type Vector<Int>" {
        val vector = Vector.nulls<Int>(5)

        vector.size shouldBe 5
        vector.shouldBeTypeOf<Vector<Int>>()
    }

    /** Functions
     * append, push, put, remove, removeAt, appendAll
     */

    "Vector(2, 2) append 7 should be [2, 2, 7]" {
        val vector = Vector(2, 2)

        (vector append 7) shouldBe Unit
        vector.shouldContainExactly(2, 2, 7)
    }

    "Vector(2, 2) push 6 should be [6, 2, 2]" {
        val vector = Vector(2, 2)

        (vector push 6) shouldBe Unit
        vector.shouldContainExactly(6, 2, 2)
    }

    "Vector(2, 3).put(10, 1) should be [3, 10, 3]" {
        val vector = Vector(2, 3)

        vector.put(10, 1) shouldBe Unit
        vector.shouldContainExactly(3, 10, 3)
    }

    "Vector(3) { it }.remove(2) should be [0, 1]" {
        val vector = Vector(3) { it }

        vector.remove(2).shouldBeTrue()
        vector.remove(4).shouldBeFalse()
        vector.shouldContainExactly(0, 1)
    }

    "Vector(3) { it }.removeAt(1) should be [0, 2] and return 1" {
        val vector = Vector(3) { it }

        vector.removeAt(1) shouldBe 1
        vector.shouldContainExactly(0, 2)
    }

    "Vector(5, 2) appendAll Vector(2, 10) should be [2, 2, 2, 2, 2, 10, 10]" {
        val vector = Vector(5, 2)

        (vector appendAll Vector(2, 10)) shouldBe Unit
        vector.shouldContainExactly(2, 2, 2, 2, 2, 10, 10)
    }

    /** Equality
     * equals, equal
     */

    "Vector(5, 2) == Vector(5, 2) should return true" {
        (Vector(5, 2) == Vector(5, 2)).shouldBeTrue()
    }

    "Vector(5, 2) equal Vector(5, 2) should be [true, true, true, true, true]" {
        (Vector(5, 2) equal Vector(5, 2)) shouldBe BooleanVector(5) { true }
    }
})
