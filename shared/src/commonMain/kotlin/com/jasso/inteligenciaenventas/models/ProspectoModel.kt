package com.jasso.inteligenciaenventas.models

import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName

@Serializable
data class ProspectoModel(
    @SerialName("fecha_registro") val fecha_registro: Long?=null,
    @SerialName("uid") val uid: String? = null,
    @SerialName("email") val email: String? = null,
//
    @SerialName("img_profile") val img_profile: String? = null,
    @SerialName("edad") val edad: Int? = null,
    @SerialName("fecha_nacimiento") val fecha_nacimiento: Long? = null,
    @SerialName("genero") val genero: String? = null,
    @SerialName("nombre") val nombre: String? = null,
    @SerialName("apellido_paterno") val apellido_paterno: String? = null,
    @SerialName("apellido_materno") val apellido_materno: String? = null,
//
    @SerialName("img_profile_pareja") val img_profile_pareja: String?= null,
    @SerialName("edad_pareja") val edad_pareja: Int?= null,
    @SerialName("fecha_nacimiento_pareja") val fecha_nacimiento_pareja: Long?= null,
    @SerialName("genero_pareja") val genero_pareja: String?= null,
    @SerialName("nombre_pareja") val nombre_pareja: String?= null,
    @SerialName("apellido_paterno_pareja") val apellido_paterno_pareja: String?= null,
    @SerialName("apellido_materno_pareja") val apellido_materno_pareja: String?= null,

    @SerialName("rol") val rol: String?= null,
    @SerialName("status") val status: String?= null,
    @SerialName("calle_uno") val calle_uno: String? = null,
    @SerialName("calle_dos") val calle_dos: String? = null,
    @SerialName("numero_interior") val numero_interior: Int? = null,
    @SerialName("numero_exterior") val numero_exterior: Int? = null,
    @SerialName("codigo_postal") val codigo_postal: Int? = null,
    @SerialName("entre_calle_uno") val entre_calle_uno: String? = null,
    @SerialName("entre_calle_dos") val entre_calle_dos: String? = null,
    @SerialName("telefonos") val telefonos: List<Telefono>?,
//
//    //info prospectos
    @SerialName("asesor_creador") val asesor_creador: String? = null,
    @SerialName("asesor_vendedor") val asesor_vendedor: String?= null,
    @SerialName("folio") val folio: String?= null,
    @SerialName("invitados") val invitados: Int? = null,
//
    @SerialName("descripcion") val descripcion: String? = null,
    @SerialName("pregunto_por") val pregunto_por: String? = null,
    @SerialName("fecha_evento") val fecha_evento: Long?,
    @SerialName("tipo_evento") val tipo_evento: String?,

) {
    constructor() : this(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
    )
}


@Serializable
data class Llamadas(
    @SerialName("asesor_llamada") val asesor_llamada: String?,
    @SerialName("fecha_llamada") val fecha_llamada: String?,
    @SerialName("duracion_llamada") val numero: String?
)