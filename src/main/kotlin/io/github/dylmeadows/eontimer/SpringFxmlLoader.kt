package io.github.dylmeadows.eontimer

import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.util.Callback
import org.springframework.beans.factory.BeanFactory
import org.springframework.stereotype.Component
import java.io.InputStream
import java.net.URL
import java.util.*

@Component
class SpringFxmlLoader(
    private val beans: BeanFactory
) {
    fun <T : Parent> load(
        location: URL
    ): T {
        val loader = FXMLLoader()
        loader.location = location
        loader.controllerFactory = Callback { beans.getBean(it) }
        return loader.load()
    }

    fun <T : Parent> load(
        location: URL,
        resources: ResourceBundle
    ): T {
        val loader = FXMLLoader()
        loader.location = location
        loader.resources = resources
        loader.controllerFactory = Callback { beans.getBean(it) }
        return loader.load()
    }

    fun <T : Parent> load(
        inputStream: InputStream
    ): T {
        val loader = FXMLLoader()
        loader.controllerFactory = Callback { beans.getBean(it) }
        return loader.load(inputStream)
    }

    fun <T : Parent> load(
        inputStream: InputStream,
        resources: ResourceBundle
    ): T {
        val loader = FXMLLoader()
        loader.resources = resources
        loader.controllerFactory = Callback { beans.getBean(it) }
        return loader.load(inputStream)
    }
}