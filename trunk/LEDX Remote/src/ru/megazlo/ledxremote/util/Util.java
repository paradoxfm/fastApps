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
		int rez = 0;
		for (int i = 0; i < arr.length - 2; i++)
			rez += arr[i] & 0xFF;

		byte qwe = (byte) rez;
		return qwe;
	}
}
