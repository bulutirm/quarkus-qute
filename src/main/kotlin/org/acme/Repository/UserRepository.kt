package org.acme.Repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import org.acme.Model.User

@ApplicationScoped
class UserRepository : PanacheRepository<User> {

    fun findByUsername(username: String): User? {
        return find("username", username).firstResult()
    }
}