package io.github.dylmeadows.eontimer.model.resource

import io.github.dylmeadows.commonkt.core.io.Resource

enum class ImageResource(
    relativePath: String
) : Resource {
    DefaultBackgroundImage("default_background_image.png");

    override val path: String = "$BASE_RESOURCE_PATH/img/$relativePath"
}