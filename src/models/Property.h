//
// Created by dylanmeadows on 10/5/21.
//

#ifndef EON_TIMER_PROPERTY_H
#define EON_TIMER_PROPERTY_H

#include <QSettings>
#include <utility>

namespace EonTimer {
    struct Property {
        const QString name;
        const QVariant defaultValue;

        Property(QString name, QVariant defaultValue) : name(std::move(name)), defaultValue(std::move(defaultValue)) {}

        [[nodiscard]] QVariant getValue(const QSettings &settings) { return settings.value(name, defaultValue); }

        void setValue(QSettings &settings, const QVariant &newValue) { settings.setValue(name, newValue); }
    };
}  // namespace EonTimer

#endif  // EON_TIMER_PROPERTY_H
