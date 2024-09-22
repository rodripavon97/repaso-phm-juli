package ar.edu.unsam.phm.tpphmgrupo4.Controller

import ar.edu.unsam.phm.tpphmgrupo4.DTO.FuncionDTO
import ar.edu.unsam.phm.tpphmgrupo4.DTO.ShowAdminDTO
import ar.edu.unsam.phm.tpphmgrupo4.DTO.ShowDTO
import ar.edu.unsam.phm.tpphmgrupo4.DTO.ShowDetalleDTO
import ar.edu.unsam.phm.tpphmgrupo4.Funcion
import ar.edu.unsam.phm.tpphmgrupo4.Show
import ar.edu.unsam.phm.tpphmgrupo4.UsuarioComun
import ar.edu.unsam.phm.tpphmgrupo4.service.showService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
@CrossOrigin("*")

class ShowController {
    @Autowired
    lateinit var showService: showService

    @GetMapping("/shows")
    @Operation(summary = "devuelvo todos los shows incluido con los filtros")
    fun getShowsAll(
        @RequestParam(required = false, defaultValue = "") artista: String?,
        @RequestParam(required = false, defaultValue = "") locacion: String?,
        @RequestParam(required = false, defaultValue = "") id: Int?,
        @RequestParam(required = false) conAmigos: Boolean?
    ): List<ShowDTO> {
        return showService.getAllShowWithFilters(id, artista, locacion, conAmigos).map { show: Show ->

            ShowDTO.fromShow(
                show,
                showService.filterAmigos(id, show)
            )
        }
    }

    @GetMapping("/admin/shows")
    @Operation(summary = "devuelvo todos los shows incluido con los filtros")
    fun getAdminShows(@RequestParam(required = false, defaultValue = "") artista: String?, @RequestParam(required = false, defaultValue = "") locacion: String?,  @RequestParam(required = false, defaultValue = "") id: Int?): List<ShowAdminDTO> {
        return showService.getAllShowAdmin(id, artista,locacion).map{
            show : Show -> ShowAdminDTO.fromshowAdminDTO(show)
        }
    }

    @GetMapping("/show-detalle/{id}")
    @Operation(summary = "devuelvo el detalle del show elegido")
    fun ShowPorID(@PathVariable id: Int): ShowDetalleDTO {
        return ShowDetalleDTO.fromShowDetalle(showService.navegarShowDetallePorId(id))
    }

    @PostMapping("/show/{idShow}/fila-espera/{idUsuario}")
    @Operation(summary = "Agregar a una persona en fila de espera")
    fun agregrarPersonaEnEspera(@PathVariable idShow: Int, @PathVariable idUsuario: Int): UsuarioComun {
        return showService.agragarUsuarioAEspera(idShow, idUsuario)
    }

    @PostMapping("/show/{id}/nueva-funcion")
    @Operation(summary = "Agregar nueva funcion al show")

    fun agregrarFuncion(@PathVariable id: Int, @RequestBody dto: FuncionDTO): Funcion {
        return showService.crearFuncion(id, dto)

    }

    @DeleteMapping("/show/{id}")
    @Operation(summary = "Eliminar un show por ID")
    fun eliminarShow(@PathVariable id: Int) {
        showService.eliminarShow(id)
    }

    @PatchMapping("/show/{id}")
    @Operation(summary = "Actualizar un show por ID")
    fun editarDatos(@PathVariable id: Int, @RequestBody showDTO: ShowDTO) {
        showService.editarDatos(id, showDTO.nombreBanda, showDTO.nombreRecital)
    }


}