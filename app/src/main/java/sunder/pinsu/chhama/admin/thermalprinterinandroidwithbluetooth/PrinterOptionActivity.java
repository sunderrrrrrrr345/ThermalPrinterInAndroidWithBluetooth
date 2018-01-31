package sunder.pinsu.chhama.admin.thermalprinterinandroidwithbluetooth;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;




import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import sunder.pinsu.chhama.admin.thermalprinterinandroidwithbluetooth.util.Utils;

public class PrinterOptionActivity extends Activity{
	private Button mBtnBack = null;
	private Button mBtnPrintText = null;
	private Button mBtnPrintImage = null;
	private Button mBtnPrint1DBarcode = null;
	private Button mBtnPrintTicket = null;
	private Button mBtnPrintTable = null;
	private EditText m1DBarcodeContent = null;
//	private BloothPrinter mBloothPrinter = null;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.printer_option);
        InitUIControl();
    }



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
//		this.mBloothPrinter = BloothPrinterActivity.mBloothPrinter;
		super.onResume();
	}

	
	private String getLanguageEnv() {  
	       Locale l = Locale.getDefault();  
	       String language = l.getLanguage();  
	       String country = l.getCountry().toLowerCase();  
	       if ("zh".equals(language)) {  
	           if ("cn".equals(country)) {  
	               language = "zh-CN";  
	           } else if ("tw".equals(country)) {  
	               language = "zh-TW";  
	           }  
	       } else if ("pt".equals(language)) {  
	           if ("br".equals(country)) {  
	               language = "pt-BR";  
	           } else if ("pt".equals(country)) {  
	               language = "pt-PT";  
	           }  
	       }  
	       return language;  
	}  

	private void InitUIControl(){
    	mBtnBack = (Button)findViewById(R.id.btn_back);
    	mBtnBack.setOnClickListener(mBtnBackOnClickListener);
    	mBtnPrintText = (Button)findViewById(R.id.btn_print_text);
    	mBtnPrintText.setOnClickListener(mBtnPrintTextOnClickListener);
    	mBtnPrintImage = (Button)findViewById(R.id.btn_print_image);
    	mBtnPrintImage.setOnClickListener(mBtnPrintImageOnClickListener);
    	mBtnPrint1DBarcode = (Button)findViewById(R.id.btn_print_barcode);
    	mBtnPrint1DBarcode.setOnClickListener(mBtnPrint1DBarcodeOnClickListener);
    	mBtnPrintTicket = (Button)findViewById(R.id.btn_print_smallticket);
    	mBtnPrintTicket.setOnClickListener(mBtnPrintTicketOnClickListener);
    	mBtnPrintTable = (Button)findViewById(R.id.btn_print_table);
    	mBtnPrintTable.setOnClickListener(mBtnPrintTableOnClickListener);
    	m1DBarcodeContent = (EditText)findViewById(R.id.edt_barcode_content);
    }

	OnClickListener mBtnBackOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};

	OnClickListener mBtnPrintTextOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(BluetoothPrintDriver1.IsNoConnection()){
				return;
			}
			BluetoothPrintDriver1.Begin();
			String tmpString = PrinterOptionActivity.this.getResources().getString(R.string.print_text_content);
			BluetoothPrintDriver1.BT_Write(tmpString);
			BluetoothPrintDriver1.BT_Write("\r");
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.LF();
		}
	};

	OnClickListener mBtnPrintImageOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(BluetoothPrintDriver1.IsNoConnection()){
				return;
			}
			InputStream in = null;
			try {
				in = getResources().getAssets().open("Rongta.jpg");
			} catch (IOException e) {
				e.printStackTrace();
			}
			BluetoothPrintDriver1.Begin();
			BluetoothPrintDriver1.printImage();

			/*String print1DBarcodeStr = m1DBarcodeContent.getText().toString();
			BluetoothPrintDriver1.AddCodePrint(BluetoothPrintDriver1.Code128_B, print1DBarcodeStr);*/


		}
	};

	OnClickListener mBtnPrint1DBarcodeOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(BluetoothPrintDriver1.IsNoConnection()){
				return;
			}
			BluetoothPrintDriver1.Begin();
			String print1DBarcodeStr = m1DBarcodeContent.getText().toString();
			Log.i("Sunder",print1DBarcodeStr);
			int len = print1DBarcodeStr.length();
        	if(len > 16){
        		String tmpString = PrinterOptionActivity.this.getResources().getString(R.string.barcode_input_hint);
    			Utils.ShowMessage(PrinterOptionActivity.this, tmpString);
        		return;
        	}
//        	for(int i=0; i<len; i++){
//        		if(print1DBarcodeStr.charAt(i)<'0' || print1DBarcodeStr.charAt(i)>'9'){
//        			//Utils.ShowMessage(PrinterOptionActivity.this, "�����ַ�ֻ����0��9λ֮�������!");
//        			String tmpString = PrinterOptionActivity.this.getResources().getString(R.string.barcode_input_hint);
//        			Utils.ShowMessage(PrinterOptionActivity.this, tmpString);
//            		return;
//            	}
//        	}
        	//BluetoothPrintDriver1.AddCodePrint(BluetoothPrintDriver1.Code128_B, print1DBarcodeStr);
        	BluetoothPrintDriver1.AddCodePrint(BluetoothPrintDriver1.UPCA, print1DBarcodeStr);
			}
	};

	OnClickListener mBtnPrintTicketOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(BluetoothPrintDriver1.IsNoConnection()){
				return;
			}
			String tmpString1 = PrinterOptionActivity.this.getResources().getString(R.string.print_smallticket_content1);
			String tmpString2 = PrinterOptionActivity.this.getResources().getString(R.string.print_smallticket_content2);
			String tmpString3 = PrinterOptionActivity.this.getResources().getString(R.string.print_smallticket_content3);
			String tmpString4 = PrinterOptionActivity.this.getResources().getString(R.string.print_smallticket_content4);
			String tmpString5 = PrinterOptionActivity.this.getResources().getString(R.string.print_smallticket_content5);
			String tmpString6 = PrinterOptionActivity.this.getResources().getString(R.string.print_smallticket_content6);
			String tmpString7 = PrinterOptionActivity.this.getResources().getString(R.string.print_smallticket_content7);
			String tmpString8 = PrinterOptionActivity.this.getResources().getString(R.string.print_smallticket_content8);
			String tmpString9 = PrinterOptionActivity.this.getResources().getString(R.string.print_smallticket_content9);
			String tmpString10 = PrinterOptionActivity.this.getResources().getString(R.string.print_smallticket_content10);
			String tmpString11 = PrinterOptionActivity.this.getResources().getString(R.string.print_smallticket_content11);
			String tmpString12 = PrinterOptionActivity.this.getResources().getString(R.string.print_ticket_line1);
			String tmpString13 = PrinterOptionActivity.this.getResources().getString(R.string.print_ticket_line2);
			BluetoothPrintDriver1.Begin();
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.SetAlignMode((byte)1);//����
			BluetoothPrintDriver1.SetLineSpacing((byte)50);
			BluetoothPrintDriver1.SetFontEnlarge((byte)0x11);//���ߣ�����
			BluetoothPrintDriver1.BT_Write(tmpString1);
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.SetAlignMode((byte)0);//�����		
			BluetoothPrintDriver1.SetFontEnlarge((byte)0x00);//Ĭ�Ͽ�ȡ�Ĭ�ϸ߶�
			BluetoothPrintDriver1.BT_Write(tmpString2);
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.BT_Write(tmpString3);
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.BT_Write(tmpString4);
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.BT_Write(tmpString12);
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.BT_Write(tmpString5);
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.BT_Write(tmpString12);
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.BT_Write(tmpString6);
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.BT_Write(tmpString7);
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.BT_Write(tmpString12);
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.BT_Write(tmpString8);
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.SetFontEnlarge((byte)0x11);//���ߣ�����	
			BluetoothPrintDriver1.BT_Write(tmpString9);
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.BT_Write(tmpString13);
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.BT_Write(tmpString10);
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.SetFontEnlarge((byte)0x00);//Ĭ�Ͽ�ȡ�Ĭ�ϸ߶�	
			BluetoothPrintDriver1.BT_Write(tmpString11);
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.LF();
			BluetoothPrintDriver1.LF();
		}
	};

	OnClickListener mBtnPrintTableOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(BluetoothPrintDriver1.IsNoConnection()){
				return;
			}
			String tmpString1 = PrinterOptionActivity.this.getResources().getString(R.string.print_table_content1);
			String tmpString2 = PrinterOptionActivity.this.getResources().getString(R.string.print_table_content2);
			String tmpString3 = PrinterOptionActivity.this.getResources().getString(R.string.print_table_content3);
			String tmpString4 = PrinterOptionActivity.this.getResources().getString(R.string.print_table_content4);
			String tmpString5 = PrinterOptionActivity.this.getResources().getString(R.string.print_table_content5);
			String tmpString6 = PrinterOptionActivity.this.getResources().getString(R.string.print_table_content6);
			String tmpString7 = PrinterOptionActivity.this.getResources().getString(R.string.print_table_content7);
			String tmpString8 = PrinterOptionActivity.this.getResources().getString(R.string.print_table_content8);
			String tmpString9 = PrinterOptionActivity.this.getResources().getString(R.string.print_table_content9);
			
			
			String language = getLanguageEnv();//��ȡ��ǰϵͳ������
		    if (language != null  
		            && (language.trim().equals("zh-CN") || language.trim().equals("zh-TW")))  
		    {//����
		    	BluetoothPrintDriver1.Begin();
		    	BluetoothPrintDriver1.BT_Write(new byte[]{0x1d,0x21,0x01}, 3);	//���ñ���
		    	BluetoothPrintDriver1.BT_Write(String.format(tmpString1),true);
		    	BluetoothPrintDriver1.BT_Write(String.format(tmpString2),true);
		    	BluetoothPrintDriver1.BT_Write(new byte[]{0x1d,0x21,0x01},3);	//���ò�����
		    	BluetoothPrintDriver1.BT_Write(String.format(tmpString3),true);
		    	BluetoothPrintDriver1.BT_Write(new byte[]{0x1d,0x21,0x01}, 3);	//���ñ���
		    	BluetoothPrintDriver1.BT_Write(String.format(tmpString4),true);
		    	BluetoothPrintDriver1.BT_Write(new byte[]{0x1d,0x21,0x01},3);	//���ò�����
		    	BluetoothPrintDriver1.BT_Write(String.format(tmpString5), true);
		    	BluetoothPrintDriver1.BT_Write(new byte[]{0x1d,0x21,0x01},3);	//���ñ���
		    	BluetoothPrintDriver1.BT_Write(String.format(tmpString6),true);
		    	BluetoothPrintDriver1.BT_Write(new byte[]{0x1d,0x21,0x01},3);	//���ò�����
		    	BluetoothPrintDriver1.BT_Write(String.format(tmpString7),true);
		    	BluetoothPrintDriver1.BT_Write(new byte[]{0x1d,0x21,0x01},3);	//���ñ���
		    	BluetoothPrintDriver1.BT_Write(String.format(tmpString8),true);
		    	BluetoothPrintDriver1.BT_Write(new byte[]{0x1d,0x21,0x00},3);	//���ò�����
		    	BluetoothPrintDriver1.BT_Write(String.format(tmpString9),true);
		    	BluetoothPrintDriver1.LF();
		    	BluetoothPrintDriver1.LF();
		    }
		    else
		    {//������
		    	BluetoothPrintDriver1.Begin();
		    	BluetoothPrintDriver1.BT_Write(new byte[]{0x1d,0x21,0x01}, 3);	//���ñ���
				// ��һ��
		    	BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xDA},1);// ��
		    	BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4},6);// ��
		    	BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xC2},1);// ��
		    	BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4},8);// ��
		    	BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xC2},1);// ��
		    	BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xC4,(byte)0xC4},2);// ��
		    	BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xC2},1);// ��
		    	BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4},6);// ��
		    	BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xBF,0x0A},2);// ��
				// �ڶ���
		    	BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xB3},1);// ��
		    	BluetoothPrintDriver1.BT_Write(String.format("From  "),true);
		    	BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xB3},1);// ��
		    	BluetoothPrintDriver1.BT_Write(String.format("Shanghai"),true);
		    	BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xB3},1);// ��
		    	BluetoothPrintDriver1.BT_Write(String.format("To"),true);
		    	BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xB3},1);// ��
		    	BluetoothPrintDriver1.BT_Write(String.format("Xiamen"),true);
		    	BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xB3,0x0A},2);
				// ������
		    	BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xC3},1);// ��
				BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4},6);// ��
				BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xC5},1);// ��
				BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4},4);// ��
				BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xC2},1);// ��
				BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xC4,(byte)0xC4,(byte)0xC4},3);// ��
				BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xC1},1);// ��
				BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xC2},1);// ��
				BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xC4},1);// ��
				BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xC1},1);// ��
				BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4},6);// ��
				BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xB4,0x0A},2);// ��
				//������
				BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xB3},1);
				BluetoothPrintDriver1.BT_Write(String.format("Amount"),true);
				BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xB3},1);
				BluetoothPrintDriver1.BT_Write(String.format(" 1  "),true);
				BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xB3},1);
				BluetoothPrintDriver1.BT_Write(String.format("No. "),true);
				BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xB3},1);
				BluetoothPrintDriver1.BT_Write(String.format("5555555 "),true);
				BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xB3,0x0A},2);
				//������
				BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xC3},1);
				BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4},6);
				BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xC1},1);
				BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xC4,(byte)0xC4,(byte)0xC4},3);
				BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xC2},1);
				BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xC1},1);
				BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4},4);
		    	BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xC1},1);
		    	BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4},8);
		    	BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xB4,0x0A},2);	
				//������
		    	BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xB3},1);
		    	BluetoothPrintDriver1.BT_Write(String.format("Addressee "),true);
		    	BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xB3},1);
		    	BluetoothPrintDriver1.BT_Write(String.format("Sun Jun       "),true);
		    	BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xB3,0x0A},2);
				//������
		    	BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xC3},1);
		    	BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4},10);
		    	BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xC5},1);
		    	BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4},14);
		    	BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xB4,0x0A},2);    	
				//�ڰ���
		    	BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xB3},1);
				BluetoothPrintDriver1.BT_Write(String.format("Pickup by "),true);
				BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xB3},1);
				BluetoothPrintDriver1.BT_Write(String.format("              "),true);
				BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xB3,0x0A},2);
				//�ھ���
				BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xC0},1);
				BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4},10);
		    	BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xC1},1);
		    	BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4,(byte)0xC4},14);
		    	BluetoothPrintDriver1.BT_Write(new byte[]{(byte)0xD9,0x0A},2);  
				//
		    	BluetoothPrintDriver1.LF();
				BluetoothPrintDriver1.LF();
		    }
		}
	};
}
