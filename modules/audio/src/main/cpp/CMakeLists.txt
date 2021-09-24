cmake_minimum_required(VERSION 3.18)
project(audio)

set(CMAKE_CXX_STANDARD 20)

file(GLOB SOURCE
    ./*.h
    ./*.cpp
)

add_library(${PROJECT_NAME} SHARED ${SOURCE})

find_package(JNI REQUIRED)
if (JNI_FOUND)
    include_directories(${JNI_INCLUDE_DIRS})
    target_link_libraries(${PROJECT_NAME} ${JNI_LIBRARIES})
else ()
    message(SEND_ERROR "JNI not found.")
endif ()

find_package(SFML 2.5.1 REQUIRED audio)
target_link_libraries(${PROJECT_NAME} sfml-audio)