package org.kotrix.vector

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.types.shouldBeTypeOf
import org.kotrix.matrix.Matrix
import org.kotrix.utils.by
import org.kotrix.utils.sliceTo
import kotlin.IllegalArgumentException

class VectorTest : StringSpec({
    /** Constructors **/
    "Vector(length) {}: Vector(5) { 0 } should be [0, 0, 0, 0, 0] of type Vector<Int>" {
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
     * list, indices, withIndices, type
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

    "Vector(5, 2).type should be Int, Vector(5, \" \").type should be String" {
        Vector(5, 2).type shouldBe Int::class
        Vector(5, " ").type shouldBe String::class
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

    "Vector<Int>(5) should be size 5 and of type Vector<Int>" {
        val vector = Vector<Int>(size = 5)

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

    /** Get Set **/

    "Vector.of(1, 2, 3, 4, 5)[2] should be 3" {
        Vector.of(1, 2, 3, 4, 5)[2] shouldBe 3
    }

    "Vector.of(1, 2, 3, 4, 5)[-1] should be 5" {
        Vector.of(1, 2, 3, 4, 5)[-1] shouldBe 5
    }

    "Vector.of(1, 2, 3, 4, 5)[1 sliceTo 4] should be [2, 3, 4]" {
        val vector = Vector.of(1, 2, 3, 4, 5)[1 sliceTo 4]

        vector.shouldContainExactly(2, 3, 4)
        vector.shouldBeTypeOf<Vector<Int>>()
        vector.size shouldBe 3
    }

    "Vector.of(1, 2, 3, 4, 5)[2] = 10 should be [1, 2, 10, 4, 5]" {
        val vector = Vector.of(1, 2, 3, 4, 5)

        vector[2] = 10

        vector.shouldContainExactly(1, 2, 10, 4, 5)
    }

    "Vector.of(1, 2, 3, 4, 5)[1 sliceTo 4] = Vector.of(10, 11, 12) should be [1, 10, 11, 12, 13]" {
        val vector = Vector.of(1, 2, 3, 4, 5)

        vector[1 sliceTo 4] = Vector.of(10, 11, 12)

        vector.shouldContainExactly(1, 10, 11, 12, 5)
    }

    "Vector.of(1, 2, 3, 4, 5)[1 sliceTo 4] = Vector.of(10, 11, 12, 13) should throw IllegalArgumentException" {
        shouldThrowExactly<IllegalArgumentException> {
            val vector = Vector.of(1, 2, 3, 4, 5)
            vector[1 sliceTo 4] = Vector.of(10, 11, 12, 13)
            vector
        }
    }

    /** forEach, forEachIndexed, map, mapIndexed **/

    "Testing Vector.forEach { }" {
        val fakeVector = listOf(1, 2, 3, 4, 5)
        val vector = Vector(fakeVector)

        var index = 0
        val returned = vector.forEach {
            it shouldBe fakeVector[index++]
        }

        returned shouldBe Unit
    }

    "Testing Vector.forEachIndexed { }" {
        val fakeVector = listOf(1, 2, 3, 4, 5)
        val vector = Vector(fakeVector)

        val returned = vector.forEachIndexed { index, i ->
            i shouldBe fakeVector[index]
        }

        returned shouldBe Unit
    }

    "Testing Vector.map { }" {
        val vector = Vector.of(1, 2, 3, 4, 5)

        val mappedVector = vector.map {
            it * 2
        }

        mappedVector.shouldBeTypeOf<Vector<Int>>()
        mappedVector.shouldContainExactly(2, 4, 6, 8, 10)
    }

    "Testing Vector.mapIndexed { }" {
        val vector = Vector.of(1, 2, 3, 4, 5)

        val mappedVector = vector.mapIndexed { index: Int, i: Int ->
            i * 2 + index
        }

        mappedVector.shouldBeTypeOf<Vector<Int>>()
        mappedVector.shouldContainExactly(2, 5, 8, 11, 14)
    }

    /** Conversion
     * Matrix, List, toVector, toIntVector, toDoubleVector
     */

    "Vector.of(1, 2, 3, 4, 5).toMatrix(asCol = false)" {
        Vector.of(1, 2, 3, 4, 5).toMatrix(asCol = false) shouldBe Matrix(1 by 5, asCols = false) { it + 1 }

    }

    "Vector.of(1, 2, 3, 4, 5).toMatrix(asCol = true)" {
        Vector.of(1, 2, 3, 4, 5).toMatrix(asCol = true) shouldBe Matrix(5 by 1, asCols = true) { it + 1 }
    }

    "Vector.of(1, 2, 3, 4, 5).list / .toList()" {
        Vector.of(1, 2, 3, 4, 5).toList() shouldBe listOf(1, 2, 3, 4, 5,)
    }

    "listOf(1, 2, 3, 4, 5).toVector()" {
        val vec = listOf(1, 2, 3, 4, 5).toVector()
        vec shouldBe Vector.of(1,2,3,4,5)
        vec.shouldBeTypeOf<Vector<Int>>()
    }

    "Vector.of(1, 2, 3, 4, 5).toIntVector()" {
        val vec = Vector.of(1,2,3,4,5).toIntVector()
        vec shouldBe IntVector.of(1,2,3,4,5)
        vec.shouldBeTypeOf<IntVector>()
    }

    "Vector.of(1.0, 2.0, 3.0, 4.0, 5.0).toDoubleVector()" {
        val vec = Vector.of(1.0, 2.0, 3.0, 4.0, 5.0).toDoubleVector()
        vec shouldBe DoubleVector.of(1,2,3,4,5)
        vec.shouldBeTypeOf<DoubleVector>()
    }

    /** Contains
     * in / contains, in / containsAll
     */

    "3 in Vector.of(1, 2, 3, 4, 5) should be true" {
        (3 in Vector.of(1, 2, 3, 4, 5)).shouldBeTrue()
    }

    "10 in Vector.of(1, 2, 3, 4, 5) should be false" {
        (10 in Vector.of(1, 2, 3, 4, 5)).shouldBeFalse()
    }

    "Vector.of(2, 3, 4) in Vector.of(1, 2, 3, 4, 5) should be true" {
        (Vector.of(2, 3, 4) in Vector.of(1, 2, 3, 4, 5)).shouldBeTrue()
    }

    "Vector.of(10, 11) in Vector.of(1, 2, 3, 4, 5) should be false" {
        (Vector.of(10, 11) in Vector.of(1, 2, 3, 4, 5)).shouldBeFalse()
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
