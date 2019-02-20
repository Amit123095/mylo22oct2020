
package com.mindyourlovedone.healthcare.HomeActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.mindyourlovedone.healthcare.util.IabHelper;
import com.mindyourlovedone.healthcare.util.IabResult;
import com.mindyourlovedone.healthcare.util.Inventory;
import com.mindyourlovedone.healthcare.util.Purchase;
import com.mindyourlovedone.healthcare.utility.PrefConstants;
import com.mindyourlovedone.healthcare.utility.Preferences;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;
import ss.com.bannerslider.views.BannerSlider;

public class SplashNewActivity extends AppCompatActivity implements View.OnClickListener {
    static final String TAG = "TrivialDrive";
    //static final String SKU_INFINITE_GAS = "app_subscription"; // $1
    static final String SKU_INFINITE_GAS = "subscribe_app";   //$4.99
    static final int RC_REQUEST = 10001;
    private static final int REQUEST_CALL_PERMISSION = 100;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.
    Context context = this;
    TextView txtNew, txtRegistered, textMessage, txtWelcome, txtSubscribe, txtSubscribes;
    Preferences preferences;
    ImageView img1, img2, img3, img4, imgForword;
    RelativeLayout llBottom, llSubscribe;
    LinearLayout llSplash;
    BannerSlider bannerSlider;
    ViewPager viewPagerLogin;
    RelativeLayout rlBottom;
    int currentPage = 0;
    Timer timer;
    boolean mSubscribedToInfiniteGas = false;
    IabHelper mHelper;
    public static boolean fromDash = false;//nikita

    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {


            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            // Do we have the infinite gas plan?
            Purchase infiniteGasPurchase = inventory.getPurchase(SKU_INFINITE_GAS);
            mSubscribedToInfiniteGas = (infiniteGasPurchase != null &&
                    verifyDeveloperPayload(infiniteGasPurchase));
            Log.d(TAG, "User " + (mSubscribedToInfiniteGas ? "HAS" : "DOES NOT HAVE")
                    + " app subscription.");


            if (mSubscribedToInfiniteGas == true) {

                //   Log.d(TAG,  ""+infiniteGasPurchase.getPurchaseTime());
             /*   long purchasetime=infiniteGasPurchase.getPurchaseTime();
                String time= String.valueOf(purchasetime);
                switch (time)
                {
                    case "5,184,000,000":
                        //60 Days
                        break;
                    case "2,592,000,000":
                        //30 Days
                        break;
                    case "1,296,000,000":
                        //15 Days
                        break;
                    case "864,000,000":
                        //10 Days
                        break;
                    case "432,000,000":
                        //5 Days
                        break;
                    case "259,200,000":
                        //3 Days
                        break;
                    case "172,800,000":
                        //2 Days
                        break;

                    case "86,400,000":
                        //1 day
                        break;
                }
*/
                llSubscribe.setVisibility(View.GONE);
                if (preferences == null) {
                    preferences = new Preferences(SplashNewActivity.this);
                }
                if (preferences.getREGISTERED()) {
                    llBottom.setVisibility(View.VISIBLE);
                    llSplash.setVisibility(View.GONE);
                } else {
                    llSplash.setVisibility(View.VISIBLE);
                    llBottom.setVisibility(View.GONE);
                }
            } else {
                // if (infiniteGasPurchase.getPurchaseTime()!=null)
                // Log.d(TAG,  ""+infiniteGasPurchase.getPurchaseTime());
                //  Toast.makeText(getApplicationContext(),"Please Subscribe for Continue",Toast.LENGTH_SHORT).show();
               //Varsha Add 20feb
                llSubscribe.setVisibility(View.GONE);
                if (preferences == null) {
                    preferences = new Preferences(SplashNewActivity.this);
                }
                if (preferences.getREGISTERED()) {
                    llBottom.setVisibility(View.VISIBLE);
                    llSplash.setVisibility(View.GONE);
                } else {
                    llSplash.setVisibility(View.VISIBLE);
                    llBottom.setVisibility(View.GONE);
                }
               //Varsha
             /*   llSubscribe.setVisibility(View.VISIBLE);
                llBottom.setVisibility(View.GONE);
                llSplash.setVisibility(View.GONE);
                if (preferences == null) {
                    preferences = new Preferences(SplashNewActivity.this);
                }
                if (preferences.getSubscribed()) {
                    txtSubscribe.setText("Renew Your Subscription");
                } else {
                    txtSubscribe.setText("Subscribe To Continue");
                }

                onInfiniteGasButtonClicked();*/
                //Varsha
            }

            updateUi();
            setWaitScreen(false);
            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };

    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                if (!result.getMessage().contains("canceled")) {
                    complain(result.getMessage());
                }
                setWaitScreen(false);

                //Shradha
               /*
                llSubscribe.setVisibility(View.GONE);
                if (preferences == null) {
                    preferences = new Preferences(SplashNewActivity.this);
                }
                preferences.setSubscribed(true);
                if (preferences.getREGISTERED()) {
                    llBottom.setVisibility(View.VISIBLE);
                    llSplash.setVisibility(View.GONE);
                } else {
                    llSplash.setVisibility(View.VISIBLE);
                    llBottom.setVisibility(View.GONE);
                }*/

                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");
                setWaitScreen(false);
                return;
            }

            Log.d(TAG, "Purchase successful.");
            if (purchase.getSku().equals(SKU_INFINITE_GAS)) {
                // bought the infinite gas subscription
                Log.d(TAG, "Mylo app subscription purchased.");
                alert("Thank you for subscribing to Mylo app!");
                mSubscribedToInfiniteGas = true;
                //  mTank = TANK_MAX;
                updateUi();
                llSubscribe.setVisibility(View.GONE);
                if (preferences == null) {
                    preferences = new Preferences(SplashNewActivity.this);
                }
                preferences.setSubscribed(true);
                if (preferences.getREGISTERED()) {
                    llBottom.setVisibility(View.VISIBLE);
                    llSplash.setVisibility(View.GONE);
                } else {
                    llSplash.setVisibility(View.VISIBLE);
                    llBottom.setVisibility(View.GONE);
                }
                // updateUi();*g*
                setWaitScreen(false);
                //    Toast.makeText(getApplicationContext(),"Thanx and Welcome",Toast.LENGTH_SHORT).show();
            }
        }
    };
    private int[] layouts;
    //    ProgressDialog pd;
    private MyViewPagerAdapter myViewPagerAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_n);
        new AsynData().execute("");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (fromDash) {//nikita
            fromDash = false;
            new AsynData().execute("");
        }
    }


    public static long getDaysBetweenDates(Date startDate, Date endDate) {

        long numberOfDays = 0;
        try {
            numberOfDays = getUnitBetweenDates(startDate, endDate, TimeUnit.DAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return numberOfDays;
    }

    private static long getUnitBetweenDates(Date startDate, Date endDate, TimeUnit unit) {
        long timeDiff = endDate.getTime() - startDate.getTime();
        return unit.convert(timeDiff, TimeUnit.MILLISECONDS);
    }

    private void loadata() {//nikita
        // hashKey();
        //  accessPermission();
        variableInitialization();
        initUI();
        initListener();
        //  initViewPager();
        // initBanner();

        init();
//        inApp();//commented for 30 free days***************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************'


        // code below for 30 days -starts here-nikita
        preferences = new Preferences(SplashNewActivity.this);

        //Varsha Start
      /*  if (!preferences.getFirstTime()) {
            preferences.setFirstTime(true);
            Calendar c = Calendar.getInstance();
            c.setTime(c.getTime());
            c.add(Calendar.DATE, 30);
            String expDate = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(c.getTime());
            preferences.putString(PrefConstants.EXPIRY_DATE, expDate.toString());
        }

        String date = preferences.getString(PrefConstants.EXPIRY_DATE);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss"); // here set the pattern as you date in string was containing like date/month/year
            Date d = sdf.parse(date);
            Date current = Calendar.getInstance().getTime();

            if (current.after(d)) {
                final AlertDialog.Builder bld = new AlertDialog.Builder(this);
                bld.setCancelable(false);
                bld.setMessage("Your evaluation period is over.\nPlease get MYLO from the app store. ");
                bld.setNeutralButton("Install", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                        finish();
                    }
                });
                bld.create().show();

//                Toast.makeText(this, "30 days over", Toast.LENGTH_SHORT).show();
//                inApp();
                // Your time expired do your logic here.
            } else {
                if (getDaysBetweenDates(current, d) < 8) {
                    final AlertDialog.Builder bld = new AlertDialog.Builder(this);
                    bld.setCancelable(false);
                    bld.setMessage("Your evaluation period ends in " + getDaysBetweenDates(current, d) + " days to end free trial!\nPlease get MYLO from the app store.");
                    bld.setPositiveButton("Go to MYLO Store", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            }
                            finish();
                        }
                    });
                    bld.setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            bld.create().cancel();
                        }
                    });
                    bld.create().show();

                    if (preferences.getREGISTERED()) {
                        llBottom.setVisibility(View.VISIBLE);
                        llSplash.setVisibility(View.GONE);
                    } else {
                        llSplash.setVisibility(View.VISIBLE);
                        llBottom.setVisibility(View.GONE);
                    }

                } else {
                    if (preferences.getREGISTERED()) {
                        llBottom.setVisibility(View.VISIBLE);
                        llSplash.setVisibility(View.GONE);
                    } else {
                        llSplash.setVisibility(View.VISIBLE);
                        llBottom.setVisibility(View.GONE);
                    }
                    alert("Welcome to MYLO app. This is a 30 day evaluation version of the app.");
                }
//                Toast.makeText(this, "Using free version", Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
            // handle parsing exception if date string was different from the pattern applying into the SimpleDateFormat contructor
        }
        */
//Varsha End
        // code for 30 days -ends here-nikita

       /* img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              inApp();
            }
        });
*/
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)           // Enables Crashlytics debugger
                .build();
        Fabric.with(fabric);


    }

/*
    private void initViewPager() {
        viewPagerLogin = findViewById(R.id.viewPagerLogin);
        tabLayout = findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(viewPagerLogin, true);
        layouts = new int[]{
                R.layout.slide1,
                R.layout.slide5
        };
        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPagerLogin.setAdapter(myViewPagerAdapter);
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == 5) {
                    currentPage = 0;
                }
                viewPagerLogin.setCurrentItem(currentPage++, true);
            }
        };
        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }
*/

    /* private void initBanner() {
         List<Banner> banners = new ArrayList<>();
 //        banners.add(new RemoteBanner("Put banner image url here ..."));
         banners.add(new DrawableBanner((R.drawable.mye_img)).setScaleType(ImageView.ScaleType.FIT_XY));
         banners.add(new DrawableBanner((R.drawable.s_three)).setScaleType(ImageView.ScaleType.FIT_XY));
         banners.add(new DrawableBanner((R.drawable.s_four)).setScaleType(ImageView.ScaleType.FIT_XY));
         banners.add(new DrawableBanner((R.drawable.s_two)).setScaleType(ImageView.ScaleType.FIT_XY));
         banners.add(new DrawableBanner((R.drawable.three_women)).setScaleType(ImageView.ScaleType.FIT_XY));
         banners.add(new DrawableBanner((R.drawable.officeman)).setScaleType(ImageView.ScaleType.FIT_XY));
         bannerSlider.setBanners(banners);
     }
 */
    private void inApp() {
        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq3i1ShkUzBAWxerhJne2R7KYwWVXyERXLxz7Co0kW9wS45C55XnM/kFHNZ0hI62Oz8HWbTO+RisBMQ5If21sHu5DgXLHa+LNYj+2ZPQWlh46jo/jhMgo+V9YJ7EeOLedH70fFRlhy9OT2ZmOWscxN5YJDp22RXvilale2WcoKVOriS+I9fNbeREDcKM4CsB0isJyDEVIagaRaa0Za8MleOVeYUdma5q3ENZDJ8g9W2Dy0h6fioCZ9OIgBCY63qr0jVxHUwD8Jebp91czKWRSRi433suBmSkoE6qkhwtDEdckeG+cx6xErHcoPSrwhaLlvqCC1KngYduRZy5j1jCAywIDAQAB"; //"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAt/vQGFXEB+fQ7s5JbO/teKHjmvkZgqSeLSXmicYu4jDC5mBqfZ1/wBES/lhPGEfJAmjmSSQ1Z35XIcoTL74KVASTrUComknH4XiGaiXCjeCe9cFwYCXlWT+B3Y+dkRajRTi9G/iIgUZP6NTyblmKd5KcUn64CQIqgIZ8pD/4GsIR5abUFTEH9XXQEKzFjcdaBKB4uK1m2JLZ+w+FTFeNydzqSYdRL5lY4IHr8RHZwA3BReNMpzPt1Zp7URSkAGjXvbpOkURupUP+hB4VBYQYPfHfx3K4m32XKWl8zP0qwHS2kIIAjAEekzN+l+bDAU9fXdkDKuHIeXA0HLC6i9jRkQIDAQAB";

        // Some sanity checks to see if the developer (that's you!) really followed the
        // instructions to run this sample (don't put these checks on your app!)
        if (base64EncodedPublicKey.contains("CONSTRUCT_YOUR")) {
            throw new RuntimeException("Please put your app's public key in MainActivity.java. See README.");
        }
        if (getPackageName().startsWith("com.example")) {
            throw new RuntimeException("Please change the sample's package name! See README.");
        }

        // Create the helper, passing it our context and the public key to verify signatures with
        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(true);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });
    }

    private void hashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.mindyourlovedone.healthcare.HomeActivity",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("VKey:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private void accessPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(new String[]{android.Manifest.permission.CALL_PHONE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
            }, REQUEST_CALL_PERMISSION);

        } else {
            // checkForRegistration();
        }
    }

    private void init() {

    }

    private void variableInitialization() {
        preferences = new Preferences(this);
        //for dashboard webservice call
        preferences.setFirstTimeCall(true);
    }

    private void initListener() {
        txtNew.setOnClickListener(this);
        txtRegistered.setOnClickListener(this);
        txtWelcome.setOnClickListener(this);
        imgForword.setOnClickListener(this);
//        llSubscribe.setOnClickListener(this);
//        txtSubscribe.setOnClickListener(this);
//        rlBottom.setOnClickListener(this);

        llSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onInfiniteGasButtonClicked();
            }
        });

        llBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onInfiniteGasButtonClicked();
            }
        });
    }

    private void initUI() {

//        String s = getResources().getString(R.string.MergedAppname);
//        TextView textlogo = findViewById(R.id.txtHeaders);
//        textlogo.setText(Html.fromHtml(s));

//        textMessage = findViewById(R.id.textMessage);
        txtNew = findViewById(R.id.txtNew);
        txtRegistered = findViewById(R.id.txtRegistered);
        txtSubscribe = findViewById(R.id.txtSubscribe);
        txtWelcome = findViewById(R.id.txtWelcome);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        imgForword = findViewById(R.id.imgForword);
        llBottom = findViewById(R.id.llBottom);
        llSplash = findViewById(R.id.llSplash);
        llSubscribe = findViewById(R.id.llSubscribe);
        rlBottom = findViewById(R.id.rlBottom);
        // bannerSlider = findViewById(R.id.bannerSlider);

        final String[] array = {getResources().getString(R.string.msgstay), getResources().getString(R.string.msg), "Manage and Share Critical Information on\n" +
                "your Smartphone or Tablet", "Access to Critical Information and Advance\n" +
                "Care Directives 24/7", "The Just In Case App"};
/*
        textMessage.post(new Runnable() {
            int i = 0;

            @Override
            public void run() {

                textMessage.setText(array[i]);
                Animation RightSwipe = AnimationUtils.loadAnimation(context, R.anim.enter);
                textMessage.startAnimation(RightSwipe);
                i++;
                if (i == 5)
                    i = 0;
                textMessage.postDelayed(this, 5000);
            }
        });
*/

        if (preferences == null) {
            preferences = new Preferences(SplashNewActivity.this);
        }
        if (preferences.getREGISTERED()) {

            if (preferences.isLogin()) {
                llBottom.setVisibility(View.VISIBLE);
                llSplash.setVisibility(View.GONE);
                //Shradha
                txtWelcome.setText("Welcome Back " + preferences.getString(PrefConstants.USER_NAME));
            } else {
                llBottom.setVisibility(View.GONE);
                llSplash.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        /*    case R.id.txtSubscribes:
                onInfiniteGasButtonClicked();
                break;*/
            case R.id.rlBottom:
                onInfiniteGasButtonClicked();
                break;
            case R.id.llSubscribe:
                onInfiniteGasButtonClicked();
                break;

            case R.id.txtNew:
                Intent intent = new Intent(context, SignUpActivity.class);
                startActivity(intent);
                break;
            case R.id.txtRegistered:
                if (preferences == null) {
                    preferences = new Preferences(SplashNewActivity.this);
                }

                if (preferences.getREGISTERED()) {
                    Intent intent1 = new Intent(SplashNewActivity.this, BaseActivity.class);
                    startActivity(intent1);

//                    finish();
                } else {
                    Intent intent2 = new Intent(SplashNewActivity.this, LoginActivity.class);
                    startActivity(intent2);
//                    finish();
                }

                break;

            case R.id.txtWelcome:
                if (preferences == null) {
                    preferences = new Preferences(SplashNewActivity.this);
                }

                if (preferences.getREGISTERED()) {
                    startActivity(new Intent(SplashNewActivity.this, BaseActivity.class));
//                    finish();
                } else {
                    startActivity(new Intent(SplashNewActivity.this, LoginActivity.class));
//                    finish();
                }
                break;
            case R.id.imgForword:
                if (preferences == null) {
                    preferences = new Preferences(SplashNewActivity.this);
                }

                if (preferences.getREGISTERED()) {
                    startActivity(new Intent(SplashNewActivity.this, BaseActivity.class));
//                    finish();
                } else {
                    startActivity(new Intent(SplashNewActivity.this, LoginActivity.class));
//                    finish();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CALL_PERMISSION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //  checkForRegistration();

                } else {

                    accessPermission();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'switch' lines to check for other
            // permissions this app might request
        }
    }

    public void onInfiniteGasButtonClicked() {
        if (!mHelper.subscriptionsSupported()) {
            complain("Subscriptions not supported on your device yet. Sorry!");
            return;
        }

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";

        setWaitScreen(true);
        Log.d(TAG, "Launching purchase flow for Mylo subscription.");
        mHelper.launchPurchaseFlow(this,
                SKU_INFINITE_GAS, IabHelper.ITEM_TYPE_SUBS,
                RC_REQUEST, mPurchaseFinishedListener, payload);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) return;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    /**
     * Verifies the developer payload of a purchase.
     */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();
        return true;
    }

    // We're being destroyed. It's important to dispose of the helper here!
    @Override
    public void onDestroy() {
        super.onDestroy();

        // very important:
        Log.d(TAG, "Destroying helper.");
        if (mHelper != null) {
            mHelper.dispose();
            mHelper = null;
        }
    }

    // updates UI to reflect model
    public void updateUi() {
        // "Get infinite gas" button is only visible if the user is not subscribed yet
     /*   findViewById(R.id.infinite_gas_button).setVisibility(mSubscribedToInfiniteGas ?
                View.GONE : View.VISIBLE);

        // update gas gauge to reflect tank status
        if (mSubscribedToInfiniteGas) {
            ((ImageView)findViewById(R.id.gas_gauge)).setImageResource(R.drawable.gas_inf);
        }
        else {
            //  int index = mTank >= TANK_RES_IDS.length ? TANK_RES_IDS.length - 1 : mTank;
            //  ((ImageView)findViewById(R.id.gas_gauge)).setImageResource(TANK_RES_IDS[index]);
        }*/
    }

    // Enables or disable   s the "please wait" screen.
    void setWaitScreen(boolean set) {
       /* findViewById(R.id.screen_main).setVisibility(set ? View.GONE : View.VISIBLE);
        findViewById(R.id.screen_wait).setVisibility(set ? View.VISIBLE : View.GONE);*/
    }

    void complain(String message) {
        Log.e(TAG, "Error: " + message);
        alert(message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    private class AsynData extends AsyncTask {//nikita

        @Override
        protected Object doInBackground(Object[] objects) {
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            loadata();
            super.onPostExecute(o);
        }
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

}

/*
package com.mindyourlovedone.healthcare.HomeActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.mindyourlovedone.healthcare.util.IabHelper;
import com.mindyourlovedone.healthcare.util.IabResult;
import com.mindyourlovedone.healthcare.util.Inventory;
import com.mindyourlovedone.healthcare.util.Purchase;
import com.mindyourlovedone.healthcare.utility.PrefConstants;
import com.mindyourlovedone.healthcare.utility.Preferences;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;

import io.fabric.sdk.android.Fabric;
import ss.com.bannerslider.views.BannerSlider;

public class SplashNewActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txtNew,txtRegistered;
    Context context = this;
    Preferences preferences;
    public static boolean fromDash = false;
    //shradha-comment for new changes
   */
/* static final String TAG = "TrivialDrive";
    //static final String SKU_INFINITE_GAS = "app_subscription"; // $1
    static final String SKU_INFINITE_GAS = "subscribe_app";   //$4.99
    static final int RC_REQUEST = 10001;
    private static final int REQUEST_CALL_PERMISSION = 100;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.
    Context context = this;
    TextView txtNew, txtRegistered, textMessage, txtWelcome, txtSubscribe, txtSubscribes;

    ImageView img1, img2, img3, img4, imgForword;
    RelativeLayout llBottom, llSubscribe;
    LinearLayout llSplash;
    BannerSlider bannerSlider;
    ViewPager viewPagerLogin;
    RelativeLayout rlBottom;
    int currentPage = 0;
    Timer timer;
    boolean mSubscribedToInfiniteGas = false;
    IabHelper mHelper;
   //nikita

    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {


            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            // Do we have the infinite gas plan?
            Purchase infiniteGasPurchase = inventory.getPurchase(SKU_INFINITE_GAS);
            mSubscribedToInfiniteGas = (infiniteGasPurchase != null &&
                    verifyDeveloperPayload(infiniteGasPurchase));
            Log.d(TAG, "User " + (mSubscribedToInfiniteGas ? "HAS" : "DOES NOT HAVE")
                    + " app subscription.");


            if (mSubscribedToInfiniteGas == true) {

                //   Log.d(TAG,  ""+infiniteGasPurchase.getPurchaseTime());
             *//*
*/
/*   long purchasetime=infiniteGasPurchase.getPurchaseTime();
                String time= String.valueOf(purchasetime);
                switch (time)
                {
                    case "5,184,000,000":
                        //60 Days
                        break;
                    case "2,592,000,000":
                        //30 Days
                        break;
                    case "1,296,000,000":
                        //15 Days
                        break;
                    case "864,000,000":
                        //10 Days
                        break;
                    case "432,000,000":
                        //5 Days
                        break;
                    case "259,200,000":
                        //3 Days
                        break;
                    case "172,800,000":
                        //2 Days
                        break;

                    case "86,400,000":
                        //1 day
                        break;
                }
*//*
*/
/*
                llSubscribe.setVisibility(View.GONE);
                if (preferences == null) {
                    preferences = new Preferences(SplashNewActivity.this);
                }
                if (preferences.getREGISTERED()) {
                    llBottom.setVisibility(View.VISIBLE);
                    llSplash.setVisibility(View.GONE);
                } else {
                    llSplash.setVisibility(View.VISIBLE);
                    llBottom.setVisibility(View.GONE);
                }
            } else {
                // if (infiniteGasPurchase.getPurchaseTime()!=null)
                // Log.d(TAG,  ""+infiniteGasPurchase.getPurchaseTime());
                //  Toast.makeText(getApplicationContext(),"Please Subscribe for Continue",Toast.LENGTH_SHORT).show();
                llSubscribe.setVisibility(View.VISIBLE);
                llBottom.setVisibility(View.GONE);
                llSplash.setVisibility(View.GONE);
                if (preferences == null) {
                    preferences = new Preferences(SplashNewActivity.this);
                }
                if (preferences.getSubscribed()) {
                    txtSubscribe.setText("Renew Your Subscription");
                } else {
                    txtSubscribe.setText("Subscribe To Continue");
                }

               onInfiniteGasButtonClicked();
            }

            updateUi();
            setWaitScreen(false);
            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };

    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                if (!result.getMessage().contains("canceled")) {
                    complain(result.getMessage());
                }
                setWaitScreen(false);

                //Shradha
               *//*
*/
/*
                llSubscribe.setVisibility(View.GONE);
                if (preferences == null) {
                    preferences = new Preferences(SplashNewActivity.this);
                }
                preferences.setSubscribed(true);
                if (preferences.getREGISTERED()) {
                    llBottom.setVisibility(View.VISIBLE);
                    llSplash.setVisibility(View.GONE);
                } else {
                    llSplash.setVisibility(View.VISIBLE);
                    llBottom.setVisibility(View.GONE);
                }*//*
*/
/*

                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");
                setWaitScreen(false);
                return;
            }

            Log.d(TAG, "Purchase successful.");
            if (purchase.getSku().equals(SKU_INFINITE_GAS)) {
                // bought the infinite gas subscription
                Log.d(TAG, "Mylo app subscription purchased.");
                alert("Thank you for subscribing to Mylo app!");
                mSubscribedToInfiniteGas = true;
                //  mTank = TANK_MAX;
                updateUi();
                llSubscribe.setVisibility(View.GONE);
                if (preferences == null) {
                    preferences = new Preferences(SplashNewActivity.this);
                }
                preferences.setSubscribed(true);
                if (preferences.getREGISTERED()) {
                    llBottom.setVisibility(View.VISIBLE);
                    llSplash.setVisibility(View.GONE);
                } else {
                    llSplash.setVisibility(View.VISIBLE);
                    llBottom.setVisibility(View.GONE);
                }
               // updateUi();*g*
                setWaitScreen(false);
                //    Toast.makeText(getApplicationContext(),"Thanx and Welcome",Toast.LENGTH_SHORT).show();
            }
        }
    };
    private int[] layouts;
    //    ProgressDialog pd;
    private MyViewPagerAdapter myViewPagerAdapter;
    private TabLayout tabLayout;*//*


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_n);
        initUi();
        initListener();
        //shradha-comment for new changes
        //  new AsynData().execute("");
    }

    private void initListener() {
        txtNew.setOnClickListener(this);
        txtRegistered.setOnClickListener(this);
    }

    private void initUi() {
        txtNew = findViewById(R.id.txtNew);
        txtRegistered = findViewById(R.id.txtRegistered);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtNew:
                Intent intent = new Intent(context, SignUpActivity.class);
                startActivity(intent);
                break;

            case R.id.txtRegistered:
                if (preferences == null) {
                    preferences = new Preferences(SplashNewActivity.this);
                }

                if (preferences.getREGISTERED()) {
                    startActivity(new Intent(SplashNewActivity.this, BaseActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashNewActivity.this, LoginActivity.class));
                    finish();

                    //Toast.makeText(context,"Please Signin first you have not signed up yet.!!",Toast.LENGTH_SHORT).show();

                    // Intent intent2 = new Intent(SplashNewActivity.this, LoginActivity.class);
                   // startActivity(intent2);
//                    finish();
                }

                break;
        }
    }


//shradha-comment for new changes
 */
/*   @Override
    protected void onResume() {
        super.onResume();

        if (fromDash) {//nikita
            fromDash = false;
            new AsynData().execute("");
        }
    }

    private void loadata() {//nikita
        // hashKey();
        //  accessPermission();
        variableInitialization();
        initUI();
        initListener();
        //  initViewPager();
        // initBanner();

        init();
        inApp();
       *//*
*/
/* img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              inApp();
            }
        });
*//*
*/
/*
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)           // Enables Crashlytics debugger
                .build();
        Fabric.with(fabric);


    }

*//*
*/
/*
    private void initViewPager() {
        viewPagerLogin = findViewById(R.id.viewPagerLogin);
        tabLayout = findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(viewPagerLogin, true);
        layouts = new int[]{
                R.layout.slide1,
                R.layout.slide5
        };
        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPagerLogin.setAdapter(myViewPagerAdapter);
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == 5) {
                    currentPage = 0;
                }
                viewPagerLogin.setCurrentItem(currentPage++, true);
            }
        };
        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }
*//*
*/
/*

     *//*
*/
/* private void initBanner() {
         List<Banner> banners = new ArrayList<>();
 //        banners.add(new RemoteBanner("Put banner image url here ..."));
         banners.add(new DrawableBanner((R.drawable.mye_img)).setScaleType(ImageView.ScaleType.FIT_XY));
         banners.add(new DrawableBanner((R.drawable.s_three)).setScaleType(ImageView.ScaleType.FIT_XY));
         banners.add(new DrawableBanner((R.drawable.s_four)).setScaleType(ImageView.ScaleType.FIT_XY));
         banners.add(new DrawableBanner((R.drawable.s_two)).setScaleType(ImageView.ScaleType.FIT_XY));
         banners.add(new DrawableBanner((R.drawable.three_women)).setScaleType(ImageView.ScaleType.FIT_XY));
         banners.add(new DrawableBanner((R.drawable.officeman)).setScaleType(ImageView.ScaleType.FIT_XY));
         bannerSlider.setBanners(banners);
     }
 *//*
*/
/*
    private void inApp() {
        String base64EncodedPublicKey ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq3i1ShkUzBAWxerhJne2R7KYwWVXyERXLxz7Co0kW9wS45C55XnM/kFHNZ0hI62Oz8HWbTO+RisBMQ5If21sHu5DgXLHa+LNYj+2ZPQWlh46jo/jhMgo+V9YJ7EeOLedH70fFRlhy9OT2ZmOWscxN5YJDp22RXvilale2WcoKVOriS+I9fNbeREDcKM4CsB0isJyDEVIagaRaa0Za8MleOVeYUdma5q3ENZDJ8g9W2Dy0h6fioCZ9OIgBCY63qr0jVxHUwD8Jebp91czKWRSRi433suBmSkoE6qkhwtDEdckeG+cx6xErHcoPSrwhaLlvqCC1KngYduRZy5j1jCAywIDAQAB"; //"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAt/vQGFXEB+fQ7s5JbO/teKHjmvkZgqSeLSXmicYu4jDC5mBqfZ1/wBES/lhPGEfJAmjmSSQ1Z35XIcoTL74KVASTrUComknH4XiGaiXCjeCe9cFwYCXlWT+B3Y+dkRajRTi9G/iIgUZP6NTyblmKd5KcUn64CQIqgIZ8pD/4GsIR5abUFTEH9XXQEKzFjcdaBKB4uK1m2JLZ+w+FTFeNydzqSYdRL5lY4IHr8RHZwA3BReNMpzPt1Zp7URSkAGjXvbpOkURupUP+hB4VBYQYPfHfx3K4m32XKWl8zP0qwHS2kIIAjAEekzN+l+bDAU9fXdkDKuHIeXA0HLC6i9jRkQIDAQAB";

        // Some sanity checks to see if the developer (that's you!) really followed the
        // instructions to run this sample (don't put these checks on your app!)
        if (base64EncodedPublicKey.contains("CONSTRUCT_YOUR")) {
            throw new RuntimeException("Please put your app's public key in MainActivity.java. See README.");
        }
        if (getPackageName().startsWith("com.example")) {
            throw new RuntimeException("Please change the sample's package name! See README.");
        }

        // Create the helper, passing it our context and the public key to verify signatures with
        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(true);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });
    }

    private void hashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.mindyourlovedone.healthcare.HomeActivity",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("VKey:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private void accessPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ) {
            requestPermissions(new String[]{android.Manifest.permission.CALL_PHONE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
            }, REQUEST_CALL_PERMISSION);

        } else {
            // checkForRegistration();
        }
    }

    private void init() {

    }

    private void variableInitialization() {
        preferences = new Preferences(this);
        //for dashboard webservice call
        preferences.setFirstTimeCall(true);
    }

    private void initListener() {
        txtNew.setOnClickListener(this);
        txtRegistered.setOnClickListener(this);
        txtWelcome.setOnClickListener(this);
        imgForword.setOnClickListener(this);
//        llSubscribe.setOnClickListener(this);
//        txtSubscribe.setOnClickListener(this);
//        rlBottom.setOnClickListener(this);

        llSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onInfiniteGasButtonClicked();
            }
        });

        llBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onInfiniteGasButtonClicked();
            }
        });
    }

    private void initUI() {

//        String s = getResources().getString(R.string.MergedAppname);
//        TextView textlogo = findViewById(R.id.txtHeaders);
//        textlogo.setText(Html.fromHtml(s));

//        textMessage = findViewById(R.id.textMessage);
        txtNew = findViewById(R.id.txtNew);
        txtRegistered = findViewById(R.id.txtRegistered);
        txtSubscribe = findViewById(R.id.txtSubscribe);
        txtWelcome = findViewById(R.id.txtWelcome);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        imgForword = findViewById(R.id.imgForword);
        llBottom = findViewById(R.id.llBottom);
        llSplash = findViewById(R.id.llSplash);
        llSubscribe = findViewById(R.id.llSubscribe);
        rlBottom = findViewById(R.id.rlBottom);
        // bannerSlider = findViewById(R.id.bannerSlider);

        final String[] array = {getResources().getString(R.string.msgstay), getResources().getString(R.string.msg), "Manage and Share Critical Information on\n" +
                "your Smartphone or Tablet", "Access to Critical Information and Advance\n" +
                "Care Directives 24/7", "The Just In Case App"};
*//*
*/
/*
        textMessage.post(new Runnable() {
            int i = 0;

            @Override
            public void run() {

                textMessage.setText(array[i]);
                Animation RightSwipe = AnimationUtils.loadAnimation(context, R.anim.enter);
                textMessage.startAnimation(RightSwipe);
                i++;
                if (i == 5)
                    i = 0;
                textMessage.postDelayed(this, 5000);
            }
        });
*//*
*/
/*

        if (preferences == null) {
            preferences = new Preferences(SplashNewActivity.this);
        }
        if (preferences.getREGISTERED()) {

            if (preferences.isLogin()) {
                llBottom.setVisibility(View.VISIBLE);
                llSplash.setVisibility(View.GONE);
                //Shradha
                txtWelcome.setText("Welcome Back " + preferences.getString(PrefConstants.USER_NAME));
            } else {
                llBottom.setVisibility(View.GONE);
                llSplash.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        *//*
*/
/*    case R.id.txtSubscribes:
                onInfiniteGasButtonClicked();
                break;*//*
*/
/*
            case R.id.rlBottom:
                onInfiniteGasButtonClicked();
                break;
            case R.id.llSubscribe:
                onInfiniteGasButtonClicked();
                break;

            case R.id.txtNew:
                Intent intent = new Intent(context, SignUpActivity.class);
                startActivity(intent);
                break;
            case R.id.txtRegistered:
                if (preferences == null) {
                    preferences = new Preferences(SplashNewActivity.this);
                }

                if (preferences.getREGISTERED()) {
                    Intent intent1 = new Intent(SplashNewActivity.this, BaseActivity.class);
                    startActivity(intent1);

//                    finish();
                } else {
                    Intent intent2 = new Intent(SplashNewActivity.this, LoginActivity.class);
                    startActivity(intent2);
//                    finish();
                }

                break;

            case R.id.txtWelcome:
                if (preferences == null) {
                    preferences = new Preferences(SplashNewActivity.this);
                }

                if (preferences.getREGISTERED()) {
                    startActivity(new Intent(SplashNewActivity.this, BaseActivity.class));
//                    finish();
                } else {
                    startActivity(new Intent(SplashNewActivity.this, LoginActivity.class));
//                    finish();
                }
                break;
            case R.id.imgForword:
                if (preferences == null) {
                    preferences = new Preferences(SplashNewActivity.this);
                }

                if (preferences.getREGISTERED()) {
                    startActivity(new Intent(SplashNewActivity.this, BaseActivity.class));
//                    finish();
                } else {
                    startActivity(new Intent(SplashNewActivity.this, LoginActivity.class));
//                    finish();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CALL_PERMISSION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //  checkForRegistration();

                } else {

                    accessPermission();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'switch' lines to check for other
            // permissions this app might request
        }
    }

    public void onInfiniteGasButtonClicked() {
        if (!mHelper.subscriptionsSupported()) {
            complain("Subscriptions not supported on your device yet. Sorry!");
            return;
        }

        *//*
*/
/* TODO: for security, generate your payload here for verification. See the comments on
     *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
     *        an empty string, but on a production app you should carefully generate this. *//*
*/
/*
        String payload = "";

        setWaitScreen(true);
        Log.d(TAG, "Launching purchase flow for Mylo subscription.");
        mHelper.launchPurchaseFlow(this,
                SKU_INFINITE_GAS, IabHelper.ITEM_TYPE_SUBS,
                RC_REQUEST, mPurchaseFinishedListener, payload);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) return;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    *//*
*/
/**
     * Verifies the developer payload of a purchase.
     *//*
*/
/*
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();
        return true;
    }

    // We're being destroyed. It's important to dispose of the helper here!
    @Override
    public void onDestroy() {
        super.onDestroy();

        // very important:
        Log.d(TAG, "Destroying helper.");
        if (mHelper != null) {
            mHelper.dispose();
            mHelper = null;
        }
    }

    // updates UI to reflect model
    public void updateUi() {
        // "Get infinite gas" button is only visible if the user is not subscribed yet
     *//*
*/
/*   findViewById(R.id.infinite_gas_button).setVisibility(mSubscribedToInfiniteGas ?
                View.GONE : View.VISIBLE);

        // update gas gauge to reflect tank status
        if (mSubscribedToInfiniteGas) {
            ((ImageView)findViewById(R.id.gas_gauge)).setImageResource(R.drawable.gas_inf);
        }
        else {
            //  int index = mTank >= TANK_RES_IDS.length ? TANK_RES_IDS.length - 1 : mTank;
            //  ((ImageView)findViewById(R.id.gas_gauge)).setImageResource(TANK_RES_IDS[index]);
        }*//*
*/
/*
    }

    // Enables or disable   s the "please wait" screen.
    void setWaitScreen(boolean set) {
       *//*
*/
/* findViewById(R.id.screen_main).setVisibility(set ? View.GONE : View.VISIBLE);
        findViewById(R.id.screen_wait).setVisibility(set ? View.VISIBLE : View.GONE);*//*
*/
/*
    }

    void complain(String message) {
        Log.e(TAG, "Error: " + message);
        alert(message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    private class AsynData extends AsyncTask {//nikita

        @Override
        protected Object doInBackground(Object[] objects) {
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            loadata();
            super.onPostExecute(o);
        }
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }*//*


}
*/
