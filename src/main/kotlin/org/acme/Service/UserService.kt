package org.acme.Service

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.acme.Model.User
import org.acme.Repository.UserRepository

@ApplicationScoped
@Transactional
class UserService {

    @Inject
    lateinit var userRepository: UserRepository

    fun listAllUsers(): List<User> {
        return userRepository.listAll()
    }


    fun getUserByUsername(username: String): User? {
        val user: User? = userRepository.findByUsername(username)
        return user;
    }

    fun login(username: String, password: String): Boolean {
        val user: User? = userRepository.findByUsername(username)
        return if (user != null) {
            user.password == password
        } else {
            false
        }
    }

    fun registerUser(user: User): User {
        userRepository.persist(user)
        return user
    }
}