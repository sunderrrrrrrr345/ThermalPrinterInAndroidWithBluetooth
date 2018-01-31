
package sunder.pinsu.chhama.admin.thermalprinterinandroidwithbluetooth;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.RT_Printer.BluetoothPrinter.BLUETOOTH.BluetoothPrintDriver;


public class BloothPrinterActivity extends Activity {
	// Debugging
    private static final String TAG = "BloothPrinterActivity";
    private static final boolean D = true;
    
    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    
    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    
    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;     
    public static int revBytes=0;   
    public  static boolean isHex=false;
    
    public static final int REFRESH = 8;
    
    // Layout Views
    private TextView mTitle;
    
    /** Called when the activity is first created. */
	private Button mBtnConnetBluetoothDevice = null;
	private Button mBtnQuit = null;
	private Button mBtnPrint = null;
	private Button mBtnPrintOption = null;
	private Button mBtnTest = null;
	private Button mBtnInquiry = null;
	private EditText mPrintContent = null;
	private CheckBox mBeiKuan = null;
	private CheckBox mUnderline = null;
	private CheckBox mBold = null;
	private CheckBox mBeiGao = null;
	private CheckBox mMinifont = null;
	private CheckBox mHightlight = null;
	
	// Name of the connected device
    private String mConnectedDeviceName = null;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BluetoothPrintDriver1 mChatService = null;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(D) Log.e(TAG, "+++ ON CREATE +++");
        
        // Set up the window layout
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);

        // Set up the custom title
        mTitle = (TextView) findViewById(R.id.title_left_text);
        mTitle.setText(R.string.app_name);
        mTitle = (TextView) findViewById(R.id.title_right_text);
        
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
        // �����ʼ��
        InitUIControl();
    }

	private void InitUIControl(){
    	mBtnQuit = (Button)findViewById(R.id.btn_quit);
    	mBtnQuit.setOnClickListener(mBtnQuitOnClickListener);
    	mBtnConnetBluetoothDevice = (Button)findViewById(R.id.btn_connect_bluetooth_device);
    	mBtnConnetBluetoothDevice.setOnClickListener(mBtnConnetBluetoothDeviceOnClickListener);
    	mBtnPrint = (Button)findViewById(R.id.btn_print);
    	mBtnPrint.setOnClickListener(mBtnPrintOnClickListener);
    	mBtnPrintOption = (Button)findViewById(R.id.btn_option);
    	mBtnPrintOption.setOnClickListener(mBtnPrintOptionOnClickListener);
    	mBtnTest = (Button)findViewById(R.id.btn_test);
    	mBtnTest.setOnClickListener(mBtnTestOnClickListener);
    	mBtnInquiry = (Button)findViewById(R.id.btn_bt_inquiry);
    	mBtnInquiry.setOnClickListener(mBtnInquiryOnClickListener);
    	mPrintContent = (EditText)findViewById(R.id.edt_print_content);
    	mBeiKuan = (CheckBox)findViewById(R.id.checkbox_beikuan);
    	mUnderline = (CheckBox)findViewById(R.id.checkbox_underline);
    	mBold = (CheckBox)findViewById(R.id.checkbox_bold);
    	mBeiGao = (CheckBox)findViewById(R.id.checkbox_beigao);
    	mMinifont = (CheckBox)findViewById(R.id.checkbox_minifont);
    	mHightlight = (CheckBox)findViewById(R.id.checkbox_hightlight);
    }

	@Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "++ ON START ++");

        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        // Otherwise, setup the chat session
        } else {
            if (mChatService == null) setupChat();
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if(D) Log.e(TAG, "+ ON RESUME +");

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothPrintDriver1.STATE_NONE) {
              // Start the Bluetooth chat services
              mChatService.start();
            }
        }
    }
    
    private void setupChat() {
        Log.d(TAG, "setupChat()");
        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothPrintDriver1(this, mHandler);
    }
    
    @Override
    public synchronized void onPause() {
        super.onPause();
        if(D) Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if(D) Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (mChatService != null) mChatService.stop();
        if(D) Log.e(TAG, "--- ON DESTROY ---");
    }
    
    @SuppressLint("NewApi")
	private void ensureDiscoverable() {
        if(D) Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() !=
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }
    
    // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                case BluetoothPrintDriver1.STATE_CONNECTED:
                	mTitle.setText(R.string.title_connected_to);
                    mTitle.append(mConnectedDeviceName);
                	//setTitle(R.string.title_connected_to);
                	//setTitle(mConnectedDeviceName);
                    break;
                case BluetoothPrintDriver1.STATE_CONNECTING:
                	mTitle.setText(R.string.title_connecting);
                	//setTitle(R.string.title_connecting);
                    break;
                case BluetoothPrintDriver1.STATE_LISTEN:
                case BluetoothPrintDriver1.STATE_NONE:
                	mTitle.setText(R.string.title_not_connected);
                	//setTitle(R.string.title_not_connected);
                    break;
                }
                break;
            case MESSAGE_WRITE:
                break;
            case MESSAGE_READ:
            	String ErrorMsg = null;
            	byte[] readBuf = (byte[]) msg.obj;
                float Voltage = 0;
                if(D) Log.i(TAG, "readBuf[0]:"+readBuf[0]+"  readBuf[1]:"+readBuf[1]+"  readBuf[2]:"+readBuf[2]);
                if(readBuf[2]==0)
                	ErrorMsg = "NO ERROR!         ";
                else
                {
	                if((readBuf[2] & 0x02) != 0)
	                	ErrorMsg = "ERROR: No printer connected!";
	                if((readBuf[2] & 0x04) != 0)
	                	ErrorMsg = "ERROR: No paper!  ";
	                if((readBuf[2] & 0x08) != 0)
	                	ErrorMsg = "ERROR: Voltage is too low!  ";
	                if((readBuf[2] & 0x40) != 0)
	                	ErrorMsg = "ERROR: Printer Over Heat!  ";
                }
                Voltage = (float) ((readBuf[0]*256 + readBuf[1])/10.0);
                //if(D) Log.i(TAG, "Voltage: "+Voltage);
                DisplayToast(ErrorMsg+"                                        "+"Battery voltage��"+Voltage+" V");
                break;
            case MESSAGE_DEVICE_NAME:
                // save the connected device's name
                mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                Toast.makeText(getApplicationContext(), "Connected to "
                               + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                               Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };
    
	
	//��ʾ��Ϣ
    public void showMessage(String str)
    {
        Toast.makeText(this,str, Toast.LENGTH_LONG).show();
    }//showMessage
    
    // ��ʾToast
	public void DisplayToast(String str)
	{
		Toast toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
		//����toast��ʾ��λ��
		toast.setGravity(Gravity.TOP, 0, 100);
		//��ʾ��Toast
		toast.show();
	}//DisplayToast

	 @Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
	        if(D) Log.d(TAG, "onActivityResult " + resultCode);
	        switch (requestCode) {
	        case REQUEST_CONNECT_DEVICE:
	            // When DeviceListActivity returns with a device to connect
	            if (resultCode == Activity.RESULT_OK) {
	                // Get the device MAC address
	                String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
	                // Get the BLuetoothDevice object
	                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
	                // Attempt to connect to the device
	                mChatService.connect(device);
	            }
	            break;
	        case REQUEST_ENABLE_BT:
	            // When the request to enable Bluetooth returns
	            if (resultCode == Activity.RESULT_OK) {
	                // Bluetooth is now enabled, so set up a chat session
	                setupChat();
	            } else {
	                // User did not enable Bluetooth or an error occured
	                Log.d(TAG, "BT not enabled");
	                //Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
	                finish();
	            }
	        }
	    }


	OnClickListener mBtnQuitOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// Stop the Bluetooth chat services
	        if (mChatService != null) mChatService.stop();
			finish();
		}
	};

	OnClickListener mBtnConnetBluetoothDeviceOnClickListener = new OnClickListener() {
		Intent serverIntent = null;
		public void onClick(View arg0)
		{
			// Launch the DeviceListActivity to see devices and do scan
            serverIntent = new Intent(BloothPrinterActivity.this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
		}
	};

	OnClickListener mBtnPrintOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(BluetoothPrintDriver1.IsNoConnection()){
				return;
			}
			BluetoothPrintDriver1.Begin();
			if(mBeiKuan.isChecked()){
				BluetoothPrintDriver1.SetFontEnlarge((byte)0x10);
			}
			if(mBeiGao.isChecked()){
				BluetoothPrintDriver1.SetFontEnlarge((byte)0x01);
			}
			if(mUnderline.isChecked()){
				BluetoothPrintDriver1.SetUnderline((byte)0x02);//�»���
			}
			if(mBold.isChecked()){
				BluetoothPrintDriver1.SetBold((byte)0x01);//����
			}
			if(mMinifont.isChecked()){
				BluetoothPrintDriver1.SetCharacterFont((byte)0x01);
			}
			if(mHightlight.isChecked()){
				BluetoothPrintDriver1.SetBlackReversePrint((byte)0x01);
			}
			String tmpContent = mPrintContent.getText().toString();
			BluetoothPrintDriver1.BT_Write(tmpContent);
			BluetoothPrintDriver1.BT_Write("\r");
		}
	};

	OnClickListener mBtnPrintOptionOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(BloothPrinterActivity.this, PrinterOptionActivity.class);
			//intent.putExtra("mBloothPrinter", mBloothPrinter);
			startActivity(intent);
		}
	};
	OnClickListener mBtnTestOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(BluetoothPrintDriver1.IsNoConnection()){
				return;
			}
			BluetoothPrintDriver1.Begin();
			BluetoothPrintDriver1.SelftestPrint();	//��ӡ�Բ�ҳ
		}
	};
	OnClickListener mBtnInquiryOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			//Log.i(TAG, "inquiry btn");
			if(BluetoothPrintDriver1.IsNoConnection()){
				return;
			}
			BluetoothPrintDriver1.Begin();
	    	BluetoothPrintDriver1.StatusInquiry();    // ��ѯ����״̬���ص�ѹ
	    	/*
			BluetoothPrintDriver1.SetFontEnlarge((byte) 0x10);
			BluetoothPrintDriver1.BT_Write("CODEPAGE TEST:\r\n");
			BluetoothPrintDriver1.SetFontEnlarge((byte) 0x00);
			byte[] cmd=new byte[16];
			byte i;
			for(i = 0;i<16;i++)
				cmd[i]=(byte) (0x80+i);
			BluetoothPrintDriver1.BT_Write(cmd);
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.CR();
			for(i = 0;i<16;i++)
				cmd[i]=(byte) (0x90+i);
			BluetoothPrintDriver1.BT_Write(cmd);
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.CR();
			for(i = 0;i<16;i++)
				cmd[i]=(byte) (0xA0+i);
			BluetoothPrintDriver1.BT_Write(cmd);
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.CR();
			for(i = 0;i<16;i++)
				cmd[i]=(byte) (0xB0+i);
			BluetoothPrintDriver1.BT_Write(cmd);
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.CR();
			for(i = 0;i<16;i++)
				cmd[i]=(byte) (0xC0+i);
			BluetoothPrintDriver1.BT_Write(cmd);
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.CR();
			for(i = 0;i<16;i++)
				cmd[i]=(byte) (0xD0+i);
			BluetoothPrintDriver1.BT_Write(cmd);
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.CR();
			for(i = 0;i<16;i++)
				cmd[i]=(byte) (0xE0+i);
			BluetoothPrintDriver1.BT_Write(cmd);
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.CR();
			for(i = 0;i<16;i++)
				cmd[i]=(byte) (0xF0+i);
			BluetoothPrintDriver1.BT_Write(cmd);
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.CR();
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.CR();
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.CR();
			
			BluetoothPrintDriver1.SetFontEnlarge((byte) 0x10);
			BluetoothPrintDriver1.BT_Write("TEXT TEST:\r\n");
			BluetoothPrintDriver1.SetFontEnlarge((byte) 0x00);
			String tmpString = BloothPrinterActivity.this.getResources().getString(R.string.print_text_content);
			BluetoothPrintDriver1.BT_Write(tmpString);
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.CR();
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.CR();
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.CR();
			
			BluetoothPrintDriver1.SetFontEnlarge((byte) 0x10);
			BluetoothPrintDriver1.BT_Write("BARCODE TEST:\r\n");
			BluetoothPrintDriver1.SetFontEnlarge((byte) 0x00);
			BluetoothPrintDriver1.AddCodePrint(BluetoothPrintDriver1.CODE39, "123456");
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.CR();
			
			BluetoothPrintDriver1.SetFontEnlarge((byte) 0x10);
			BluetoothPrintDriver1.BT_Write("QRCODE TEST:\r\n");
			BluetoothPrintDriver1.SetFontEnlarge((byte) 0x00);
			BluetoothPrintDriver1.AddQRCodePrint();
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.CR();
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.CR();
			
			BluetoothPrintDriver1.SetFontEnlarge((byte) 0x10);
			BluetoothPrintDriver1.BT_Write("IMAGE TEST:\r\n");
			BluetoothPrintDriver1.SetFontEnlarge((byte) 0x00);
			BluetoothPrintDriver1.printImage();
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.CR();
			*/
		}
	};
	
}