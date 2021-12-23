package io.eontimer

import io.eontimer.config.AppProperties
import io.eontimer.config.DebugProperties
import io.eontimer.model.resource.CssResource
import io.eontimer.model.resource.FxmlResource
import io.eontimer.util.javafx.addCss
import javafx.application.Application
import javafx.application.Application.launch
import javafx.scene.Scene
import javafx.stage.Stage
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.getBean
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ConfigurableApplicationContext

@SpringBootApplication
class EonTimerLauncher : Application() {
    private lateinit var ctx: ConfigurableApplicationContext
    private val log = LoggerFactory.getLogger(EonTimerLauncher::class.java)

    override fun init() {
        val args = parameters.raw.toTypedArray()
        ctx = SpringApplicationBuilder(javaClass)
            .web(WebApplicationType.NONE)
            .headless(false)
            .run(*args)

        val debugProperties = ctx.getBean<DebugProperties>()
        val longestTag = debugProperties.tags.maxOf { it.length }
        debugProperties.tags.forEach {
            log.info("{} == {}", it.padEnd(longestTag), System.getProperty(it))
        }
    }

    override fun start(stage: Stage) {
        val fxmlLoader = ctx.getBean<SpringFxmlLoader>()
        stage.title = ctx.getBean<AppProperties>().fullApplicationName
        stage.scene = Scene(fxmlLoader.load(FxmlResource.EonTimerPane))
        stage.scene.addCss(CssResource.MAIN)
        stage.show()
    }

    override fun stop() {
        ctx.close()
    }
}

fun main(args: Array<String>) {
    launch(EonTimerLauncher::class.java, *args)
}
