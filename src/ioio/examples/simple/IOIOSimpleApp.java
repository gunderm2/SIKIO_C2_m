/*
*The MIT License (MIT)
*
*Copyright (c) 2015 Michael Gunderson
*
*Permission is hereby granted, free of charge, to any person obtaining a copy
*of this software and associated documentation files (the "Software"), to deal
*in the Software without restriction, including without limitation the rights
*to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
*copies of the Software, and to permit persons to whom the Software is
*furnished to do so, subject to the following conditions:
*
*The above copyright notice and this permission notice shall be included in
*all copies or substantial portions of the Software.
*
*THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
*IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
*FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
*AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
*LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
*OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
*THE SOFTWARE.
 */
package ioio.examples.simple;

import ioio.lib.api.AnalogInput;
import ioio.lib.api.DigitalInput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.TextView;

public class IOIOSimpleApp extends IOIOActivity {
//	private TextView heading;
	private TextView potLabel;
	private TextView potValue;
	private RadioButton buttonIO;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

//		heading = (TextView) findViewById(R.id.title);
		potLabel = (TextView) findViewById(R.id.potLabel);
		potValue = (TextView) findViewById(R.id.potValue);
		buttonIO = (RadioButton) findViewById(R.id.radioButton);
		buttonIO.setSelected(false);
	}

	class Looper extends BaseIOIOLooper {
		// Declare the IO pins for the button and potentiometer.
		DigitalInput button; // Our button is a DigitalInput
		AnalogInput pot; // Our potentiometer is an AnalogInput
		// Variables to store pin numbers
		int buttonPin = 7;
		int potPin = 40;

		// Variables to store analog and digital values
		float potVal; // Our analog values range between 0 and 1
		boolean buttonVal; // Digital is either 0 OR 1 (true or false)

		@Override
		public void setup() throws ConnectionLostException {
			// Opening the input pins.
		    button = ioio_.openDigitalInput(buttonPin);
		    pot = ioio_.openAnalogInput(potPin);
		}

		@Override
		public void loop() throws ConnectionLostException, InterruptedException {
			try
			  {
			    // While we're running, read our potentiometer and button values. The pot value is a
			    // number between 0 and 1. Button value is either a 0 or 1.
			    potVal = pot.read();
			    setPotValue(potVal);
			    buttonVal = button.read();
			    setRadioButton(buttonVal);
			    // Don't call this loop again for 100 milliseconds
			    Thread.sleep(100);
			  } 
			  catch (InterruptedException e) 
			  {
			  }
		}
	}

	@Override
	protected IOIOLooper createIOIOLooper() {
		return new Looper();
	}
	
	private void setRadioButton(final boolean value) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				buttonIO.setChecked(value);
			}
		});
	}
	
	private void setPotValue(final Float value) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				potValue.setText(value.toString());
			}
		});
	}
}