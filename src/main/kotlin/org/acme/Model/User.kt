package org.acme.Model

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue
    val id: Long? = 0,
    val username: String? = null,
    val email: String? = null,
    val password: String? = null
)