/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.cbase.blinkendroid.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.cbase.blinkendroid.Constants;
import org.cbase.blinkendroid.OldBlinkendroid;



import android.util.Log;

public class Server extends Thread{
	private boolean running=false;
	private OldBlinkendroid blinkendroid;
	public Server(OldBlinkendroid blinkendroid) {
		this.blinkendroid =blinkendroid;
	}

	@Override
	public void run() {
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(4444);
		} catch (IOException e) {
			Log.e(Constants.LOG_TAG, "Could not create Socket",e);
			return;
		}
		running=true;
		Log.i(Constants.LOG_TAG,"Server Thread started");
		while(running){
			Socket clientSocket;
			try {
				clientSocket = serverSocket.accept();
				new ConnectionThread(blinkendroid, clientSocket).start();	
			} catch (IOException e) {
				Log.e(Constants.LOG_TAG, "Could not accept",e);
			}
		}
		try {
			serverSocket.close();
		} catch (IOException e) {
			Log.e(Constants.LOG_TAG, "Could not close",e);
		}
		Log.i(Constants.LOG_TAG,"Server Thread closed");
	}

	public void end(){
		running=false;
		Log.i(Constants.LOG_TAG, "Server Thread ended");
		interrupt();
	}
	
	public boolean isRunning() {
	    return running;
	}
}