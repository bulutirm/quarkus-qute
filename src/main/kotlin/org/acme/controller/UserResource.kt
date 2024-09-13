package org.acme.controller

import io.quarkus.qute.CheckedTemplate
import io.quarkus.qute.TemplateInstance
import jakarta.inject.Inject
import jakarta.servlet.http.HttpServletRequest
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Request
import jakarta.ws.rs.core.Response
import org.acme.Model.User
import org.acme.Service.UserService

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class UserResource {

    @Inject
    lateinit var userService: UserService

    @CheckedTemplate(requireTypeSafeExpressions = false)
    object Templates {
        @JvmStatic
        external fun register(): TemplateInstance
        @JvmStatic
        external fun login(): TemplateInstance
        @JvmStatic
        external fun profile(): TemplateInstance
    }

    @GET
    @Path("/profile")
    @Produces(MediaType.TEXT_HTML)
    fun getProfilePage(@QueryParam("username") username: String): TemplateInstance {
        val user = userService.getUserByUsername(username)
        val allUsers = userService.listAllUsers() // Tüm kullanıcıları getir
        return if (user != null) {
            Templates.profile()
                .data("username", user.username)
                .data("email", user.email)
                .data("users", allUsers) // Tüm kullanıcı listesini şablona ekle
        } else {
            Templates.profile()
                .data("username", "Unknown")
                .data("email", "N/A")
                .data("users", allUsers) // Kullanıcı bulunmasa bile tüm kullanıcıları ekle
        }
    }


    @GET
    @Path("/register")
    @Produces(MediaType.TEXT_HTML)
    fun getRegisterPage(): TemplateInstance {
        return Templates.register()
    }

    @GET
    @Path("/login")
    @Produces(MediaType.TEXT_HTML)
    fun getLoginPage(): TemplateInstance {
        return Templates.login()
    }


    @GET
    fun listAllUsers(): Response {
        val users: List<User> = userService.listAllUsers()
        return Response.ok(users).build()
    }


    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    fun login(
        @FormParam("username") username: String,
        @FormParam("password") password: String,
    ): Response {
        return if (userService.login(username, password)) {
            return Response.seeOther(java.net.URI("/users/profile?username=$username")).build()
        } else {
            Response.status(Response.Status.UNAUTHORIZED).entity("Invalid username or password").build()
        }
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    fun register(
        @FormParam("username") username: String,
        @FormParam("email") email: String,
        @FormParam("password") password: String
    ): Response {
        val user = User(username = username, email = email, password = password)
        userService.registerUser(user)
        return Response.seeOther(java.net.URI("/users/login")).build()
    }


}