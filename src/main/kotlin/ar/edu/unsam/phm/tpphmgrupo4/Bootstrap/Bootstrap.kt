package ar.edu.unsam.phm.tpphmgrupo4.Bootstrap

import ar.edu.unsam.phm.tpphmgrupo4.*
import ar.edu.unsam.phm.tpphmgrupo4.repositorio.Repositorio
import ar.edu.unsam.phm.tpphmgrupo4.repositorio.RepositorioEntrada
import ar.edu.unsam.phm.tpphmgrupo4.repositorio.RepositorioInstalacion
import ar.edu.unsam.phm.tpphmgrupo4.repositorio.RepositorioUsuario
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime

@Service
class Bootstrap : InitializingBean {

    @Autowired
    lateinit var showRepository: Repositorio<Show>

    @Autowired
    lateinit var usuarioRepository: RepositorioUsuario

    @Autowired
    lateinit var instalacionRepository: RepositorioInstalacion

    @Autowired
    lateinit var entradaRepository: RepositorioEntrada

    lateinit var usuarioAlan: UsuarioComun
    lateinit var usuarioStefan: UsuarioComun
    lateinit var usuarioCeci: UsuarioComun
    lateinit var usuarioBraian: UsuarioComun

    lateinit var usuarioRodri: UsuarioAdmin

    lateinit var teatroLunaPark: Teatro
    lateinit var estadioMasMonumental: Estadio
    lateinit var estadioAlmafitani: Estadio

    lateinit var show1: Show
    lateinit var show2: Show
    lateinit var show3: Show
    lateinit var show4: Show
    lateinit var show5: Show


    fun crearUsuariosYAdmin() {
        usuarioAlan = UsuarioComun(
            "alan", "gomez", LocalDate.of(2000, 12, 10),
            "alanG", "1234abcd", "/src/assets/fotoperfil2.jpg"
        ).also {
            it.saldo = 500000.0
            it.dni = 99845383
        }

        usuarioStefan = UsuarioComun(
            "Stefan", "janzcuk", LocalDate.of(1997, 3, 21),
            "stefanJ", "abcd1234", "/src/assets/fotoperfil3.jpg"
        ).also {
            it.saldo = 600000.0
            it.dni = 54566313
        }

        usuarioBraian = UsuarioComun(
            "Braian", "Berardi", LocalDate.of(1994, 8, 12),
            "braianB94", "password", "/src/assets/fotoperfil5.jpg"
        ).also {
            it.saldo = 789000.1
            it.dni = 13345332
        }

        usuarioCeci = UsuarioComun(
            "ceci", "dragonetti", LocalDate.of(1994, 6, 7),
            "ceci_d", "password", "/src/assets/fotoperfil4.jpg"
        ).also {
            it.saldo = 990110.0
            it.dni = 4356792
        }

        usuarioRodri = UsuarioAdmin(
            "rodrigo", "pavon", LocalDate.of(1997, 7, 21),
            "romeAdm", "admin123", "/src/assets/fotoperfilAdmin.jpg"
        )


        usuarioRepository.apply {
            create(usuarioAlan)
            create(usuarioStefan)
            create(usuarioCeci)
            create(usuarioBraian)
            create(usuarioRodri)
        }
    }

    fun crearInstalaciones() {
        teatroLunaPark = Teatro(
            true,
            2,
            3
        ).also {
            it.nombre = "Luna Park"
            it.ubicacion = UbicacionMapa("15.00.64", "19.00.24")
        }

        estadioMasMonumental = Estadio(
            450000.00,
            26000,
            27000,
            28000
        ).also {
            it.nombre = "Estadio Mas Monumental"
            it.ubicacion = UbicacionMapa("20.00.64", "40.00.24")
        }

        estadioAlmafitani = Estadio(
            150000.00,
            17000,
            18000,
            16000
        ).also {
            it.nombre = "Estadio Almafitani"
            it.ubicacion = UbicacionMapa("21.05.16", "43.60.05")
        }
        instalacionRepository.apply {
            create(teatroLunaPark)
            create(estadioMasMonumental)
            create(estadioAlmafitani)
        }
    }

    fun crearShow() {
        show1 = Show(
            "AC/DC",
            "Power Up Tour",
            "/src/assets/acdc.jpg",
            10000.00,
            teatroLunaPark,
            Estado.PrecioBase,
            mutableListOf()
        ).also {
            it.agregarFuncion(LocalDate.of(2024, 10, 23), LocalTime.of(21, 30, 40))
            it.agregarFuncion(LocalDate.of(2024, 10, 24), LocalTime.of(21, 30, 40))
            it.agregarFuncion(LocalDate.of(2024, 10, 26), LocalTime.of(21, 30, 40))

        }

        show2 = Show(
            "Gorillaz",
            "The Gateway",
            "/src/assets/gorillaz.jpg",
            135000.00,
            estadioMasMonumental,
            Estado.Megashow,
            mutableListOf()
        ).also {
            it.agregarFuncion(LocalDate.of(2024, 5, 23), LocalTime.of(21, 30, 40))
            it.agregarFuncion(LocalDate.of(2024, 5, 24), LocalTime.of(21, 30, 40))
            it.agregarFuncion(LocalDate.of(2024, 5, 26), LocalTime.of(21, 30, 40))
        }

        show3 = Show(
            "SIAMÉS",
            "Home Tour",
            "/src/assets/siames.jpg",
            35000.00,
            teatroLunaPark,
            Estado.VentaPlena,
            mutableListOf()
        ).also {
            it.agregarFuncion(LocalDate.of(2024, 1, 15), LocalTime.of(21, 30, 40))
        }

        show4= Show(
            "Ratones Paranoicos",
            "Ultima Cena Tour",
            "/src/assets/ratonesparanoicos.jpg",
             55000.00,
            estadioAlmafitani,
            Estado.VentaPlena,
            mutableListOf()
        ).also {
            it.agregarFuncion(LocalDate.of(2024, 9, 14), LocalTime.of(21, 30, 40))
        }

        show5 = Show(
            "El Bordo",
            "IRREAL",
            "/src/assets/elbordo.jpg",
            67000.00,
            estadioAlmafitani,
            Estado.PrecioBase,
            mutableListOf()
        ).also {
            it.agregarFuncion(LocalDate.of(2024, 8, 30), LocalTime.of(21, 30, 40))
        }

        showRepository.apply {
            create(show1)
            create(show2)
            create(show3)
            create(show4)
            create(show5)
        }
    }

    fun crearEntradas() {
        showRepository.elementos.forEach { show ->
            for (i in 1..5) {
                show.agregarEntrada(show.funciones[0], show.instalacion.ubicacionMenor())
                show.agregarEntrada(show.funciones[0], show.instalacion.ubicacionMayor())
            }
            show.funciones[0].entradasDisponibles.forEach {
                entradaRepository.create(it)
            }
        }
    }

    fun compraDeEntradas() {
        usuarioAlan.agregarEntradasACarrito(show1.funciones[0], 2, show1.instalacion.ubicacionMenor())
        usuarioAlan.agregarEntradasACarrito(show3.funciones[0], 1, show3.instalacion.ubicacionMenor())
        usuarioAlan.comprarEntradas()

        usuarioCeci.agregarEntradasACarrito(show2.funciones[0], 1, show2.instalacion.ubicacionMenor())
        usuarioCeci.agregarEntradasACarrito(show3.funciones[0], 1, show3.instalacion.ubicacionMenor())
        usuarioCeci.comprarEntradas()
        usuarioCeci.agregarEntradasACarrito(show5.funciones[0], 1, show5.instalacion.ubicacionMenor())

        usuarioStefan.agregarEntradasACarrito(show1.funciones[0], 3, show1.instalacion.ubicacionMenor())
        usuarioStefan.comprarEntradas()
        usuarioStefan.agregarEntradasACarrito(show2.funciones[0], 1, show2.instalacion.ubicacionMenor())

        usuarioBraian.agregarEntradasACarrito(show3.funciones[0], 1, show3.instalacion.ubicacionMenor())
        usuarioBraian.comprarEntradas()
        usuarioBraian.agregarEntradasACarrito(show4.funciones[0], 2, show4.instalacion.ubicacionMenor())
        usuarioBraian.agregarEntradasACarrito(show1.funciones[0], 1, show1.instalacion.ubicacionMenor())
        usuarioBraian.comprarEntradas()
    }

    fun llenadoDeAmigos(){
        usuarioAlan.agregarAmigo(usuarioCeci.id)
        usuarioAlan.agregarAmigo(usuarioBraian.id)
        usuarioCeci.agregarAmigo(usuarioAlan.id)
        usuarioCeci.agregarAmigo(usuarioBraian.id)
        usuarioCeci.agregarAmigo(usuarioStefan.id)
        usuarioBraian.agregarAmigo(usuarioAlan.id)
        usuarioBraian.agregarAmigo(usuarioCeci.id)
        usuarioStefan.agregarAmigo(usuarioCeci.id)
    }

    fun posteoDeComentarios(){
        usuarioCeci.dejarComentario(show3, "una experiecia innolvidable", 4.75f)
        usuarioAlan.dejarComentario(show3, "Volvi a sentir esa vibra de ninio", 5f)
    }

    override fun afterPropertiesSet() {
        this.crearUsuariosYAdmin()
        this.crearInstalaciones()
        this.crearShow()
        this.crearEntradas()
        this.compraDeEntradas()
        this.llenadoDeAmigos()
        this.posteoDeComentarios()
    }
}