package io.github.dylmeadows.eontimer.model.resource

import io.github.dylmeadows.commonkt.core.io.Resource

enum class CssResource(
    relativePath: String
) : Resource {
    MAIN("main.css");

    override val path: String = "$BASE_RESOURCE_PATH/css/$relativePath"
}