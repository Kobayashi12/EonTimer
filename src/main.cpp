#include "ApplicationWindow.h"
#include "app.h"
#include <qapplication.h>

int main(int argc, char **argv) {
    QApplication app(argc, argv);
    QCoreApplication::setApplicationName(APP_NAME);
    QCoreApplication::setOrganizationName("DylanMeadows");
    QCoreApplication::setOrganizationDomain("io.github.dylmeadows");
    qRegisterMetaTypeStreamOperators<QList<int>>("QList<int>");

    EonTimer::ApplicationWindow window;
    window.show();

    return QApplication::exec();
}