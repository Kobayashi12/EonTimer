if "%OS%"=="win32" (set QT_PATH=C:\Qt\5.15.2\msvc2019)
if "%OS%"=="win64" (set QT_PATH=C:\Qt\5.15.2\msvc2019_64)
set PATH=%QT_PATH%\bin;C:\Qt\Tools\QtCreator\bin;%PATH%
echo %QT_PATH%

md EonTimer
move EonTimer.exe EonTimer\EonTimer.exe
windeployqt --release --no-translations --no-angle --no-plugins --no-opengl-sw EonTimer\EonTimer.exe
xcopy /I %QT_PATH%\plugins\platforms\qwindows.dll EonTimer\platforms\
xcopy /I %QT_PATH%\plugins\styles\qwindowsvistastyle.dll EonTimer\styles\
xcopy /I SFML-2.5.1\bin\openal32.dll EonTimer\
xcopy /I SFML-2.5.1\bin\sfml-audio-2.dll EonTimer\
xcopy /I SFML-2.5.1\bin\sfml-system-2.dll EonTimer\
7z a EonTimer-%OS%.zip EonTimer\
sha256sum EonTimer-%OS%.zip > EonTimer-%OS%.zip.sha256
