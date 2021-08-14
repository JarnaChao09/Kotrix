package org.kotrix.symbolic.funAST

//import org.kotrix.vector.map
//
//private typealias FunVector = org.kotrix.vector.VectorImpl<Fun>
//
//data class Vector(val vector: FunVector): Fun() {
//    override fun simplify(): Fun = Vector(this.vector.map {
//        it.simplify()
//    })
//
//    override fun sub(replace: Variable, with: Fun): Fun = Vector(this.vector.map {
//        it.sub(replace, with)
//    })
//
//    override fun stringify(): String = this.vector.map { it.stringify() }.toString()
//
//    override fun diff(by: Variable): Fun = Vector(this.vector.map { it.diff(by) })
//
//    override fun partialEval(value: Map<Fun, Scalar>): Fun {
//        TODO("Not yet implemented, rework partialEval to work with Vector type")
//    }
//
//    override fun fullEval(value: Map<Fun, Scalar>): Double {
//        TODO("Not yet implemented, rework fullEval to work with Vector type")
//    }
//
//}
