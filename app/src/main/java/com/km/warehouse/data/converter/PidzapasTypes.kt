package com.km.warehouse.data.converter

/**
 * Create by Pustovit Oleksandr on 26/08/2026
 * Київ:
 *
 * КМДИСТИ
 * ПРОЦЕСОРИ
 *
 * Львів:
 *
 * ЛЬВІВ
 * ГП
 * КАСКА
 * ШЛЕМИ
 * КРОНА
 * ПФ
 * СБОРКА
 * ОМЗБ
 * МАЛЯР
 * ДППМ
 * РОЗКРІЙ
 */
enum class PidzapasTypes(val sybType: String, val warehouseName: String) {
    KMDISTY("Київ", "КМДИСТИ"),
    PROCESSORS("Київ", "ПРОЦЕСОРИ"),
    LVIV("Львів", "ЛЬВІВ"),
    GP("Львів", "ГП"),
    KASKA("Львів", "КАСКА"),
    HELMET("Львів", "ШЛЕМИ"),
    CRONA("Львів", "КРОНА"),
    PF("Львів", "ПФ"),
    SBORKA("Львів", "СБОРКА"),
    OMZB("Львів", "ОМЗБ"),
    MALAR("Львів", "МАЛЯР"),
    DPPM("Львів", "ДППМ"),
    ROZKRII("Львів", "РОЗКРІЙ")
}