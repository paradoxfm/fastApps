package Forms;

import javax.swing.*;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import java.io.*;

import javax.xml.parsers.*;

import java.awt.event.*;
import java.util.*;

/** Класс загрузки меню из Xml */
public class XmlMenuLoader {
	/** Источник XML */
	private InputSource source;
	/** SAX парсер XML */
	private SAXParser parser;
	/** Просмотрщик XML */
	private DefaultHandler documentHandler;
	/** Хранилище пар элементов меню - ID */
	private Map<String, JComponent> menuStorage = new HashMap<String, JComponent>();

	/** Конструктор загрузчика меню */
	public XmlMenuLoader(InputStream stream) {
		try {
			parser = SAXParserFactory.newInstance().newSAXParser();
			Reader reader = new InputStreamReader(stream, "UTF-8");
			source = new InputSource(reader);
		} catch (Exception ex) {
		}
		documentHandler = new XMLParser();
	}

	/** Вызов парсинга */
	public void parse() throws Exception {
		parser.parse(source, documentHandler);
	}

	/** Возвращает распасеное меню */
	public JMenuBar getMenuBar(String name) {
		return (JMenuBar) menuStorage.get(name);
	}

	/** Возвращает меню, по строковому идентификатору */
	public JMenu getMenu(String name) {
		return (JMenu) menuStorage.get(name);
	}

	/** Возвращает элемент меню по стрококвому идентивикатору */
	public JMenuItem getMenuItem(String name) {
		return (JMenuItem) menuStorage.get(name);
	}

	/** Добавление обработчика событий элементу меню по ID */
	public void addActionListener(String name, ActionListener listener) {
		getMenuItem(name).addActionListener(listener);
	}

	/** Текущее меню */
	private JMenuBar currentMenuBar;
	/** Связаный список элементов меню */
	private LinkedList<JMenu> menus = new LinkedList<JMenu>();

	/** Парсер XML */
	class XMLParser extends DefaultHandler {
		/**  */
		public void startElement(String uri, String localName, String qName, Attributes attributes) {
			if (qName.equals("menubar"))
				parseMenuBar(attributes);
			else if (qName.equals("menu"))
				parseMenu(attributes);
			else if (qName.equals("menuitem"))
				parseMenuItem(attributes);
		}

		/**  */
		public void endElement(String uri, String localName, String qName) {
			if (qName.equals("menu"))
				menus.removeFirst();
		}

		/**  */
		protected void parseMenuBar(Attributes attrs) {
			JMenuBar menuBar = new JMenuBar();
			String name = attrs.getValue("name");
			menuStorage.put(name, menuBar);
			currentMenuBar = menuBar;
		}

		/**  */
		protected void parseMenu(Attributes attrs) {
			JMenu menu = new JMenu();
			String name = attrs.getValue("name");
			adjustProperties(menu, attrs);
			menuStorage.put(name, menu);
			if (menus.size() != 0)
				((JMenu) menus.getFirst()).add(menu);
			else
				currentMenuBar.add(menu);
			menus.addFirst(menu);
		}

		/**  */
		protected void parseMenuItem(Attributes attrs) {
			String name = attrs.getValue("name");
			if (name.equals("separator")) {
				((JMenu) menus.getFirst()).addSeparator();
				return;
			}
			JMenuItem menuItem = new JMenuItem();
			adjustProperties(menuItem, attrs);
			menuStorage.put(name, menuItem);
			((JMenu) menus.getFirst()).add(menuItem);
		}

		/**  */
		private void adjustProperties(JMenuItem menuItem, Attributes attrs) {
			String text = attrs.getValue("text");
			String mnemonic = attrs.getValue("mnemonic");
			String accelerator = attrs.getValue("accelerator");
			String enabled = attrs.getValue("enabled");
			menuItem.setText(text);
			if (mnemonic != null)
				menuItem.setMnemonic(mnemonic.charAt(0));
			if (accelerator != null)
				menuItem.setAccelerator(KeyStroke.getKeyStroke(accelerator));
			if (enabled != null) {
				boolean isEnabled = true;
				if (enabled.equals("false"))
					isEnabled = false;
				menuItem.setEnabled(isEnabled);
			}
		}
	}
}
