package component.login

import ajax.Api
import ajax.ApiForbiddenException
import ajax.ApiUnauthoridedException
import component.bootstrap.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.css.marginTop
import kotlinx.css.px
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.img
import styled.css
import utils.withTarget

data class LoginPageProps(
    val onUserLogged: (userName: String) -> Unit
) : RProps

val LoginPage = rFunction("LoginPage") { props: LoginPageProps ->
    var loading by useState(false)
    var user by useState("")
    var password by useState("")
    var showError by useState(false)
    var errorMessage by useState("")

    fun doLogin() {
        loading = true
        showError = false
        MainScope().launch {
            try {
                Api.login(user, password)
                props.onUserLogged(user)
                user = ""
                password = ""
            } catch (e: ApiUnauthoridedException) {
                showError = true
                errorMessage = "User not found"
            } catch (e: ApiForbiddenException) {
                showError = true
                errorMessage = "CSRF token check error"
            } catch (e: Exception) {
                showError = true
                errorMessage = "Ooops! Something went wrong"
            } finally {
                loading = false
            }
        }
    }

    StyledContainer {
        css { marginTop = 100.px }
        Row {
            attrs.className = "justify-content-md-center"
            Col {
                attrs.md = 6

                img {
                    attrs {
                        src = "/static/images/ramona.jpg"
                        width = "100%"
                    }
                }

                StyledForm {
                    css { marginTop = 40.px }
                    attrs {
                        onSubmit = { event ->
                            event.preventDefault()
                            doLogin()
                        }
                    }

                    Alert {
                        attrs {
                            dismissible = true
                            show = showError
                            variant = AlertVariant.danger
                            onClose = { showError = false }
                        }
                        +errorMessage
                    }

                    FormGroup {
                        attrs.controlId = "loginUserField"
                        FormLabel { +"User" }
                        FormControl {
                            attrs {
                                type = FormControlType.text
                                value(user)
                                disabled = loading
                                isInvalid = showError
                                onChange = withTarget<HTMLInputElement> { user = it.value }
                            }
                        }
                    }

                    FormGroup {
                        attrs.controlId = "loginPasswordField"
                        FormLabel { +"Password" }
                        FormControl {
                            attrs {
                                type = FormControlType.password
                                value(password)
                                disabled = loading
                                isInvalid = showError
                                onChange = withTarget<HTMLInputElement> { password = it.value }
                            }
                        }
                    }

                    Button {
                        attrs {
                            className = "float-right"
                            type = ButtonType.submit
                            variant = ButtonVariant.primary
                            disabled = loading
                        }
                        +"Login"
                    }
                }
            }
        }
    }
}

fun RBuilder.loginPage(onUserLogged: (userName: String) -> Unit) = child(LoginPage, LoginPageProps(onUserLogged)) {}
