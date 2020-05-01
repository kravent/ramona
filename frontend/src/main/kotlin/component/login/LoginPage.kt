package component.login

import ajax.Api
import ajax.ApiForbiddenException
import ajax.ApiUnauthoridedException
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.css.Color
import kotlinx.css.color
import kotlinx.css.properties.boxShadow
import kotlinx.css.px
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onKeyPressFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.KeyboardEvent
import react.*
import react.dom.button
import react.dom.div
import react.dom.label
import styled.css
import styled.styledDiv
import styled.styledInput
import utils.withEvent
import utils.withTarget

data class LoginPageProps(
    val onUserLogged: (userName: String) -> Unit
) : RProps

val LoginPage = rFunction("LoginPage") { props: LoginPageProps ->
    var loading by useState(false)
    var user by useState("")
    var password by useState("")
    var errorMessage by useState(null as String?)

    fun doLogin() {
        loading = true
        MainScope().launch {
            try {
                Api.login(user, password)
                props.onUserLogged(user)
                user = ""
                password = ""
                errorMessage = null
            } catch (e: ApiUnauthoridedException) {
                errorMessage = "User not found"
            } catch (e: ApiForbiddenException) {
                errorMessage = "CSRF token check error"
            } catch (e: Exception) {
                errorMessage = "Ooops! Something went wrong"
            } finally {
                loading = false
            }
        }
    }

    div {
        errorMessage?.let {
            styledDiv {
                css {
                    color = Color.red
                }
                +it
            }
        }

        div {
            div {
                label { +"User" }
                styledInput {
                    if (errorMessage != null) {
                        css {
                            boxShadow(color = Color.red, blurRadius = 3.px)
                        }
                    }
                    attrs {
                        value = user
                        disabled = loading
                        onChangeFunction = withTarget<HTMLInputElement> { user = it.value }
                        onKeyPressFunction = withEvent<KeyboardEvent>{ if (it.key == "Enter") doLogin() }
                    }
                }
            }
            div {
                label { +"Password" }
                styledInput {
                    if (errorMessage != null) {
                        css {
                            boxShadow(color = Color.red, blurRadius = 3.px)
                        }
                    }
                    attrs {
                        type = InputType.password
                        value = password
                        disabled = loading
                        onChangeFunction = withTarget<HTMLInputElement> { password = it.value }
                        onKeyPressFunction = withEvent<KeyboardEvent>{ if (it.key == "Enter") doLogin() }
                    }
                }
            }
            div {
                button {
                    attrs.onClickFunction = { doLogin() }
                    +"Login"
                }
            }
        }
    }
}

fun RBuilder.loginPage(onUserLogged: (userName: String) -> Unit) = child(LoginPage, LoginPageProps(onUserLogged)) {}
