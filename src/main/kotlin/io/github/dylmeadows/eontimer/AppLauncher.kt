package io.github.dylmeadows.eontimer

import io.github.dylmeadows.springboot.javafx.SpringFxApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(scanBasePackages = ["io.github.dylmeadows.*"])
class AppLauncher : SpringFxApplication()
