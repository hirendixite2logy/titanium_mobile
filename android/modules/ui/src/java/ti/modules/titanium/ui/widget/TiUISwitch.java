/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2012 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
package ti.modules.titanium.ui.widget;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.util.TiConvert;
import org.appcelerator.titanium.util.TiUIHelper;
import org.appcelerator.titanium.view.TiUIView;

import ti.modules.titanium.ui.android.AndroidModule;
import android.os.Build;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.ToggleButton;

public class TiUISwitch extends TiUIView
	implements OnCheckedChangeListener
{
	private static final String TAG = "TiUISwitch";
	
	private boolean oldValue = false;
	
	public TiUISwitch(TiViewProxy proxy) {
		super(proxy);
		Log.d(TAG, "Creating a switch", Log.DEBUG_MODE);

		propertyChanged(TiC.PROPERTY_STYLE, null, proxy.getProperty(TiC.PROPERTY_STYLE), proxy);
	}

	@Override
	public void processProperties(KrollDict d)
	{
		super.processProperties(d);

		if (d.containsKey(TiC.PROPERTY_STYLE)) {
			setStyle(TiConvert.toInt(d.get(TiC.PROPERTY_STYLE), AndroidModule.SWITCH_STYLE_TOGGLEBUTTON));
		}

		if (d.containsKey(TiC.PROPERTY_VALUE)) {
			oldValue = TiConvert.toBoolean(d, TiC.PROPERTY_VALUE);
		}

		View nativeView = getNativeView();
		if (nativeView != null) {
			updateButton((CompoundButton)nativeView, d);
		}
	}
	
	protected void updateButton(CompoundButton cb, KrollDict d) {
		if (d.containsKey(TiC.PROPERTY_TITLE) && cb instanceof CheckBox) {
			cb.setText(TiConvert.toString(d, TiC.PROPERTY_TITLE));
		}
		if (d.containsKey(TiC.PROPERTY_TITLE_OFF)) {
			if (cb instanceof ToggleButton) {
				((ToggleButton) cb).setTextOff(TiConvert.toString(d, TiC.PROPERTY_TITLE_OFF));
			} else if (Build.VERSION.SDK_INT >= 14 && cb instanceof Switch) { // ICE_CREAM_SANDWICH, 4.0
				((Switch) cb).setTextOff(TiConvert.toString(d, TiC.PROPERTY_TITLE_OFF));
			}

		}
		if (d.containsKey(TiC.PROPERTY_TITLE_ON)) {
			if (cb instanceof ToggleButton) {
				((ToggleButton) cb).setTextOn(TiConvert.toString(d, TiC.PROPERTY_TITLE_ON));
			} else if (Build.VERSION.SDK_INT >= 14 && cb instanceof Switch) { 
				((Switch) cb).setTextOn(TiConvert.toString(d, TiC.PROPERTY_TITLE_ON));
			}
		}
		if (d.containsKey(TiC.PROPERTY_VALUE)) {
		
			cb.setChecked(TiConvert.toBoolean(d, TiC.PROPERTY_VALUE));
		}
		if (d.containsKey(TiC.PROPERTY_COLOR)) {
			cb.setTextColor(TiConvert.toColor(d, TiC.PROPERTY_COLOR));
		}
		if (d.containsKey(TiC.PROPERTY_FONT)) {
			TiUIHelper.styleText(cb, d.getKrollDict(TiC.PROPERTY_FONT));
		}
		if (d.containsKey(TiC.PROPERTY_TEXT_ALIGN)) {
			String textAlign = d.getString(TiC.PROPERTY_TEXT_ALIGN);
			TiUIHelper.setAlignment(cb, textAlign, null);
		}
		if (d.containsKey(TiC.PROPERTY_VERTICAL_ALIGN)) {
			String verticalAlign = d.getString(TiC.PROPERTY_VERTICAL_ALIGN);
			TiUIHelper.setAlignment(cb, null, verticalAlign);
		}
		cb.invalidate();
	}


	@Override
	public void propertyChanged(String key, Object oldValue, Object newValue, KrollProxy proxy)
	{
		if (Log.isDebugModeEnabled()) {
			Log.d(TAG, "Property: " + key + " old: " + oldValue + " new: " + newValue, Log.DEBUG_MODE);
		}
		
		CompoundButton cb = (CompoundButton) getNativeView();
		if (key.equals(TiC.PROPERTY_STYLE) && newValue != null) {
			setStyle(TiConvert.toInt(newValue));
		} else if (key.equals(TiC.PROPERTY_TITLE) && cb instanceof CheckBox) {
			cb.setText((String) newValue);
		} else if (key.equals(TiC.PROPERTY_TITLE_OFF)) {
			if (cb instanceof ToggleButton) {
				((ToggleButton) cb).setTextOff((String) newValue);
			} else if (Build.VERSION.SDK_INT >= 14) { // ICE_CREAM_SANDWICH, 4.0
				if (cb instanceof Switch) {
					((Switch) cb).setTextOff((String) newValue);
				}
			}

		} else if (key.equals(TiC.PROPERTY_TITLE_ON)) {
			if (cb instanceof ToggleButton) {
				((ToggleButton) cb).setTextOn((String) newValue);
			} else if (Build.VERSION.SDK_INT >= 14 && cb instanceof Switch) { // ICE_CREAM_SANDWICH, 4.0 
				((Switch) cb).setTextOn((String) newValue);
			}

		} else if (key.equals(TiC.PROPERTY_VALUE)) {
			cb.setChecked(TiConvert.toBoolean(newValue));
		} else if (key.equals(TiC.PROPERTY_COLOR)) {
			cb.setTextColor(TiConvert.toColor(TiConvert.toString(newValue)));
		} else if (key.equals(TiC.PROPERTY_FONT)) {
			TiUIHelper.styleText(cb, (KrollDict) newValue);
		} else if (key.equals(TiC.PROPERTY_TEXT_ALIGN)) {
			TiUIHelper.setAlignment(cb, TiConvert.toString(newValue), null);
			cb.requestLayout();
		} else if (key.equals(TiC.PROPERTY_VERTICAL_ALIGN)) {
			TiUIHelper.setAlignment(cb, null, TiConvert.toString(newValue));
			cb.requestLayout();
		} else {
			super.propertyChanged(key, oldValue, newValue, proxy);
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton btn, boolean value) {
		KrollDict data = new KrollDict();

		proxy.setProperty(TiC.PROPERTY_VALUE, value);
		//if user triggered change, we fire it.
		if (oldValue != value) {
			data.put(TiC.PROPERTY_VALUE, value);
			fireEvent(TiC.EVENT_CHANGE, data);
			oldValue = value;
		}
	}
	
	protected void setStyle(int style)
	{
		CompoundButton currentButton = (CompoundButton) getNativeView();
		CompoundButton button = null;

		switch (style) {
			case AndroidModule.SWITCH_STYLE_CHECKBOX:
				if (!(currentButton instanceof CheckBox)) {
					button = new CheckBox(proxy.getActivity())
					{
						@Override
						protected void onLayout(boolean changed, int left, int top, int right, int bottom)
						{
							super.onLayout(changed, left, top, right, bottom);
							TiUIHelper.firePostLayoutEvent(proxy);
						}
					};
				}
				break;

			case AndroidModule.SWITCH_STYLE_TOGGLEBUTTON_OLD:
			case AndroidModule.SWITCH_STYLE_TOGGLEBUTTON:
				// ICE_CREAM_SANDWICH, 4.0
				if (style != AndroidModule.SWITCH_STYLE_TOGGLEBUTTON_OLD && Build.VERSION.SDK_INT >= 14) { 
					if (!(currentButton instanceof Switch)) {
						button = new Switch(proxy.getActivity())
						{
							@Override
							protected void onLayout(boolean changed, int left, int top, int right, int bottom)
							{
								super.onLayout(changed, left, top, right, bottom);
								TiUIHelper.firePostLayoutEvent(proxy);
							}
						};
					}
				} else {
					if (!(currentButton instanceof ToggleButton)) {
						button = new ToggleButton(proxy.getActivity())
						{
							@Override
							protected void onLayout(boolean changed, int left, int top, int right, int bottom)
							{
								super.onLayout(changed, left, top, right, bottom);
								TiUIHelper.firePostLayoutEvent(proxy);
							}
						};
					}
				}
				break;

			default:
				return;
		}

		if (button != null) {
			setNativeView(button);
			updateButton(button, proxy.getProperties());
			button.setOnCheckedChangeListener(this);
		}
	}
}
