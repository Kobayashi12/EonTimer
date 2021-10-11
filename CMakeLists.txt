cmake_minimum_required(VERSION 3.13)

project(EonTimer VERSION 3.0.0)
set(CMAKE_CXX_STANDARD 20)
set(CMAKE_AUTOMOC ON)
set(CMAKE_AUTORCC ON)

set(BASEPATH "${CMAKE_SOURCE_DIR}/src")
include_directories("${BASEPATH}")

configure_file(
        ${CMAKE_SOURCE_DIR}/src/app.h.in
        ${CMAKE_BINARY_DIR}/generated/app.h
)
include_directories(${CMAKE_BINARY_DIR}/generated)

if (UNIX AND NOT APPLE)
    message("-- Linux build detected")
    set(Qt5Widgets_DIR "/opt/qt514/lib/cmake/Qt5Widgets")
    list(APPEND CMAKE_MODULE_PATH "${CMAKE_MODULE_PATH};/usr/share/SFML/cmake/Modules")
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -pthread")
endif ()

if (WIN32)
    message("-- Windows build detected")
    set(SFML_DIR "./SFML-2.5.1/lib/cmake/SFML")
endif ()

set(CMAKE_PREFIX_PATH "/usr/local/opt/qt/lib/cmake")
find_package(Qt5Widgets REQUIRED)
find_package(SFML 2.5 COMPONENTS audio REQUIRED)

set(SOURCES
        resources/resources.qrc
        src/ApplicationPane.cpp
        src/ApplicationPane.h
        src/ApplicationWindow.cpp
        src/ApplicationWindow.h
        src/SettingsDialog.cpp
        src/SettingsDialog.h
        src/Action/SettingsPane.cpp
        src/Action/SettingsPane.h
        src/Timer/SettingsPane.cpp
        src/Timer/SettingsPane.h
        src/Timer/TimerDisplayPane.cpp
        src/Timer/TimerDisplayPane.h
        src/Gen4/TimerPane.cpp
        src/Gen4/TimerPane.h
        src/Util/WidgetHelper.h
        src/Action/Mode.cpp
        src/Action/Mode.h
        src/Timer/Console.cpp
        src/Timer/Console.h
        src/Action/Sound.cpp
        src/Action/Sound.h
        src/Timer/TimerState.cpp
        src/Timer/TimerState.h
        src/Util/CalibrationService.cpp
        src/Util/CalibrationService.h
        src/Action/Settings.cpp
        src/Action/Settings.h
        src/Gen4/TimerModel.cpp
        src/Gen4/TimerModel.h
        src/Timer/Settings.cpp
        src/Timer/Settings.h
        src/Action/SoundService.cpp
        src/Action/SoundService.h
        src/Timer/Factory/DelayTimer.cpp
        src/Timer/Factory/DelayTimer.h
        src/Timer/Factory/SecondTimer.cpp
        src/Timer/Factory/SecondTimer.h
        src/Timer/TimerService.cpp
        src/Timer/TimerService.h
        src/Util/Functions.cpp
        src/Util/Functions.h
        src/Timer/Clock.cpp
        src/Timer/Clock.h
        src/Gen5/TimerPane.cpp
        src/Gen5/TimerPane.h
        src/Gen5/TimerModel.cpp
        src/Gen5/TimerModel.h
        src/Gen5/TimerMode.cpp
        src/Gen5/TimerMode.h
        src/Timer/Factory/EntralinkTimer.cpp
        src/Timer/Factory/EntralinkTimer.h
        src/Timer/Factory/FrameTimer.cpp
        src/Timer/Factory/FrameTimer.h
        src/Timer/Factory/EnhancedEntralinkTimer.cpp
        src/Timer/Factory/EnhancedEntralinkTimer.h
        src/Util/FieldSet.h
        src/Gen3/TimerModel.cpp
        src/Gen3/TimerModel.h
        src/Gen3/TimerPane.cpp
        src/Gen3/TimerPane.h
        src/Util/QSettingsProperty.cpp
        src/Util/QSettingsProperty.h
        src/app.h.in
        src/main.cpp
        )

if (APPLE)
    add_executable(${PROJECT_NAME} MACOSX_BUNDLE ${SOURCES})
    set_target_properties(${PROJECT_NAME} PROPERTIES MACOSX_BUNDLE_INFO_PLIST ${CMAKE_SOURCE_DIR}/resources/macos/Info.plist)
else ()
    add_executable(${CMAKE_PROJECT_NAME} ${SOURCES})
endif ()

target_link_libraries(EonTimer Qt5::Widgets sfml-audio)