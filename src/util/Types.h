//
// Created by Dylan Meadows on 6/2/21.
//

#ifndef EONTIMER_TYPES_H
#define EONTIMER_TYPES_H

#include <chrono>

typedef const char *CStr;

typedef std::chrono::minutes Minutes;
typedef std::chrono::milliseconds Milliseconds;
typedef std::chrono::microseconds Microseconds;
typedef std::chrono::time_point<std::chrono::high_resolution_clock> Instant;

#define NOW std::chrono::high_resolution_clock::now()
#define ELAPSED(unit, start, stop) std::chrono::duration_cast<unit>((stop) - (start))

#define MINUTES(duration) std::chrono::duration_cast<Minutes>(duration)
#define MILLIS(duration) std::chrono::duration_cast<Milliseconds>(duration)
#define MICROS(duration) std::chrono::duration_cast<Microseconds>(duration)

#endif  // EONTIMER_TYPES_H
