//
// Created by Dylan Meadows on 2020-03-09.
//

#include "ApplicationWindow.h"

#include <SettingsDialog.h>
#include <Util/Functions.h>
#include <app.h>

#include <QFile>
#include <QMenuBar>

namespace EonTimer {
    static const QString &getTitle() {
        static const QString title = QString("%1 %2").arg(APP_NAME, APP_VERSION);
        return title;
    }

    void addStylesheet(QString &css, const char *resource) {
        QFile file(resource);
        file.open(QFile::ReadOnly);
        css.append(file.readAll());
    }

    ApplicationWindow::ApplicationWindow(QWidget *parent) : QMainWindow(parent) { initComponents(); }

    void ApplicationWindow::initComponents() {
        auto *settings = new QSettings(this);
        auto *gen3Timer = new Gen3::TimerModel(settings);
        auto *gen4Timer = new Gen4::TimerModel(settings);
        auto *gen5Timer = new Gen5::TimerModel(settings);
        auto *timerSettings = new Timer::Settings(settings);
        auto *actionSettings = new Action::Settings(settings);
        auto *timerService = new Timer::TimerService(timerSettings, actionSettings, this);
        auto *applicationPane = new ApplicationPane(settings,
                                                    actionSettings,
                                                    timerSettings,
                                                    gen5Timer,
                                                    gen4Timer,
                                                    gen3Timer,
                                                    timerService,
                                                    this);

        setWindowTitle(getTitle());
        setWindowFlags(Qt::Window | Qt::WindowTitleHint | Qt::CustomizeWindowHint | Qt::WindowCloseButtonHint |
                       Qt::WindowMaximizeButtonHint | Qt::WindowMinimizeButtonHint);
        setCentralWidget(applicationPane);
        //        setFixedSize(525, 395);

        QString stylesheet;
        addStylesheet(stylesheet, ":/styles/main.css");
        setStyleSheet(stylesheet);

        // background image
        QPalette palette;
        QPixmap background(":/images/default_background_image.png");
        background = background.scaled(this->size(), Qt::KeepAspectRatioByExpanding);
        palette.setBrush(QPalette::Window, background);
        setPalette(palette);

        // ----- menu -----
        {
            auto *menu = new QMenu();
            auto *menuBar = new QMenuBar();
            menuBar->addMenu(menu);
            // ----- preferences -----
            {
                auto *preferences = new QAction();
                preferences->setMenuRole(QAction::PreferencesRole);
                connect(preferences, &QAction::triggered, [timerSettings, actionSettings, this] {
                    SettingsDialog(timerSettings, actionSettings, this).exec();
                });
                connect(timerService, &Timer::TimerService::activated, [preferences](const bool activated) {
                    preferences->setEnabled(!activated);
                });
                menu->addAction(preferences);
            }
        }

        connect(this,
                &ApplicationWindow::onClose,
                [gen3Timer, gen4Timer, gen5Timer, settings] {
                    gen3Timer->sync(settings);
                    gen4Timer->sync(settings);
                    gen5Timer->sync(settings);
                    settings->sync();
                });
    }

    void ApplicationWindow::closeEvent(QCloseEvent *) { emit onClose(); }

}  // namespace EonTimer
