package ru.megazlo.ledxremote.enums;

/** Команда передаваемая контроллеру */
public abstract class Cmd {
	/** Проверка связи */
	public static final byte ECHO = 0x00;
	/** Установка цвета */
	public static final byte SET_COLOR = 0x01;
	/** Переключение программы */
	public static final byte RUN_PROG = 0x02;
	/** Приостановка выполнения программы */
	public static final byte PAUSE_PROG = 0x03;
	/** Возобновление выполнения программы */
	public static final byte RESUME_PROG = 0x04;
	/** Установить скорость */
	public static final byte CHANGE_SP = 0x05;
	/** Установить яркость */
	public static final byte CHANGE_BR = 0x06;
	/** Начать синхронизацию */
	public static final byte START_SYNC = 0x07;
	/** Закончить синхронизацию */
	public static final byte STOP_SYNC = 0x08;
	/** Войти режим программирования */
	public static final byte ENTER_PROG_MODE = 0x09;
	/** Записать фрейм программы */
	public static final byte WRITE_FRAME = 0x0A;
	/** Завершить программу и выйти из режима программирования */
	public static final byte FINILIZE_PROG = 0x0B;
	/** Синхро команда. С компьютера не используется */
	public static final byte SYNC = 0x0C;
	/** Войти в режим Live/DMX */
	public static final byte LIVEDMX = 0x0D;
	/** Включение/выключение контроллера */
	public static final byte ONOFFCOMM = 0x0E;
	/** Зарезервировано */
	public static final byte CHANGE_CTR_NUM = 0x0F;
	/** Переключение режима паузы */
	public static final byte PAUSE_TOGGLE = 0x10;
	/** Запрос состояния контроллера */
	public static final byte GET_STATE = 0x11;
}