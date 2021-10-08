//
// Created by dmeadows on 5/22/20.
//

#pragma once

#include <QComboBox>
#include <functional>
#include <iostream>

namespace EonTimer::Util {
    template <typename T>
    inline void addItems(QComboBox* comboBox, const std::vector<T>& items, std::function<QString(T)> textMapper) {
        comboBox->clear();
        for (auto item : items) {
            const QString text = textMapper(item);
            std::cout << text.toStdString() << std::endl;
            comboBox->addItem(text, item);
        }
    }

    inline void setModel(QSpinBox* spinBox, int min, int max, int value) {
        spinBox->setRange(min, max);
        spinBox->setValue(value);
    }
}  // namespace EonTimer::Util
