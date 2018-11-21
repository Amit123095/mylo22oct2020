package com.mindyourlovedones.healthcare.DashBoard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.mindyourlovedones.healthcare.HomeActivity.BaseActivity;
import com.mindyourlovedones.healthcare.HomeActivity.R;
import com.mindyourlovedones.healthcare.database.DBHelper;
import com.mindyourlovedones.healthcare.database.LivingQuery;
import com.mindyourlovedones.healthcare.model.Living;
import com.mindyourlovedones.healthcare.pdfCreation.EventPdf;
import com.mindyourlovedones.healthcare.pdfCreation.MessageString;
import com.mindyourlovedones.healthcare.pdfCreation.PDFDocumentProcess;
import com.mindyourlovedones.healthcare.pdfdesign.Header;
import com.mindyourlovedones.healthcare.utility.PrefConstants;
import com.mindyourlovedones.healthcare.utility.Preferences;

import java.io.File;
import java.util.ArrayList;

public class LivingActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    final CharSequence[] dialog_items = {"View", "Email", "User Instructions"};
    Context context = this;
    View rootview;
    RelativeLayout rlLiving;
    ImageView imgBack, imgDone, imgRight, imgInfo;
    Preferences preferences;
    TextView txtTitle, txtName, txtSave;
    DBHelper dbHelper;
    ImageView imgInfoF, imgInfoI;
    EditText etOtherFunction, etFunctionalNote, etOtherInstrument, etInstrumentalNote;
    ToggleButton tbAlert, tbComputer, tbRemote, tbFinances, tbPreparing, tbShopping, tbUsing, tbBathing, tbContinence, tbDressing, tbfeed, tbToileting, tbTranfering, tbTransport, tbPets, tbDriving, tbKeeping, tbMedication;
    String alert = "NO", computer = "NO", remote = "NO", eating = "NO", finance = "NO", prepare = "NO", shop = "NO", use = "NO", bath = "NO", continence = "NO", dress = "NO", feed = "NO", toileting = "NO", transfer = "NO", transport = "NO", pets = "NO", drive = "NO", keep = "NO", medication = "NO";
    String functionnote = "", fouctionOther = "", instaOther = "", instaNote = "";
    FloatingActionButton floatProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_living);
        preferences = new Preferences(context);
        initComponent();
        initUI();
        initListener();
    }

    private void initListener() {
        txtSave.setOnClickListener(this);
        floatProfile.setOnClickListener(this);
        imgDone.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        imgInfoF.setOnClickListener(this);
        imgInfoI.setOnClickListener(this);
        imgRight.setOnClickListener(this);
        tbFinances.setOnCheckedChangeListener(this);
        tbPreparing.setOnCheckedChangeListener(this);
        tbShopping.setOnCheckedChangeListener(this);

        tbRemote.setOnCheckedChangeListener(this);
        tbComputer.setOnCheckedChangeListener(this);
        tbAlert.setOnCheckedChangeListener(this);

        tbUsing.setOnCheckedChangeListener(this);
        tbBathing.setOnCheckedChangeListener(this);
        tbContinence.setOnCheckedChangeListener(this);
        tbDressing.setOnCheckedChangeListener(this);
        tbfeed.setOnCheckedChangeListener(this);
        tbToileting.setOnCheckedChangeListener(this);
        tbTranfering.setOnCheckedChangeListener(this);
        tbTransport.setOnCheckedChangeListener(this);
        tbPets.setOnCheckedChangeListener(this);
        tbDriving.setOnCheckedChangeListener(this);
        tbKeeping.setOnCheckedChangeListener(this);
        tbMedication.setOnCheckedChangeListener(this);
    }

    private void initUI() {
        floatProfile = findViewById(R.id.floatProfile);
        imgInfo = findViewById(R.id.imgInfo);
        imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LivingActivity.this, InstructionActivity.class);
                i.putExtra("From", "Living");
                startActivity(i);
               /* final Dialog customDialog;
                customDialog = new Dialog(LivingActivity.this);
                customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customDialog.setContentView(R.layout.dialog_living);
                customDialog.setCancelable(false);
                TextView txtNotes= (TextView) customDialog.findViewById(R.id.txtNotes);
                String msg="To save information click the check mark" +
                        " on the top right side of the screen." +
                        "<br><br>" +
                        "To edit information simply change the data and then save your edits by clicking on the check mark on the top right side of the screen." +
                        "<br><br>" +
                        "To view, email, or fax the data in each section click on the three dots on the top right side of the screen.";

                txtNotes.setText(Html.fromHtml(msg));
                TextView txtNoteHeader= (TextView) customDialog.findViewById(R.id.txtNoteHeader);
                txtNoteHeader.setText("Help");
                TextView btnYes= (TextView) customDialog.findViewById(R.id.btnYes);
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                });
                customDialog.show();*/
            }
        });
        rlLiving = findViewById(R.id.rlLiving);
        txtName = findViewById(R.id.txtName);
        txtName.setText(preferences.getString(PrefConstants.CONNECTED_NAME));
        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setVisibility(View.VISIBLE);
        txtTitle.setText("ACTIVITIES OF DAILY\nLIVING");

        txtSave = findViewById(R.id.txtSave);
        txtSave.setVisibility(View.VISIBLE);

        imgBack = findViewById(R.id.imgBack);
        imgRight = findViewById(R.id.imgRight);
        imgDone = findViewById(R.id.imgDone);
        //imgDone.setVisibility(View.VISIBLE);

        imgInfoF = findViewById(R.id.imgInfoF);
        imgInfoI = findViewById(R.id.imgInfoI);

        etOtherFunction = findViewById(R.id.etOtherFunction);
        etFunctionalNote = findViewById(R.id.etFunctionalNote);
        etOtherInstrument = findViewById(R.id.etOtherInstrument);
        etInstrumentalNote = findViewById(R.id.etInstrumentalNote);

        tbFinances = findViewById(R.id.tbFinances);
        tbRemote = findViewById(R.id.tbRemote);
        tbComputer = findViewById(R.id.tbComputer);
        tbAlert = findViewById(R.id.tbAlert);

        tbPreparing = findViewById(R.id.tbPreparing);
        tbShopping = findViewById(R.id.tbShopping);
        tbUsing = findViewById(R.id.tbUsing);
        tbBathing = findViewById(R.id.tbBathing);
        tbContinence = findViewById(R.id.tbContinence);
        tbDressing = findViewById(R.id.tbDressing);
        tbfeed = findViewById(R.id.tbfeed);
        tbToileting = findViewById(R.id.tbToileting);
        tbTranfering = findViewById(R.id.tbTranfering);
        tbTransport = findViewById(R.id.tbTransport);
        tbPets = findViewById(R.id.tbPets);
        tbDriving = findViewById(R.id.tbDriving);
        tbKeeping = findViewById(R.id.tbKeeping);
        tbMedication = findViewById(R.id.tbMedication);

        rlLiving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
            }
        });

        setLivingInfo();
    }

    private void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void setLivingInfo() {
        Living medInfo = LivingQuery.fetchOneRecord(preferences.getInt(PrefConstants.CONNECTED_USERID));
        if (medInfo != null) {
            etFunctionalNote.setText(medInfo.getFunctionNote());
            etOtherFunction.setText(medInfo.getFunctionOther());
            etInstrumentalNote.setText(medInfo.getInstNote());
            etOtherInstrument.setText(medInfo.getInstOther());


            if (medInfo.getFinance().equals("YES")) {
                tbFinances.setChecked(true);
                finance = "YES";
            } else if (medInfo.getFinance().equals("NO")) {
                tbFinances.setChecked(false);
                finance = "NO";
            }

            if (medInfo.getAlert().equals("YES")) {
                tbAlert.setChecked(true);
                alert = "YES";
            } else if (medInfo.getAlert().equals("NO")) {
                tbAlert.setChecked(false);
                alert = "NO";
            }

            if (medInfo.getComputer().equals("YES")) {
                tbComputer.setChecked(true);
                computer = "YES";
            } else if (medInfo.getComputer().equals("NO")) {
                tbComputer.setChecked(false);
                computer = "NO";
            }

            if (medInfo.getRemote().equals("YES")) {
                tbRemote.setChecked(true);
                remote = "YES";
            } else if (medInfo.getRemote().equals("NO")) {
                tbRemote.setChecked(false);
                remote = "NO";
            }

            if (medInfo.getPrepare().equals("YES")) {
                tbPreparing.setChecked(true);
                prepare = "YES";
            } else if (medInfo.getPrepare().equals("NO")) {
                tbPreparing.setChecked(false);
                prepare = "NO";
            }

            if (medInfo.getShop().equals("YES")) {
                tbShopping.setChecked(true);
                shop = "YES";
            } else if (medInfo.getShop().equals("NO")) {
                tbShopping.setChecked(false);
                shop = "NO";
            }

            if (medInfo.getUse().equals("YES")) {
                tbUsing.setChecked(true);
                use = "YES";
            } else if (medInfo.getUse().equals("NO")) {
                tbUsing.setChecked(false);
                use = "NO";
            }
            if (medInfo.getBath().equals("YES")) {
                tbBathing.setChecked(true);
                bath = "YES";
            } else if (medInfo.getBath().equals("NO")) {
                tbBathing.setChecked(false);
                bath = "NO";
            }
            if (medInfo.getContinence().equals("YES")) {
                tbContinence.setChecked(true);
                continence = "YES";
            } else if (medInfo.getContinence().equals("NO")) {
                tbContinence.setChecked(false);
                continence = "NO";
            }

            if (medInfo.getDress().equals("YES")) {
                tbDressing.setChecked(true);
                dress = "YES";
            } else if (medInfo.getDress().equals("NO")) {
                tbDressing.setChecked(false);
                dress = "NO";
            }

            if (medInfo.getFeed().equals("YES")) {
                tbfeed.setChecked(true);
                feed = "YES";
            } else if (medInfo.getFeed().equals("NO")) {
                tbfeed.setChecked(false);
                feed = "NO";
            }

            if (medInfo.getToileting().equals("YES")) {
                tbToileting.setChecked(true);
                toileting = "YES";
            } else if (medInfo.getToileting().equals("NO")) {
                tbToileting.setChecked(false);
                toileting = "NO";
            }

            if (medInfo.getTransfer().equals("YES")) {
                tbTranfering.setChecked(true);
                transfer = "YES";
            } else if (medInfo.getTransfer().equals("NO")) {
                tbTranfering.setChecked(false);
                transfer = "NO";
            }

            if (medInfo.getTransport().equals("YES")) {
                tbTransport.setChecked(true);
                transport = "YES";
            } else if (medInfo.getTransport().equals("NO")) {
                tbTransport.setChecked(false);
                transport = "NO";
            }

            if (medInfo.getPets().equals("YES")) {
                tbPets.setChecked(true);
                pets = "YES";
            } else if (medInfo.getPets().equals("NO")) {
                tbPets.setChecked(false);
                pets = "NO";
            }

            if (medInfo.getDrive().equals("YES")) {
                tbDriving.setChecked(true);
                drive = "YES";
            } else if (medInfo.getDrive().equals("NO")) {
                tbDriving.setChecked(false);
                drive = "NO";
            }

            if (medInfo.getKeep().equals("YES")) {
                tbKeeping.setChecked(true);
                keep = "YES";
            } else if (medInfo.getKeep().equals("NO")) {
                tbKeeping.setChecked(false);
                keep = "NO";
            }

            if (medInfo.getMedication().equals("YES")) {
                tbMedication.setChecked(true);
                medication = "YES";
            } else if (medInfo.getMedication().equals("NO")) {
                tbMedication.setChecked(false);
                medication = "NO";
            }
        }
    }

    private void initComponent() {
        preferences = new Preferences(context);
        dbHelper = new DBHelper(context, preferences.getString(PrefConstants.CONNECTED_USERDB));
        LivingQuery p = new LivingQuery(context, dbHelper);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floatProfile:
                Intent intentDashboard = new Intent(context, BaseActivity.class);
                intentDashboard.putExtra("c", 1);//Profile Data
                intentDashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intentDashboard.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentDashboard);
                break;

            case R.id.imgInfoF:
                String msg = "<b>Bathing.</b> <i>The ability to clean oneself and perform grooming activities like shaving and brushing teeth.</i>  <br><br>" +
                        "<b>Dressing.</b> <i> The ability to get dressed by oneself without struggling with buttons and zippers.</i><br><br>" +
                        "<b>Eating.</b> <i> The ability to feed oneself.</i><br><br>" +
                        "<b>Transferring.</b> <i> Being able to either walk or move oneself from a bed to a wheelchair and back again.</i><br><br>" +
                        "<b>Toileting.</b> <i> The ability to get on and off the toilet.</i><br><br>" +
                        "<b>Continence.</b> <i> The ability to control one's bladder and bowel functions.</i>";
                String title = "Activities of Daily Living";
                showViewDialog(context, msg, title);
                break;
            case R.id.imgRight:

                final String RESULT = Environment.getExternalStorageDirectory()
                        + "/mylopdf/";
                File dirfile = new File(RESULT);
                dirfile.mkdirs();
                File file = new File(dirfile, "ActivityLiving.pdf");
                if (file.exists()) {
                    file.delete();
                }
                new Header().createPdfHeader(file.getAbsolutePath(),
                        "" + preferences.getString(PrefConstants.CONNECTED_NAME));
                preferences.copyFile("ic_launcher.png", context);
                Header.addImage("/sdcard/MYLO/images/" + "ic_launcher.png");
                Header.addEmptyLine(1);
                Header.addusereNameChank("Activities Of Daily Living");//preferences.getString(PrefConstants.CONNECTED_NAME));
                Header.addEmptyLine(1);

                Header.addChank("MindYour-LovedOnes.com");//preferences.getString(PrefConstants.CONNECTED_NAME));

                Paragraph p = new Paragraph(" ");
                LineSeparator line = new LineSeparator();
                line.setOffset(-4);
                line.setLineColor(BaseColor.LIGHT_GRAY);
                p.add(line);
                try {
                    Header.document.add(p);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                Header.addEmptyLine(1);
                /*new Header().createPdfHeader(file.getAbsolutePath(),
                        "Activities Of Daily Living");
                Header.addusereNameChank(preferences.getString(PrefConstants.CONNECTED_NAME));
                Header.addEmptyLine(2);*/

                Living Live = LivingQuery.fetchOneRecord(preferences.getInt(PrefConstants.CONNECTED_USERID));
                ArrayList<Living> LivingList = new ArrayList<Living>();
                LivingList.add(Live);
                new EventPdf(1, LivingList, 1);

                Header.document.close();


                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("");

                builder.setItems(dialog_items, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int itemPos) {
                        String path = Environment.getExternalStorageDirectory()
                                + "/mylopdf/"
                                + "/ActivityLiving.pdf";
                        switch (itemPos) {
                            case 0: //View
                                StringBuffer result = new StringBuffer();
                                result.append(new MessageString().getLivingInfo());
                                new PDFDocumentProcess(path,
                                        context, result);

                                System.out.println("\n" + result + "\n");
                                break;
                            case 1://Email
                                File f = new File(path);
                                preferences.emailAttachement(f, context, "Activities of Daily Living");
                                break;
                          /*  case 2://fax
                                new FaxCustomDialog(context, path).show();
                                break;*/

                            case 2://fax
                                Intent i = new Intent(context, InstructionActivity.class);
                                i.putExtra("From", "LivingInstruction");
                                startActivity(i);
                                break;
                        }
                    }

                });
                builder.create().show();
                break;
            case R.id.txtSave:
                functionnote = etFunctionalNote.getText().toString().trim();
                fouctionOther = etOtherFunction.getText().toString().trim();
                instaOther = etOtherInstrument.getText().toString().trim();
                instaNote = etInstrumentalNote.getText().toString().trim();
                Boolean flag = LivingQuery.insertLivingData(preferences.getInt(PrefConstants.CONNECTED_USERID), finance, prepare, shop, use, bath, continence, dress, feed, toileting, transfer, transport, pets, drive, keep, medication, functionnote, fouctionOther, instaNote, instaOther, remote, alert, computer);
                if (flag == true) {
                    Toast.makeText(context, "Activity Living Info Saved", Toast.LENGTH_SHORT).show();
                    hideSoftKeyboard();
                    finish();
                } else {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.imgBack:
                hideSoftKeyboard();
                finish();
                break;
        }
    }

    private void showViewDialog(Context context, String Message, String title) {
        final Dialog customDialog;

        // LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
        //  View customView = inflater.inflate(R.layout.dialog_input, null);
        // Build the dialog
        customDialog = new Dialog(context);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(R.layout.dialog_living);
        customDialog.setCancelable(false);
        TextView txtNotes = customDialog.findViewById(R.id.txtNotes);
        txtNotes.setText(Html.fromHtml(Message));
        TextView txtNoteHeader = customDialog.findViewById(R.id.txtNoteHeader);
        txtNoteHeader.setText(title);
        TextView btnYes = customDialog.findViewById(R.id.btnYes);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });
        customDialog.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.tbFinances:
                if (isChecked == true)
                    finance = "YES";
                else
                    finance = "NO";
                break;

            case R.id.tbRemote:
                if (isChecked == true)
                    remote = "YES";
                else
                    remote = "NO";
                break;

            case R.id.tbAlert:
                if (isChecked == true)
                    alert = "YES";
                else
                    alert = "NO";
                break;

            case R.id.tbComputer:
                if (isChecked == true)
                    computer = "YES";
                else
                    computer = "NO";
                break;

            case R.id.tbPreparing:
                if (isChecked == true)
                    prepare = "YES";
                else
                    prepare = "NO";
                break;

            case R.id.tbShopping:
                if (isChecked == true)
                    shop = "YES";
                else
                    shop = "NO";
                break;

            case R.id.tbUsing:
                if (isChecked == true)
                    use = "YES";
                else
                    use = "NO";
                break;

            case R.id.tbBathing:
                if (isChecked == true)
                    bath = "YES";
                else
                    bath = "NO";
                break;

            case R.id.tbContinence:
                if (isChecked == true)
                    continence = "YES";
                else
                    continence = "NO";
                break;

            case R.id.tbDressing:
                if (isChecked == true)
                    dress = "YES";
                else
                    dress = "NO";
                break;

            case R.id.tbfeed:
                if (isChecked == true)
                    feed = "YES";
                else
                    feed = "NO";
                break;

            case R.id.tbToileting:
                if (isChecked == true)
                    toileting = "YES";
                else
                    toileting = "NO";
                break;

            case R.id.tbTranfering:
                if (isChecked == true)
                    transfer = "YES";
                else
                    transfer = "NO";
                break;

            case R.id.tbTransport:
                if (isChecked == true)
                    transport = "YES";
                else
                    transport = "NO";
                break;

            case R.id.tbMedication:
                if (isChecked == true)
                    medication = "YES";
                else
                    medication = "NO";
                break;

            case R.id.tbKeeping:
                if (isChecked == true)
                    keep = "YES";
                else
                    keep = "NO";
                break;

            case R.id.tbDriving:
                if (isChecked == true)
                    drive = "YES";
                else
                    drive = "NO";
                break;

            case R.id.tbPets:
                if (isChecked == true)
                    pets = "YES";
                else
                    pets = "NO";
                break;

        }
    }
}
