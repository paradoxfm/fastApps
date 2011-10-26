package ru.megazlo.ledxremote.util;

/** Вспомогательные утилиты */
public abstract class Util {
	/**
	 * Подсчет контрольной суммы пакета
	 * 
	 * @param arr
	 *          пакет передачи
	 */
	public static byte CheckSum(byte[] arr) {
		byte rez = 0;
		for (int i = 1; i < arr.length - 2; i++)
			rez += arr[i] & 0xFF;
		return rez;
	}
}
