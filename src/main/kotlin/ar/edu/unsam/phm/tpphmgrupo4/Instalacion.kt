package ar.edu.unsam.phm.tpphmgrupo4

abstract class Instalacion : Entity {
    override var id: Int = Entity.idInicial
    var nombre: String = ""
    var ubicacion: UbicacionMapa = UbicacionMapa("", "")
    abstract val categorias: MutableList<Ubicacion>
    abstract fun totalCapacidad(): Int
    abstract fun costoFijo(): Double
    fun costoUbicacion(tipoUbicacion: Ubicacion): Int {
        return tipoUbicacion.costo
    }
    fun obtenerCostosUbicaciones(): Map<Ubicacion, Int> {
        return categorias.associateWith { ubicacion -> ubicacion.costo }
    }
    fun ubicacionMayor(): Ubicacion {
        return categorias.first()
    }
    fun ubicacionMenor(): Ubicacion {
        return categorias.last()
    }
    fun obtenerUbicaciones(): List<Ubicacion>{
        return categorias
    }

}

class Teatro(
    val buenaAcustica: Boolean,
    var capacidadPlateaBaja: Int,
    var capacidadPullman: Int
) : Instalacion() {

    override val categorias = mutableListOf(
        Ubicacion.PlateaBaja,
        Ubicacion.Pullman
    )

    override fun totalCapacidad(): Int {
        return capacidadPlateaBaja + capacidadPullman
    }

    override fun costoFijo(): Double {
        var costoTotal = 100000.00
        if (buenaAcustica) {
            costoTotal += 50000.00
        }
        return costoTotal
    }

}

class Estadio(
    var costoFijoEstadio: Double,
    var capacidadPalco: Int,
    var capacidadCampo: Int,
    var capacidadPlateaAlta: Int
) : Instalacion() {

    override val categorias = mutableListOf(
        Ubicacion.Palco,
        Ubicacion.Campo,
        Ubicacion.PlateaAlta
    )

    override fun totalCapacidad(): Int {
        return capacidadPalco + capacidadPlateaAlta + capacidadCampo
    }

    override fun costoFijo(): Double {
        return costoFijoEstadio
    }

}