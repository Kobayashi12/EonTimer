//
// Created by Dylan Meadows on 2020-03-16.
//

#include "ActionSettingsPane.h"

#include <Util/FieldSet.h>
#include <Util/WidgetHelper.h>

#include <QColorDialog>
#include <QFormLayout>
#include <QLabel>
#include <QPushButton>

namespace EonTimer::Action {
    void setIconColor(QPushButton *btn, const QColor &newColor) {
        QPixmap pixmap(64, 64);
        pixmap.fill(newColor);
        QIcon icon(pixmap);
        btn->setIcon(icon);
    }

    ActionSettingsPane::ActionSettingsPane(ActionSettingsModel *settings, QWidget *parent)
        : QWidget(parent), settings(settings) {
        mode = settings->getMode();
        sound = settings->getSound();
        color = settings->getColor();
        interval = settings->getInterval().count();
        count = settings->getCount();
        initComponents();
    }

    void ActionSettingsPane::initComponents() {
        auto *form = new QGridLayout(this);
        // ----- mode -----
        {
            auto *field = new QComboBox();
            Util::FieldSet<QComboBox> fieldSet(0, new QLabel("Mode"), field);
            Util::addItems<ActionMode>(field, getActionModes(), QOverload<ActionMode>::of(&getName));
            field->setCurrentText(getName(settings->getMode()));
            Util::addFieldSet(form, fieldSet);

            connect(field, SIGNAL(currentIndexChanged(int)), this, SLOT(setMode(int)));
        }
        // ----- sound -----
        {
            auto *field = new QComboBox();
            auto *fieldSet = new Util::FieldSet<QComboBox>(1, new QLabel("Sound"), field);
            Util::addItems<Sound>(field, getSounds(), QOverload<Sound>::of(&getName));
            field->setCurrentText(getName(settings->getSound()));
            Util::addFieldSet(form, *fieldSet);

            connect(field, SIGNAL(currentIndexChanged(int)), this, SLOT(setSound(int)));
            connect(this, &ActionSettingsPane::modeChanged, [form, fieldSet](const ActionMode newValue) {
                const bool visible = newValue == AUDIO || newValue == AV;
                Util::setVisible(form, *fieldSet, visible);
            });
        }
        // ----- color -----
        {
            auto *field = new QPushButton;
            auto *fieldSet = new Util::FieldSet<QPushButton>(2, new QLabel("Color"), field);
            setIconColor(field, color);
            connect(field, &QPushButton::clicked, [this, field] {
                const QColor selectedColor = QColorDialog::getColor(color, this);
                if (selectedColor.isValid()) {
                    setIconColor(field, selectedColor);
                    color = selectedColor;
                }
            });
            connect(this, &ActionSettingsPane::modeChanged, [form, fieldSet](const ActionMode newValue) {
                Util::setVisible(form, *fieldSet, newValue == VISUAL || newValue == AV);
            });
            Util::addFieldSet(form, *fieldSet);
        }
        // ----- interval -----
        {
            auto *field = new QSpinBox();
            Util::FieldSet<QSpinBox> fieldSet(3, new QLabel("Interval"), field);
            Util::setModel(field, 1, 1000, interval);
            Util::addFieldSet(form, fieldSet);

            connect(field, SIGNAL(valueChanged(int)), this, SLOT(setInterval(int)));
        }
        // ----- count -----
        {
            auto *field = new QSpinBox();
            Util::FieldSet<QSpinBox> fieldSet(4, new QLabel("Count"), field);
            Util::setModel(field, 1, 50, count);
            Util::addFieldSet(form, fieldSet);

            connect(field, SIGNAL(valueChanged(int)), this, SLOT(setCount(int)));
        }
        // force all fieldsets to update visibility
        emit modeChanged(mode);
    }

    void ActionSettingsPane::updateSettings() {
        settings->setMode(getActionModes()[mode]);
        settings->setSound(getSounds()[sound]);
        settings->setInterval(interval);
        settings->setCount(count);
        settings->setColor(color);
    }

    void ActionSettingsPane::setMode(const int newValue) {
        setMode(static_cast<ActionMode>(newValue));
    }

    void ActionSettingsPane::setMode(const ActionMode newValue) {
        if (this->mode != newValue) {
            emit modeChanged(newValue);
            this->mode = newValue;
        }
    }

    void ActionSettingsPane::setSound(const int newValue) {
        setSound(static_cast<Sound>(newValue));
    }

    void ActionSettingsPane::setSound(const Sound newValue) { this->sound = newValue; }

    void ActionSettingsPane::setInterval(const i32 newValue) { this->interval = newValue; }

    void ActionSettingsPane::setCount(const i32 newValue) { this->count = newValue; }
}  // namespace EonTimer::Action
