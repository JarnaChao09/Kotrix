package org.kotrix.complex

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import org.kotrix.testing.utils.MAX_TOLERANCE

/**
 * Test class for org.kotrix.complex.Complex && org.kotrix.complex.CMath
 */
class ComplexTest: StringSpec({
    /** Constructors **/

    "Complex(Double, Double): Complex(1.0, 2.0) should be 1 + 2i" {
        val complex = Complex(1.0, 2.0)

        complex.real shouldBe (1.0 plusOrMinus MAX_TOLERANCE)
        complex.real.shouldBeTypeOf<Double>()

        complex.imag shouldBe (2.0 plusOrMinus MAX_TOLERANCE)
        complex.imag.shouldBeTypeOf<Double>()
    }

    "Complex(Number, NUmber): Complex(1 as Number, 2 as Number) should be 1 + 2i" {
        Complex(1 as Number, 2 as Number) shouldBe Complex(1.0, 2.0)
    }

    "Complex(Complex, Number): Complex(Complex(3, 4), 1) should be 3 + 5i" {
        Complex(Complex(3, 4), 1) shouldBe Complex(3, 5)
    }

    "Complex(Number, Complex): Complex(1, Complex(3, 4)) should be -3 + 3i" {
        Complex(1, Complex(3, 4)) shouldBe Complex(-3, 3)
    }

    "Complex(Number): Complex(10) should be 10 + 0i" {
        Complex(10) shouldBe Complex(10, 0)
    }

    "Complex(Complex): Complex(Complex(1, 2)) should be 1 + 2i" {
        Complex(Complex(1, 2)) shouldBe Complex(1, 2)
    }

    "Complex.Companion.ZERO should be 0.0 + 0.0i" {
        Complex.ZERO shouldBe Complex(0, 0)
    }

    "Complex.Companion.ONE should be 1.0 + 0.0i" {
        Complex.ONE shouldBe Complex(1, 0)
    }

    "Complex.reciprocal of 1 + 1i should be 0.5 + -0.5i" {
        Complex(1, 1).reciprocal shouldBe Complex(0.5, -0.5)
    }

    "Complex.conjugate of 1.0 + 2.0i should be 1.0 - 2.0i" {
        Complex(1, 2).conjugate shouldBe Complex(1, -2)
    }

    "Complex.radius of 3 + 4i should be 5.0" {
        Complex(3, 4).radius shouldBe 5.0
        Complex(3, 4).radius.shouldBeTypeOf<Double>()
    }

    /** Complex Basic Operators **/

    /** Unary Plus and Unary Minus **/

    "Complex.unaryPlus of 1.0 + 1.0i should be 1.0 + 1.0i" {
        +Complex(1, 1) shouldBe Complex(1, 1)
    }

    "Complex.unaryMinus of 1.0 + 1.0i should be -1.0 + -1.0i" {
        -Complex(1, 1) shouldBe Complex(-1, -1)
    }

    /** Complex Basic Arithmetic + - * / **/

    "Complex.plus: (1.0 + 1.0i) + (2.0 + -1.0i) should be 3.0 + 0.0i" {
        Complex(1, 1) + Complex(2, -1) shouldBe Complex(3, 0)
    }

    "Complex.minus: (1.0 + 1.0i) - (2.0 + -1.0i) should be -1.0 + 2.0i" {
        Complex(1, 1) - Complex(2, -1) shouldBe Complex(-1, 2)
    }

    "Complex.times: (1.0 + 1.0i) * (2.0 + 3.0i) should be -1.0 + 5i" {
        Complex(1, 1) * Complex(2, 3) shouldBe Complex(-1, 5)
    }

    "Complex.div: (2.0 + 4.0i) / (6.0 + 8.0i) should be 0.44 + 0.08i" {
        Complex(2, 4) / Complex(6, 8) shouldBe Complex(0.44, 0.08)
    }

    /** Complex Advance Arithmetic
     * power, root, natural log, trigonometry
     */

    /** Power **/

    "Complex.pow(Double): (3 + 4i) ^ 3 should be -117 + 44i" {
        Complex(3, 4).pow(3.0) shouldBe Complex(-117, 44)
    }

    "Complex.pow(Double): (3 + 4i) ^ 3 should be CMath.pow(3 + 4i, 3)" {
        Complex(3, 4).pow(3.0) shouldBe CMath.pow(Complex(3, 4), 3.0)
    }

    "Complex.pow(Complex): (3 + 4i) ^ (1 + 2i) should be approximately -0.4198131755619574 + -0.6604516942073323i" {
        Complex(3, 4).pow(Complex(1, 2)) shouldBe
                Complex(-0.4198131755619574, -0.6604516942073323)
    }

    "Complex.pow(Complex): (3 + 4i) ^ (1 + 2i) should be CMath.pow(3 + 4i, 1 + 2i)" {
        Complex(3, 4).pow(Complex(1, 2)) shouldBe CMath.pow(Complex(3, 4), Complex(1, 2))
    }

    "Complex.squared: (3 + 4i) ^ 2 should be -7 + 24i" {
        Complex(3, 4).squared shouldBe Complex(-7, 24)
    }

    /** Root **/

    "Complex.rootN(Double): (3 + 4i).rootN(3) should be approximately 1.6289371459221758 + 0.5201745023045458i" {
        Complex(3, 4).rootN(3.0) shouldBe Complex(1.6289371459221758, 0.5201745023045458)
    }

    "Complex.rootN(Double): (3 + 4i).rootN(3) should be (3 + 4i) ^ (1 / 3)" {
        Complex(3, 4).rootN(3.0) shouldBe Complex(3, 4).pow(1.0 / 3.0)
    }

    "Complex.rootN(Double): (3 + 4i).rootN(3) should be CMath.rootN(3 + 4i, 3)" {
        Complex(3, 4).rootN(3.0) shouldBe CMath.rootN(Complex(3, 4), 3.0)
    }

    "Complex.sqrt: (3 + 4i).sqrt should be 2 + i" {
        Complex(3, 4).sqrt shouldBe Complex(2, 1)
    }

    "Complex.sqrt: (3 + 4i).sqrt should be (3 + 4i).rootN(2)" {
        Complex(3, 4).sqrt shouldBe Complex(3, 4).rootN(2.0)
    }

    "Complex.sqrt: (3 + 4i).sqrt should be (3 + 4i) ^ (1 / 2)" {
        Complex(3, 4).sqrt shouldBe Complex(3, 4).pow(1.0 / 2.0)
    }

    "Complex.sqrt: (3 + 4i).sqrt should be CMath.sqrt(3 + 4i)" {
        Complex(3, 4).sqrt shouldBe CMath.sqrt(Complex(3, 4))
    }

    "Complex.cbrt: (3 + 4i).cbrt should be approximately 1.6289371459221758 + 0.5201745023045458i" {
        Complex(3, 4).cbrt shouldBe Complex(1.6289371459221758, 0.5201745023045458)
    }

    "Complex.cbrt: (3 + 4i).cbrt should be (3 + 4i).rootN(3)" {
        Complex(3, 4).cbrt shouldBe Complex(3, 4).rootN(3.0)
    }

    "Complex.cbrt: (3 + 4i).cbrt should be (3 + 4i) ^ (1 / 3)" {
        Complex(3, 4).cbrt shouldBe Complex(3, 4).pow(1.0 / 3.0)
    }

    "Complex.cbrt: (3 + 4i).cbrt should be CMath.cbrt(3 + 4i)" {
        Complex(3, 4).cbrt shouldBe CMath.cbrt(Complex(3, 4))
    }

    /** Natural Log **/

    "Complex.ln: (3 + 4i).ln should be approximately 1.6094379124341003 + 0.9272952180016122i" {
        val ln = Complex(3, 4).ln
        ln.real shouldBe
                (1.60943791243410037460075933322618763952560135426851772191264789 plusOrMinus MAX_TOLERANCE)
        ln.imag shouldBe
                (0.927295218001612232428512462922428804057074108572240527621866177 plusOrMinus MAX_TOLERANCE)
    }

    "Complex.ln: (3 + 4i).ln should be CMath.ln(3 + 4i)" {
        Complex(3, 4).ln shouldBe CMath.ln(Complex(3, 4))
    }

    /** Trigonometry
     * all values checked with wolframalpha
     */

    "Complex.cos: (3 + 4i).cos should be approximately -27.0349456030742246 + -3.851153334811777i" {
        val cos = Complex(3, 4).cos
        cos.real shouldBe
                (-27.0349456030742246476948026682709134846775369556766166101926551 plusOrMinus MAX_TOLERANCE)
        cos.imag shouldBe
                (-3.85115333481177753656333712305312456970416084609163700315772859 plusOrMinus MAX_TOLERANCE)
    }

    "Complex.cos: (3 + 4i).cos should be CMath.cos(3 + 4i)" {
        Complex(3, 4).cos shouldBe CMath.cos(Complex(3, 4))
    }

    "Complex.sin: (3 + 4i).sin should be approximately 3.853738037919377 + -27.01681325800392i" {
        val sin = Complex(3, 4).sin
        sin.real shouldBe
                (3.853738037919377321617528940463730667068274946989034956763346803 plusOrMinus MAX_TOLERANCE)
        sin.imag shouldBe
                (-27.01681325800393448809754375499215226336386568976518470594798897 plusOrMinus MAX_TOLERANCE)
    }

    "Complex.sin: (3 + 4i).sin should be CMath.sin(3 + 4i)" {
        Complex(3, 4).sin shouldBe CMath.sin(Complex(3, 4))
    }

    "Complex.tan: (3 + 4i).tan should be approximately -1.8734620462949035E-4 + 0.9993559873814731i" {
        val tan = Complex(3, 4).tan
        tan.real shouldBe
                (-0.00018734620462947842622425563772821810421242424272966062635808 plusOrMinus MAX_TOLERANCE)
        tan.imag shouldBe
                (0.99935598738147314139164963032013306156488850281353849283197 plusOrMinus MAX_TOLERANCE)
    }

    "Complex.tan: (3 + 4i).tan should be CMath.tan(3 + 4i)" {
        Complex(3, 4).tan shouldBe CMath.tan(Complex(3, 4))
    }

    "Complex.csc: (3 + 4i).csc should be approximately 0.005174473184019398 + 0.03627588962862602i" {
        val csc = Complex(3, 4).csc
        csc.real shouldBe
                (0.005174473184019397654122515195225072331642963785821395721304638 plusOrMinus MAX_TOLERANCE)
        csc.imag shouldBe
                (0.03627588962862601159419844899069413563248848863615106636517440 plusOrMinus MAX_TOLERANCE)
    }

    "Complex.csc: (3 + 4i).csc should be CMath.csc(3 + 4i)" {
        Complex(3, 4).csc shouldBe CMath.csc(Complex(3, 4))
    }

    "Complex.sec: (3 + 4i).sec should be approximately -0.03625349691586887 + 0.005164344607753179i" {
        val sec = Complex(3, 4).sec
        sec.real shouldBe
                (-0.0362534969158688718908516438864362506037681947896429624281228 plusOrMinus MAX_TOLERANCE)
        sec.imag shouldBe
                (0.00516434460775317936741467656259054231664676463920830664120378 plusOrMinus MAX_TOLERANCE)
    }

    "Complex.sec: (3 + 4i).sec should be CMath.sec(3 + 4i)" {
        Complex(3, 4).sec shouldBe CMath.sec(Complex(3, 4))
    }

    "Complex.cot: (3 + 4i).cot should be approximately -1.8758773798367115E-4 + -1.0006443924715591i" {
        val cot = Complex(3, 4).cot
        cot.real shouldBe
                (-0.0001875877379836592156285046624642335547256484652930978 plusOrMinus MAX_TOLERANCE)
        cot.imag shouldBe
                (-1.000644392471559080098184707843079489388342277009128 plusOrMinus MAX_TOLERANCE)
    }

    "Complex.cot: (3 + 4i).cot should be CMath.cot(3 + 4i)" {
        Complex(3, 4).cot shouldBe CMath.cot(Complex(3, 4))
    }

    "Complex.sinh: (3 + 4i).sinh should be approximately -6.5481200409110025 + -7.61923172032141i" {
        val sinh = Complex(3, 4).sinh
        sinh.real shouldBe
                (-6.5481200409110016477668110188353247408208883968885834997 plusOrMinus MAX_TOLERANCE)
        sinh.imag shouldBe
                (-7.6192317203214102084871357368043117965572654726755756194 plusOrMinus MAX_TOLERANCE)
    }

    "Complex.sinh: (3 + 4i).sinh should be CMath.sinh(3 + 4i)" {
        Complex(3, 4).sinh shouldBe CMath.sinh(Complex(3, 4))
    }

    "Complex.cosh: (3 + 4i).cosh should be approximately -6.580663040551157 + -7.581552742746545i" {
        val cosh = Complex(3, 4).cosh
        cosh.real shouldBe
                (-6.58066304055115643256074412653880361671126734551589777322021832 plusOrMinus MAX_TOLERANCE)
        cosh.imag shouldBe
                (-7.58155274274654435371634528653842600938752759094885281294936345 plusOrMinus MAX_TOLERANCE)
    }

    "Complex.cosh: (3 + 4i).cosh should be CMath.cosh(3 + 4i)" {
        Complex(3, 4).cosh shouldBe CMath.cosh(Complex(3, 4))
    }

    "Complex.tanh: (3 + 4i).tanh should be approximately 1.000709536067233 + 0.004908258067495992i" {
        val tanh = Complex(3, 4).tanh
        tanh.real shouldBe
                (1.00070953606723293932958547240417274621532090514676021 plusOrMinus MAX_TOLERANCE)
        tanh.imag shouldBe
                (0.00490825806749606025907878692993276684337421557935550697 plusOrMinus MAX_TOLERANCE)
    }

    "Complex.tanh: (3 + 4i).tanh should be CMath.tanh(3 + 4i)" {
        Complex(3, 4).tanh shouldBe CMath.tanh(Complex(3, 4))
    }

    "Complex.csch: (3 + 4i).csch should be approximately -0.0648774713706355 + 0.0754898329158637i" {
        val csch = Complex(3, 4).csch
        csch.real shouldBe
                (-0.0648774713706354904834951031356993605742927815534409286 plusOrMinus MAX_TOLERANCE)
        csch.imag shouldBe
                (0.0754898329158636995715437557488176324549032098801379746 plusOrMinus MAX_TOLERANCE)
    }

    "Complex.csch: (3 + 4i).csch should be CMath.csch(3 + 4i)" {
        Complex(3, 4).csch shouldBe CMath.csch(Complex(3, 4))
    }

    "Complex.sech: (3 + 4i).sech should be approximately -0.06529402785794704 + 0.07522496030277322i" {
        val sech = Complex(3, 4).sech
        sech.real shouldBe
                (-0.0652940278579470464451582064571811981581910629727511683 plusOrMinus MAX_TOLERANCE)
        sech.imag shouldBe
                (0.0752249603027732268655771485144758384369873583174336247 plusOrMinus MAX_TOLERANCE)
    }

    "Complex.sech: (3 + 4i).sech should be CMath.sech(3 + 4i)" {
        Complex(3, 4).sech shouldBe CMath.sech(Complex(3, 4))
    }

    "Complex.coth: (3 + 4i).coth should be approximately 0.9992669278059015 + -0.0049011823943044056i" {
        val coth = Complex(3, 4).coth
        coth.real shouldBe
                (0.999266927805901544522439364343414619245880656435552460 plusOrMinus MAX_TOLERANCE)
        coth.imag shouldBe
                (-0.00490118239430447335983693129141071485787285241304776953 plusOrMinus MAX_TOLERANCE)
    }

    "Complex.coth: (3 + 4i).coth should be CMath.coth(3 + 4i)" {
        Complex(3, 4).coth shouldBe CMath.coth(Complex(3, 4))
    }

    "Complex.arccos: (3 + 4i).arccos should be approximately 0.9368124611557265 + -2.305509031243474i" {
        val arccos = Complex(3, 4).arccos
        arccos.real shouldBe
                (0.93681246115571990291252457657560891648718122901434482330 plusOrMinus MAX_TOLERANCE)
        arccos.imag shouldBe
                (-2.3055090312434769420418359381334308973290823461276643442 plusOrMinus MAX_TOLERANCE)
    }

    "Complex.arccos: (3 + 4i).arccos should be CMath.arccos(3 + 4i)" {
        Complex(3, 4).arccos shouldBe CMath.arccos(Complex(3, 4))
    }

    "Complex.arcsin: (3 + 4i).arcsin should be approximately 0.6339838656391701 + 2.305509031243474i" {
        val arcsin = Complex(3, 4).arcsin
        arcsin.real shouldBe
                (0.633983865639176716318797115064142525611403470673208087183024371 plusOrMinus MAX_TOLERANCE)
        arcsin.imag shouldBe
                (2.30550903124347694204183593813343089732908234612766434427244403 plusOrMinus MAX_TOLERANCE)
    }

    "Complex.arcsin: (3 + 4i).sin should be CMath.arcsin(3 + 4i)" {
        Complex(3, 4).arcsin shouldBe CMath.arcsin(Complex(3, 4))
    }

    "Complex.arctan: (3 + 4i).arctan should be approximately 1.4483069952314644 + 0.15899719167999926i" {
        val arctan = Complex(3, 4).arctan
        arctan.real shouldBe
                (1.44830699523146454214528045103411353664151265049696087692378433 plusOrMinus MAX_TOLERANCE)
        arctan.imag shouldBe
                (0.158997191679999174364761036007018781573305474235061470956962267 plusOrMinus MAX_TOLERANCE)
    }

    "Complex.arctan: (3 + 4i).arctan should be CMath.arctan(3 + 4i)" {
        Complex(3, 4).arctan shouldBe CMath.arctan(Complex(3, 4))
    }

    "Complex.arccsc: (3 + 4i).arccsc should be approximately 0.11875073130741173 + -0.160445533774505i" {
        val arccsc = Complex(3, 4).arccsc
        arccsc.real shouldBe
                (0.118750731307411754202256979525563184131426896053703539666067734 plusOrMinus MAX_TOLERANCE)
        arccsc.imag shouldBe
                (-0.160445533774504932399834428862345381236603018283851830270210371 plusOrMinus MAX_TOLERANCE)
    }

    "Complex.arccsc: (3 + 4i).arccsc should be CMath.arccsc(3 + 4i)" {
        Complex(3, 4).arccsc shouldBe CMath.arccsc(Complex(3, 4))
    }

    "Complex.arcsec: (3 + 4i).arcsec should be approximately 1.452045595487485 + 0.160445533774505i" {
        val arcsec = Complex(3, 4).arcsec
        arcsec.real shouldBe
                (1.45204559548748486502906471211418825796715780363384937082140456 plusOrMinus MAX_TOLERANCE)
        arcsec.imag shouldBe
                (0.160445533774504932399834428862345381236603018283851830270210371 plusOrMinus MAX_TOLERANCE)
    }

    "Complex.arcsec: (3 + 4i).arcsec should be CMath.arcsec(3 + 4i)" {
        Complex(3, 4).arcsec shouldBe CMath.arcsec(Complex(3, 4))
    }

    "Complex.arccot: (3 + 4i).arccot should be approximately 0.12248933156343207-4 + -0.1589971916799992i" {
        val arccot = Complex(3, 4).arccot
        arccot.real shouldBe
                (0.122489331563432077086041240605637905457072049190592033563687957 plusOrMinus MAX_TOLERANCE)
        arccot.imag shouldBe
                (-0.158997191679999174364761036007018781573305474235061470956962267 plusOrMinus MAX_TOLERANCE)
    }

    "Complex.arccot: (3 + 4i).arccot should be CMath.arccot(3 + 4i)" {
        Complex(3, 4).arccot shouldBe CMath.arccot(Complex(3, 4))
    }

    "Complex.arcsinh: (3 + 4i).arcsinh should be approximately 2.2999140408792695 + 0.9176168533514787i" {
        val arcsinh = Complex(3, 4).arcsinh
        arcsinh.real shouldBe
                (2.29991404087926964995578963066317555536531348476463646661183008 plusOrMinus MAX_TOLERANCE)
        arcsinh.imag shouldBe
                (0.917616853351478655759862748670174541589952382036230002777364760 plusOrMinus MAX_TOLERANCE)
    }

    "Complex.arcsinh: (3 + 4i).arcsinh should be CMath.arcsinh(3 + 4i)" {
        Complex(3, 4).arcsinh shouldBe CMath.arcsinh(Complex(3, 4))
    }

    "Complex.arccosh: (3 + 4i).arccosh should be approximately 2.305509031243477 + 0.9368124611557199i" {
        val arccosh = Complex(3, 4).arccosh
        arccosh.real shouldBe
                (2.30550903124347694204183593813343089732908234612766434427244403 plusOrMinus MAX_TOLERANCE)
        arccosh.imag shouldBe
                (0.936812461155719902912524576575608916487181229014344823304447924 plusOrMinus MAX_TOLERANCE)
    }

    "Complex.arccosh: (3 + 4i).arccosh should be CMath.arccosh(3 + 4i)" {
        Complex(3, 4).arccosh shouldBe CMath.arccosh(Complex(3, 4))
    }

    "Complex.arctanh: (3 + 4i).arctanh should be approximately 0.1175009073114339 + 1.4099210495965755i" {
        val arctanh = Complex(3, 4).arctanh
        arctanh.real shouldBe
                (0.117500907311433888412734257787085516175224762203062010112348034 plusOrMinus MAX_TOLERANCE)
        arctanh.imag shouldBe
                (1.40992104959657552253061938446042078258820705190872481477107076 plusOrMinus MAX_TOLERANCE)
    }

    "Complex.arctanh: (3 + 4i).arctanh should be CMath.arctanh(3 + 4i)" {
        Complex(3, 4).arctanh shouldBe CMath.arctanh(Complex(3, 4))
    }

    "Complex.arccsch: (3 + 4i).arccsch should be approximately 0.12124561370968746 + -0.15950663187736358i" {
        val arccsch = Complex(3, 4).arccsch
        arccsch.real shouldBe
                (0.121245613709687454266309771040125263644826412562911312632231515 plusOrMinus MAX_TOLERANCE)
        arccsch.imag shouldBe
                (-0.159506631877363569499669255250948297490258948036765337849704049 plusOrMinus MAX_TOLERANCE)
    }

    "Complex.arccsch: (3 + 4i).arccsch should be CMath.arccsch(3 + 4i)" {
        Complex(3, 4).arccsch shouldBe CMath.arccsch(Complex(3, 4))
    }

    "Complex.arcsech: (3 + 4i).arcsech should be approximately 0.160445533774505 + -1.452045595487485i" {
        val arcsech = Complex(3, 4).arcsech
        arcsech.real shouldBe
                (0.160445533774504932399834428862345381236603018283851830270210371 plusOrMinus MAX_TOLERANCE)
        arcsech.imag shouldBe
                (-1.45204559548748486502906471211418825796715780363384937082140456 plusOrMinus MAX_TOLERANCE)
    }

    "Complex.arcsech: (3 + 4i).arcsech should be CMath.arcsech(3 + 4i)" {
        Complex(3, 4).arcsech shouldBe CMath.arcsech(Complex(3, 4))
    }

    "Complex.arccoth: (3 + 4i).arccoth should be approximately 0.11750090731143407 + -0.1608752771983211i" {
        val arccoth = Complex(3, 4).arccoth
        arccoth.real shouldBe
                (0.117500907311433888412734257787085516175224762203062010112348034 plusOrMinus MAX_TOLERANCE)
        arccoth.imag shouldBe
                (-0.160875277198321096700702307179330659510377647778828095716401529 plusOrMinus MAX_TOLERANCE)
    }

    "Complex.arccoth: (3 + 4i).arccoth should be CMath.arccoth(3 + 4i)" {
        Complex(3, 4).coth shouldBe CMath.coth(Complex(3, 4))
    }
})