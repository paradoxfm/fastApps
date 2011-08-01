package com.authorwjf;

import java.util.ArrayList;
import com.authorwjf.CustomMenu.OnMenuItemSelectedListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * Demo class
 *
 * This is class demonstrates how to use the custom menu helper classes.
 *
 * @category   Demos
 * @author     William J Francis (w.j.francis@tx.rr.com)
 * @copyright  Enjoy!
 * @version    1.0
 */

public class Demo extends Activity implements OnMenuItemSelectedListener {
	
	/**
	 * Some global variables.
	 */
	private CustomMenu mMenu;
	public static final int MENU_ITEM_1 = 1;
	public static final int MENU_ITEM_2 = 2;
	public static final int MENU_ITEM_3 = 3;
	public static final int MENU_ITEM_4 = 4;
	
    /**
     * Always called when an Android activity launches.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //create the view
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //initialize the menu
        mMenu = new CustomMenu(this, this, getLayoutInflater());
        mMenu.setHideOnSelect(true);
        mMenu.setItemsPerLineInPortraitOrientation(4);
        mMenu.setItemsPerLineInLandscapeOrientation(8);
        //load the menu items
        loadMenuItems();
    }
    
    /**
     * Snarf the menu key.
     */
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    if (keyCode == KeyEvent.KEYCODE_MENU) {
	    	doMenu();
	    	return true; //always eat it!
	    }
		return super.onKeyDown(keyCode, event); 
	} 
	
	/**
     * Load up our menu.
     */
	private void loadMenuItems() {
		//This is kind of a tedious way to load up the menu items.
		//Am sure there is room for improvement.
		ArrayList<CustomMenuItem> menuItems = new ArrayList<CustomMenuItem>();
		CustomMenuItem cmi = new CustomMenuItem();
		cmi.setCaption("First");
		cmi.setImageResourceId(R.drawable.icon1);
		cmi.setId(MENU_ITEM_1);
		menuItems.add(cmi);
		cmi = new CustomMenuItem();
		cmi.setCaption("Second");
		cmi.setImageResourceId(R.drawable.icon2);
		cmi.setId(MENU_ITEM_2);
		menuItems.add(cmi);
		cmi = new CustomMenuItem();
		cmi.setCaption("Third");
		cmi.setImageResourceId(R.drawable.icon3);
		cmi.setId(MENU_ITEM_3);
		menuItems.add(cmi);
		cmi = new CustomMenuItem();
		cmi.setCaption("Fourth");
		cmi.setImageResourceId(R.drawable.icon4);
		cmi.setId(MENU_ITEM_4);
		menuItems.add(cmi);
		if (!mMenu.isShowing())
		try {
			mMenu.setMenuItems(menuItems);
		} catch (Exception e) {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("Egads!");
			alert.setMessage(e.getMessage());
			alert.show();
		}
	}
	
	/**
     * Toggle our menu on user pressing the menu key.
     */
	private void doMenu() {
		if (mMenu.isShowing()) {
			mMenu.hide();
		} else {
			//Note it doesn't matter what widget you send the menu as long as it gets view.
			mMenu.show(findViewById(R.id.any_old_widget));
		}
	}

	/**
     * For the demo just toast the item selected.
     */
	@Override
	public void MenuItemSelectedEvent(CustomMenuItem selection) {
		Toast t = Toast.makeText(this, "You selected item #"+Integer.toString(selection.getId()), Toast.LENGTH_SHORT);
		t.setGravity(Gravity.CENTER, 0, 0);
		t.show();
	}
}