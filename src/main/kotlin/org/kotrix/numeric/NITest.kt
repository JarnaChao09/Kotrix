package org.kotrix.numeric

import org.kotrix.numeric.integration.GaussLegendre
import org.kotrix.numeric.integration.NewtonCoatesIntegration
import org.kotrix.numeric.integration.RiemannIntegration
import kotlin.math.absoluteValue
import kotlin.math.sign
import kotlin.math.sin

fun main() {
    val from = 20
    val to = 21
    val leftSum = RiemannIntegration.leftSum(from, to) {
        sin(it * it)
    }
    val rightSum = RiemannIntegration.rightSum(from, to) {
        sin(it * it)
    }
    val midPointSum = RiemannIntegration.midPointSum(from, to) {
        sin(it * it)
    }
    val trapezoidalSum = NewtonCoatesIntegration.trapezoidalSum(from,  to) {
        sin(it * it)
    }
    val simpsonSum = NewtonCoatesIntegration.simpsonsMethod(from, to) {
        sin(it * it)
    }
    val adaptiveSimpsonSum = NewtonCoatesIntegration.adaptiveSimpsonMethod(from, to) {
        sin(it * it)
    }
    val gaussLegendre = GaussLegendre.integrate(from, to) {
        sin(it * it)
    }

    val actual = -0.022318845283755027
    val error: Double.(Double) -> Double = {
        (this - it).absoluteValue
    }
    println("Performing int sin(x ^ 2) dx from 20 to 21")
    println("_".repeat(70))
    println("left           = ${((if(leftSum.sign == -1.0) "" else "+")            + "$leftSum")           .padEnd(22, '0')} ERROR = ${actual.error(leftSum)}")
    println("right          = ${((if(rightSum.sign == -1.0) "" else "+")           + "$rightSum")          .padEnd(22, '0')} ERROR = ${actual.error(rightSum)}")
    println("midpoint       = ${((if(midPointSum.sign == -1.0) "" else "+")        + "$midPointSum")       .padEnd(22, '0')} ERROR = ${actual.error(midPointSum)}")
    println("trapezoidal    = ${((if(trapezoidalSum.sign == -1.0) "" else "+")     + "$trapezoidalSum")    .padEnd(22, '0')} ERROR = ${actual.error(trapezoidalSum)}")
    println("simpson        = ${((if(simpsonSum.sign == -1.0) "" else "+")         + "$simpsonSum")        .padEnd(22, '0')} ERROR = ${actual.error(simpsonSum)}")
    println("adaptive       = ${((if(adaptiveSimpsonSum.sign == -1.0) "" else "+") + "$adaptiveSimpsonSum").padEnd(22, '0')} ERROR = ${actual.error(adaptiveSimpsonSum)}")
    println("Gauss-Legendre = ${((if(gaussLegendre.sign == -1.0) "" else "+")      + "$gaussLegendre")     .padEnd(22, '0')} ERROR = ${actual.error(gaussLegendre)}")
    println("-".repeat(70))
    println("Actual Answer  = -0.022318845283755027008...")
}