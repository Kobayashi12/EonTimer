//
// Created by Dylan Meadows on 2020-03-15.
//

#include "SettingsDialog.h"

#include <QPushButton>
#include <QTabWidget>
#include <QVBoxLayout>

namespace EonTimer {
    SettingsDialog::SettingsDialog(Timer::TimerSettingsModel *timerSettings,
                                   Action::ActionSettingsModel *actionSettings,
                                   QWidget *parent)
        : QDialog(parent), timerSettings(timerSettings), actionSettings(actionSettings) {
        timerSettingsPane = new Timer::TimerSettingsPane(timerSettings);
        actionSettingsPane = new Action::ActionSettingsPane(actionSettings);
        initComponents();
    }

    void SettingsDialog::initComponents() {
        setWindowTitle("Preferences");
        setWindowFlags(Qt::Dialog | Qt::WindowTitleHint | Qt::CustomizeWindowHint | Qt::WindowCloseButtonHint);
        auto *layout = new QGridLayout(this);
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
            connect(okButton, &QPushButton::clicked, [this] {
                actionSettingsPane->updateSettings();
                timerSettingsPane->updateSettings();
                done(QDialog::Accepted);
            });
            layout->addWidget(okButton, 1, 1);
            okButton->setDefault(true);
        }
    }
}  // namespace EonTimer
