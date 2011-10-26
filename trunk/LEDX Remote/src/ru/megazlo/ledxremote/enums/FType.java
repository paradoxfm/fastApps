package ru.megazlo.ledxremote.enums;

/** Тип фрейма передаваемого контроллеру */
public abstract class FType {
	/** Запрос */
	public static final byte Request = (byte) 'R';
	/** Подтверждение */
	public static final byte Confirm = (byte) 'C';
	/** Сигнал */
	public static final byte Signal = (byte) 's';
}
