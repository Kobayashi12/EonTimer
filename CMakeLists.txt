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
find_package(SFML 2.4 COMPONENTS audio REQUIRED)

set(SOURCES
        resources/resources.qrc
        src/ApplicationPane.cpp
        src/ApplicationPane.h
        src/ApplicationWindow.cpp
        src/ApplicationWindow.h
        src/SettingsDialog.cpp
        src/SettingsDialog.h
        src/Action/ActionSettingsPane.cpp
        src/Action/ActionSettingsPane.h
        src/Timer/TimerSettingsPane.cpp
        src/Timer/TimerSettingsPane.h
        src/Timer/TimerDisplayPane.cpp
        src/Timer/TimerDisplayPane.h
        src/Gen4/Gen4TimerPane.cpp
        src/Gen4/Gen4TimerPane.h
        src/Util/FontHelper.cpp
        src/Util/FontHelper.h
        src/Util/WidgetHelper.h
        src/Action/ActionMode.cpp
        src/Action/ActionMode.h
        src/Timer/Console.cpp
        src/Timer/Console.h
        src/Action/Sound.cpp
        src/Action/Sound.h
        src/Timer/TimerState.cpp
        src/Timer/TimerState.h
        src/services/CalibrationService.cpp
        src/services/CalibrationService.h
        src/Action/ActionSettingsModel.cpp
        src/Action/ActionSettingsModel.h
        src/Gen4/Gen4TimerModel.cpp
        src/Gen4/Gen4TimerModel.h
        src/Timer/TimerSettingsModel.cpp
        src/Timer/TimerSettingsModel.h
        src/Action/SoundService.cpp
        src/Action/SoundService.h
        src/services/timers/DelayTimer.cpp
        src/services/timers/DelayTimer.h
        src/services/timers/SecondTimer.cpp
        src/services/timers/SecondTimer.h
        src/Timer/TimerService.cpp
        src/Timer/TimerService.h
        src/Util/instrumentation/Timer.cpp
        src/Util/instrumentation/Timer.h
        src/Util/Functions.cpp
        src/Util/Functions.h
        src/Timer/Clock.cpp
        src/Timer/Clock.h
        src/Gen5/Gen5TimerPane.cpp
        src/Gen5/Gen5TimerPane.h
        src/Gen5/Gen5TimerModel.cpp
        src/Gen5/Gen5TimerModel.h
        src/Gen5/Gen5TimerMode.cpp
        src/Gen5/Gen5TimerMode.h
        src/services/timers/EntralinkTimer.cpp
        src/services/timers/EntralinkTimer.h
        src/services/timers/FrameTimer.cpp
        src/services/timers/FrameTimer.h
        src/services/timers/EnhancedEntralinkTimer.cpp
        src/services/timers/EnhancedEntralinkTimer.h
        src/Util/FieldSet.h
        src/Gen3/Gen3TimerModel.cpp
        src/Gen3/Gen3TimerModel.h
        src/Gen3/Gen3TimerPane.cpp
        src/Gen3/Gen3TimerPane.h
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