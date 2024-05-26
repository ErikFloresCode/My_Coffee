package com.example.mycoffee

import java.time.LocalTime

class Hours {
    var id: Int = 0
    var dia: String = ""
    var horaIni: LocalTime = LocalTime.of(0, 0)
    var horaFin: LocalTime = LocalTime.of(0, 0)
    var status: Int = 0
}