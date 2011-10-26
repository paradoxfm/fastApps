package ru.megazlo.ledxremote.enums;

/** Команда передаваемая контроллеру */
public abstract class Cmd {
	/** Проверка связи */
	public final byte ECHO = 0x00;
	/** Установка цвета */
	public final byte SET_COLOR = 0x01;
	/** Переключение программы */
	public final byte RUN_PROG = 0x02;
	/** Приостановка выполнения программы */
	public final byte PAUSE_PROG = 0x03;
	/** Возобновление выполнения программы */
	public final byte RESUME_PROG = 0x04;
	/** Установить скорость */
	public final byte CHANGE_SP = 0x05;
	/** Установить яркость */
	public final byte CHANGE_BR = 0x06;
	/** Начать синхронизацию */
	public final byte START_SYNC = 0x07;
	/** Закончить синхронизацию */
	public final byte STOP_SYNC = 0x08;
	/** Войти режим программирования */
	public final byte ENTER_PROG_MODE = 0x09;
	/** Записать фрейм программы */
	public final byte WRITE_FRAME = 0x0A;
	/** Завершить программу и выйти из режима программирования */
	public final byte FINILIZE_PROG = 0x0B;
	/** Синхро команда. С компьютера не используется */
	public final byte SYNC = 0x0C;
	/** Войти в режим Live/DMX */
	public final byte LIVEDMX = 0x0D;
	/** Включение/выключение контроллера */
	public final byte ONOFFCOMM = 0x0E;
	/** Зарезервировано */
	public final byte CHANGE_CTR_NUM = 0x0F;
	/** Переключение режима паузы */
	public final byte PAUSE_TOGGLE = 0x10;
	/** Запрос состояния контроллера */
	public final byte GET_STATE = 0x11;
}