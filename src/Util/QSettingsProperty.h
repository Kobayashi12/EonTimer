//
// Created by dylanmeadows on 10/5/21.
//

#pragma once

#include <QSettings>
#include <utility>

namespace EonTimer::Util {
    struct QSettingsProperty {
        const QString name;
        const QVariant defaultValue;

        QSettingsProperty(QString name, QVariant defaultValue);
        [[nodiscard]] QVariant getValue(const QSettings &settings) const;
        void setValue(QSettings &settings, const QVariant& newValue);
    };
}  // namespace EonTimer
