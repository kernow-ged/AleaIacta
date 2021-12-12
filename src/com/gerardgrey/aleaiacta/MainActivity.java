package com.gerardgrey.aleaiacta;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends Activity {
	
	private ValueCallback<Uri> mUploadMessage;
	public ValueCallback<Uri[]> uploadMessage;
	public static final int REQUEST_SELECT_FILE = 100;
	private final static int FILECHOOSER_RESULTCODE = 1;
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
	    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
	    {
	        if (requestCode == REQUEST_SELECT_FILE)
	        {
	            if (uploadMessage == null)
	                return;
	            uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
	            uploadMessage = null;
	        }
	    }
	    else if (requestCode == FILECHOOSER_RESULTCODE)
	    {
	        if (null == mUploadMessage)
	            return;
	    // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
	    // Use RESULT_OK only if you're implementing WebView inside an Activity
	        Uri result = intent == null || resultCode != MainActivity.RESULT_OK ? null : intent.getData();
	        mUploadMessage.onReceiveValue(result);
	        mUploadMessage = null;
	    }
	    else
	        Toast.makeText(MainActivity.this.getApplicationContext(), "Failed to Upload Image", Toast.LENGTH_LONG).show();
	}
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        WebView wv;  
        wv = (WebView) findViewById(R.id.webview);
        wv.getSettings().setUseWideViewPort(false);
        wv.getSettings().setDomStorageEnabled(true);
        wv.getSettings().setDatabaseEnabled(true);
        wv.getSettings().setAppCacheEnabled(true);
        wv.setBackgroundColor(0xFF000000);
        //wv.getSettings().setDisplayZoomControls(false);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setLoadWithOverviewMode(false);
        wv.getSettings().setSupportZoom(false);
        wv.getSettings().setBuiltInZoomControls(false);
        wv.getSettings().setAllowFileAccess(true);
        wv.getSettings().setAllowContentAccess(true);
        wv.setPadding(0, 0, 0, 0);
        wv.setOnTouchListener(null);
        wv.setOnClickListener(null);
        final Context myApp = this;       
        wv.setWebChromeClient(new WebChromeClient() {  
            @Override  
            public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result)   
            {  
                new AlertDialog.Builder(myApp)  
                    .setTitle("AleaIacta")  
                    .setMessage(message)  
                    .setPositiveButton(android.R.string.ok,  
                            new AlertDialog.OnClickListener()   
                            {  
                                public void onClick(DialogInterface dialog, int which)   
                                {  
                                    result.confirm();  
                                }  
                            })  
                    .setCancelable(false)  
                    .create()  
                    .show();  
                  
                return true;  
            }; 
            public boolean onJsPrompt(WebView view,
                    java.lang.String url,
                    java.lang.String message,
                    java.lang.String defaultValue,                   
                    final android.webkit.JsPromptResult result){  
            	AlertDialog.Builder al = new AlertDialog.Builder(myApp);
				final EditText in = new EditText(myApp);
				in.setText(" ",TextView.BufferType.EDITABLE);  			
				al.setTitle("AleaIacta");
				al.setMessage(message);
				al.setView(in);
				al.setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        result.confirm(in.getText().toString());
                    }
                })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        result.cancel();
                    }
                });
                al.create();
                al.show(); 				
            	return true;
            };         
            
        });          
        
        wv.loadUrl("file:///android_asset/AleaIacta.html");      
        
    }
}

