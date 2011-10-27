package ru.megazlo.ledxremote.enums;

/** Системная структура с стартовым и стоповым байтами */
public abstract class Sys {
	/** Стартовый байт, означающий начало пакета данных */
	public static final byte Start = (byte) 0xAA;
	/** Стоповый байт, означающий конец пакета передаваемых данных */
	public static final byte Stop = (byte) 0x55;
}
