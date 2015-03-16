/*
 * Copyright 2015 Michael Gunderson. All rights reserved.
 *
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL ARSHAN POURSOHI OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied.
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