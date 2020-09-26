package converter
import java.util.Scanner

class Measure(var name: String, var fullName: String, var multiName: String, var type: String, var index: Double, var shift: Double) {

    object Factory {
        fun createMeasure(unit: String): Measure {
            for (enum in WeightUnit.values()) {
                if (unit == enum.shortName || unit == enum.fullName || unit == enum.multiName) {
                    return Measure(enum.name, enum.fullName, enum.multiName, "Weight", enum.index, 0.0)
                }
            }
            for (enum in LengthUnit.values()) {
                if (unit == enum.shortName || unit == enum.fullName || unit == enum.multiName) {
                    return Measure(enum.name, enum.fullName, enum.multiName, "Length", enum.index, 0.0)
                }
            }
            for (enum in TemperatureUnit.values()) {
                if (unit == enum.shortName || unit == enum.fullName
                        || unit == enum.multiDName.toLowerCase() || unit == enum.shortDName || unit == enum.dName.toLowerCase()) {
                    return Measure(enum.name, enum.dName, enum.multiDName, "Temperature", enum.index, enum.shift)
                }
            }
            return Measure("???", "???", "???", "", 1.0, 0.0)
        }
    }

    enum class WeightUnit(val shortName: String, val fullName: String, val multiName: String, val index: Double) {
        GRAM("g", "gram", "grams", 1.0),
        KILOGRAM("kg", "kilogram", "kilograms", 1000.0),
        MILLIGRAM("mg", "milligram", "milligrams", 0.001),
        POUND("lb", "pound", "pounds", 453.592),
        OUNCE("oz", "ounce", "ounces", 28.3495)
    }

    enum class LengthUnit(val shortName: String, val fullName: String, val multiName: String, val index: Double) {
        METER("m", "meter", "meters", 1.0),
        KILOMETER("km", "kilometer", "kilometers", 1000.0),
        CENTIMETER("cm", "centimeter", "centimeters", 0.01),
        MILLIMETERS("mm", "millimeter", "millimeters", 0.001),
        MILE("mi", "mile", "miles", 1609.35),
        YARD("yd", "yard", "yards", 0.9144),
        FOOT("ft", "foot", "feet", 0.3048),
        INCH("in", "inch", "inches", 0.0254)
    }

    enum class TemperatureUnit(val shortName: String, val shortDName: String, val fullName: String, val dName: String, val multiDName: String, val index: Double, val shift: Double) {
        KELVIN("k", "k", "kelvin", "kelvin", "kelvins", 1.0, 273.15),
        FAHRENHEIT("f", "df", "fahrenheit", "degree Fahrenheit", "degrees Fahrenheit", 5.0 / 9, 32.0),
        CELSIUS("c", "dc", "celsius", "degree Celsius", "degrees Celsius", 1.0, 0.0)
    }
}

fun convertToHub(inputUnit: Measure, inputValue: Double): Double = (inputValue - inputUnit.shift) * inputUnit.index
fun convertFromHub(outputUnit: Measure, hubValue: Double): Double = hubValue / outputUnit.index + outputUnit.shift

fun isPlural(unit: Measure, value: Double) : String {
    var result = unit.multiName
    if (value.toString() == "1.0") result = unit.fullName
    return  result
}

fun convert(): Boolean {
    print("Enter what you want to convert (or exit): ")
    val scanner = Scanner(System.`in`)
    val str = scanner.next()
    var isRun = true
    if (str == "exit") {
        isRun = false
    } else {
        val inputValue: Double = str.toDouble()

        fun getMeasure(measure: String): String {
            var result = measure
            if (measure == "degree" || measure == "degrees") {
                result = measure + " " + scanner.next()
            }
            return result
        }

        val inputMeasure = getMeasure(scanner.next().toLowerCase())
        scanner.next()
        val outputMeasure = getMeasure(scanner.next().toLowerCase())

        //println("$inputValue $inputMeasure in $outputMeasure")

        val input = Measure.Factory.createMeasure(inputMeasure.toLowerCase())
        val output = Measure.Factory.createMeasure(outputMeasure.toLowerCase())

        if (input.fullName == "???" || output.fullName == "???" || input.type != output.type) {
            println("Conversion from ${input.multiName} to ${output.multiName} is impossible")
        } else if (input.type != "Temperature" && inputValue < 0) {
            println("${input.type} shouldn't be negative")
        } else {
            val targetValue = convertFromHub(output, convertToHub(input, inputValue))
            println("$inputValue ${isPlural(input, inputValue)} is $targetValue ${isPlural(output, targetValue)}")
        }
    }
    return isRun
}

fun main() {
    var isRun = true
    while (isRun) {
        isRun = convert()
    }
}
