#include <ApplicationWindow.h>
#include <qapplication.h>

#include <iostream>

#include "app.h"

int main(int argc, char **argv) {
    std::chrono::milliseconds ms(100);
    std::chrono::microseconds us(1);
    std::cout << (ms + us).count() << std::endl;

//    QApplication app(argc, argv);
//    QCoreApplication::setApplicationName(APP_NAME);
//    QCoreApplication::setOrganizationName("DylanMeadows");
//    QCoreApplication::setOrganizationDomain("io.github.dylmeadows");
//    qRegisterMetaTypeStreamOperators<QList<int>>("QList<int>");
//
//    EonTimer::ApplicationWindow window;
//    window.show();
//
//    return QApplication::exec();
    return 0;
}