//
// Created by Dylan Meadows on 10/7/21.
//

#include "QSettingsProperty.h"

#include <utility>

namespace EonTimer::Util {
    QSettingsProperty::QSettingsProperty(QString name, QVariant defaultValue)
        : name(std::move(name)), defaultValue(std::move(defaultValue)) {}

    [[nodiscard]] QVariant QSettingsProperty::getValue(const QSettings &settings) const {
        return settings.value(name, defaultValue);
    }

    void QSettingsProperty::setValue(QSettings &settings, const QVariant &newValue) {
        settings.setValue(name, newValue);
    }
}  // namespace EonTimer::Util