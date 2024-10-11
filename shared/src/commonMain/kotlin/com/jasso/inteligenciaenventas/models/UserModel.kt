package com.jasso.inteligenciaenventas.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class UserModel(
    @SerialName("fecha_registro") val fecha_registro: Long? = null,
    @SerialName("uid") val uid: String? = null,
    @SerialName("email") val email: String? = null,

    @SerialName("img_profile") val img_profile: String? = null,
    @SerialName("edad") val edad: String? = null,
    @SerialName("fecha_nacimiento") val fecha_nacimiento: Long? = null,
    @SerialName("genero") val genero: String? = null,
    @SerialName("nombre") val nombre: String? = null,
    @SerialName("apellido_paterno") val apellido_uno: String? = null,
    @SerialName("apellido_materno") val apellido_dos: String? = null,

    @SerialName("rol") val rol: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("direccion") val direccion: Direccion? = null,
    @SerialName("telefonos") val telefonos: List<Telefono>? = null,
) {
    // Constructor sin argumentos necesario para Firebase
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
        null
    )
}

@Serializable
data class Telefono(
    @SerialName("lada") val lada: String? = null,
    @SerialName("numero") val numero: String? = null
) {
    // Constructor sin argumentos necesario para Firebase
    constructor() : this(
        null,
        null
    )
}

@Serializable
data class Direccion(
    @SerialName("calle_uno") val calle_uno: String? = null,
    @SerialName("calle_dos") val calle_dos: String? = null,
    @SerialName("numero_interior") val numero_interior: String? = null,
    @SerialName("numero_exterior") val numero_exterior: String? = null,
    @SerialName("codigo_postal") val codigo_postal: String? = null,
    @SerialName("entre_calle_uno") val entre_calle_uno: String? = null,
    @SerialName("entre_calle_dos") val entre_calle_dos: String? = null
) {
    // Constructor sin argumentos necesario para Firebase
    constructor() : this(
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )
}
