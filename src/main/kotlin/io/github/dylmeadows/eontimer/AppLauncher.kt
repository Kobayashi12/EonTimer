package io.github.dylmeadows.eontimer

import io.github.dylmeadows.eontimer.config.AppProperties
import io.github.dylmeadows.eontimer.model.resource.CssResource
import io.github.dylmeadows.eontimer.model.resource.FxmlResource
import io.github.dylmeadows.eontimer.util.*
import javafx.application.Application
import javafx.application.Application.launch
import javafx.scene.Parent
import javafx.stage.Stage
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.getBean
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(value = ["io.github.dylmeadows.*"])
class AppLauncher : Application() {
    private lateinit var ctx: ConfigurableApplicationContext
    private val log = LoggerFactory.getLogger(AppLauncher::class.java)

    override fun init() {
        val args = parameters.raw.toTypedArray()
        ctx = SpringApplicationBuilder(javaClass)
            .web(WebApplicationType.NONE)
            .headless(false)
            .run(*args)

        arrayOf("os.name", "os.version", "os.arch", "java.version", "java.vendor", "sun.arch.data.model")
            .forEach { log.info("{} == {}", it, System.getProperty(it)) }
    }

    override fun start(stage: Stage) {
        stage.title = ctx.getBean<AppProperties>().fullApplicationName
        stage.scene = load<Parent>(FxmlResource.EonTimerPane).asScene()
        stage.scene.addCss(CssResource.MAIN)
        stage.isResizable = false
        stage.show()
    }

    override fun stop() {
        ctx.close()
    }
}

fun main(args: Array<String>) {
    launch(AppLauncher::class.java, *args)
}
