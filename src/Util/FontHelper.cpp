//
// Created by Dylan Meadows on 2020-03-10.
//

#include "FontHelper.h"

namespace EonTimer::Util {
    void setFontSize(QWidget *widget, const int fontSize) {
        QFont font = widget->font();
        font.setPointSize(fontSize);
        widget->setFont(font);
    }
}  // namespace EonTimer::Util
