//
// Created by Dylan Meadows on 2020-03-15.
//

#include "SettingsDialog.h"

#include <QPushButton>
#include <QTabWidget>
#include <QVBoxLayout>

namespace EonTimer {
    SettingsDialog::SettingsDialog(Timer::Settings *timerSettings, Action::Settings *actionSettings, QWidget *parent)
        : QDialog(parent) {
        initComponents(timerSettings, actionSettings);
    }

    void SettingsDialog::initComponents(Timer::Settings *timerSettings, Action::Settings *actionSettings) {
        setWindowTitle("Preferences");
        setWindowFlags(Qt::Dialog | Qt::WindowTitleHint | Qt::CustomizeWindowHint | Qt::WindowCloseButtonHint);
        auto *layout = new QGridLayout(this);
        auto *timerSettingsPane = new Timer::SettingsPane(timerSettings);
        auto *actionSettingsPane = new Action::SettingsPane(actionSettings);
        layout->setVerticalSpacing(10);
        // ----- tabPane -----
        {
            auto *tabPane = new QTabWidget();
            tabPane->addTab(actionSettingsPane, "Action");
            tabPane->addTab(timerSettingsPane, "Timer");
            layout->addWidget(tabPane, 0, 0, 1, 2);
        }
        // ----- cancelButton -----
        {
            auto *cancelButton = new QPushButton("Cancel");
            connect(cancelButton, &QPushButton::clicked, [this] { done(QDialog::Rejected); });
            layout->addWidget(cancelButton, 1, 0);
        }
        // ----- okButton -----
        {
            auto *okButton = new QPushButton("OK");
            connect(okButton, &QPushButton::clicked, [this, timerSettingsPane, actionSettingsPane] {
                timerSettingsPane->updateSettings();
                actionSettingsPane->updateSettings();
                done(QDialog::Accepted);
            });
            layout->addWidget(okButton, 1, 1);
            okButton->setDefault(true);
        }
    }
}  // namespace EonTimer
