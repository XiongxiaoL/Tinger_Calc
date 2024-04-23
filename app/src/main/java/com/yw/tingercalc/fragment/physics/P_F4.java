package com.yw.tingercalc.fragment.physics;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yw.tingercalc.R;



public class P_F4 extends Fragment implements View.OnClickListener{

    EditText et1;
    TextView res;
    private String te;
    Button calculate;

    private static final int DIM = 1024;
    private static final int DM1 = DIM - 1;
    private static int[][][] a = new int[1024][1024][3];
    private static int[][] pd = new int[1024][1024];
    private static double[] b1 = new double[3];
    private static double[] b2 = new double[3];
    private static double[] b3 = new double[3];
    private static double[] b4 = new double[3];
    private static double[][] c1 = new double[1024][3];
    private static double[][] c2 = new double[1024][3];
    private static int k1, k2, k3, k4, t;
    private static double[] x = new double[1000];
    private static double[] y = new double[1000];
    private static double[] z = new double[1000];
    private static int i, j, k, b, rrr, ggg, bbb;
    private static double bz, mi, rr, gg, bb, m, xx, yy, zz;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.p_f4,null);
        initView(view);
        setOnClickListener();

        if (te != null){
            et1.setText(te);
        }

        return view;
    }

    private void initView(View view) {
        et1 = view.findViewById(R.id.et_1);
        res = view.findViewById(R.id.res);
        calculate = view.findViewById(R.id.calculate);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.calculate:
                count();
                break;
        }
    }

    private void setOnClickListener() {
        calculate.setOnClickListener(this);
    }

    private void count() {
        te = et1.getText().toString();
        int wavelength = Integer.parseInt(te); // 波长（单位：纳米）
        if (wavelength > 780 || wavelength < 380) {
            Toast.makeText(getActivity(), "波长范围有误", Toast.LENGTH_SHORT).show();
        } else {
            String colorHex = wavelengthToHexColor(wavelength);
            System.out.println(colorHex + "|||||");
            res.setBackgroundColor(Color.parseColor(colorHex));
        }
    }


    private static String wavelengthToHexColor(int b) {
        a[0][0][0] = 0;
        a[0][0][1] = 0;
        a[0][0][2] = 0;

        k = b % 5;

        if (k >= 3)
            b = b + 5 - k;
        else
            b = b - k;

        x[380]=0.1741;y[380]=0.0050;z[380]=0.8209;
        x[385]=0.1740;y[385]=0.0050;z[385]=0.8210;
        x[390]=0.1738;y[390]=0.0049;z[390]=0.8213;
        x[395]=0.1736;y[395]=0.0049;z[395]=0.8215;
        x[400]=0.1733;y[400]=0.0048;z[400]=0.8219;
        x[405]=0.1730;y[405]=0.0048;z[405]=0.8222;
        x[410]=0.1726;y[410]=0.0048;z[410]=0.8226;
        x[415]=0.1721;y[415]=0.0048;z[415]=0.8231;
        x[420]=0.1714;y[420]=0.0051;z[420]=0.8235;
        x[425]=0.1703;y[425]=0.0058;z[425]=0.8239;
        x[430]=0.1689;y[430]=0.0069;z[430]=0.8242;
        x[435]=0.1669;y[435]=0.0086;z[435]=0.8245;
        x[440]=0.1644;y[440]=0.0109;z[440]=0.8247;
        x[445]=0.1611;y[445]=0.0138;z[445]=0.8251;
        x[450]=0.1566;y[450]=0.0177;z[450]=0.8257;
        x[455]=0.1510;y[455]=0.0227;z[455]=0.8263;
        x[460]=0.1440;y[460]=0.0297;z[460]=0.8263;
        x[465]=0.1355;y[465]=0.0399;z[465]=0.8246;
        x[470]=0.1241;y[470]=0.0578;z[470]=0.8181;
        x[475]=0.1096;y[475]=0.0868;z[475]=0.8036;
        x[480]=0.0913;y[480]=0.1327;z[480]=0.7760;
        x[485]=0.0687;y[485]=0.2007;z[485]=0.7306;
        x[490]=0.0454;y[490]=0.2950;z[490]=0.6596;
        x[495]=0.0235;y[495]=0.4127;z[495]=0.5638;
        x[500]=0.0082;y[500]=0.5384;z[500]=0.4534;
        x[505]=0.0039;y[505]=0.6548;z[505]=0.3413;
        x[510]=0.0139;y[510]=0.7502;z[510]=0.2359;
        x[515]=0.0389;y[515]=0.8120;z[515]=0.1491;
        x[520]=0.0743;y[520]=0.8338;z[520]=0.0919;
        x[525]=0.1142;y[525]=0.8262;z[525]=0.0596;
        x[530]=0.1547;y[530]=0.8059;z[530]=0.0394;
        x[535]=0.1929;y[535]=0.7816;z[535]=0.0255;
        x[540]=0.2296;y[540]=0.7543;z[540]=0.0161;
        x[545]=0.2658;y[545]=0.7243;z[545]=0.0099;
        x[550]=0.3016;y[550]=0.6923;z[550]=0.0061;
        x[555]=0.3373;y[555]=0.6589;z[555]=0.0038;
        x[560]=0.3731;y[560]=0.6245;z[560]=0.0024;
        x[565]=0.4087;y[565]=0.5896;z[565]=0.0017;
        x[570]=0.4441;y[570]=0.5547;z[570]=0.0012;
        x[575]=0.4788;y[575]=0.5202;z[575]=0.0010;
        x[580]=0.5125;y[580]=0.4866;z[580]=0.0009;
        x[585]=0.5448;y[585]=0.4544;z[585]=0.0008;
        x[590]=0.5752;y[590]=0.4242;z[590]=0.0006;
        x[595]=0.6029;y[595]=0.3965;z[595]=0.0006;
        x[600]=0.6270;y[600]=0.3725;z[600]=0.0005;
        x[605]=0.6482;y[605]=0.3514;z[605]=0.0004;
        x[610]=0.6658;y[610]=0.3340;z[610]=0.0002;
        x[615]=0.6801;y[615]=0.3197;z[615]=0.0002;
        x[620]=0.6915;y[620]=0.3083;z[620]=0.0002;
        x[625]=0.7006;y[625]=0.2993;z[625]=0.0001;
        x[630]=0.7079;y[630]=0.2920;z[630]=0.0001;
        x[635]=0.7140;y[635]=0.2859;z[635]=0.0001;
        x[640]=0.7219;y[640]=0.2809;z[640]=0.0001;
        x[645]=0.7230;y[645]=0.2770;z[645]=0.0000;
        x[650]=0.7260;y[650]=0.2740;z[650]=0.0000;
        x[655]=0.7283;y[655]=0.2717;z[655]=0.0000;
        x[660]=0.7300;y[660]=0.2700;z[660]=0.0000;
        x[665]=0.7311;y[665]=0.2689;z[665]=0.0000;
        x[670]=0.7320;y[670]=0.2680;z[670]=0.0000;
        x[675]=0.7327;y[675]=0.2673;z[675]=0.0000;
        x[680]=0.7334;y[680]=0.2666;z[680]=0.0000;
        x[685]=0.7340;y[685]=0.2660;z[685]=0.0000;
        x[690]=0.7344;y[690]=0.2656;z[690]=0.0000;
        x[695]=0.7346;y[695]=0.2654;z[695]=0.0000;
        x[700]=0.7347;y[700]=0.2653;z[700]=0.0000;
        x[705]=0.7347;y[705]=0.2653;z[705]=0.0000;
        x[710]=0.7347;y[710]=0.2653;z[710]=0.0000;
        x[715]=0.7347;y[715]=0.2653;z[715]=0.0000;
        x[720]=0.7347;y[720]=0.2653;z[720]=0.0000;
        x[725]=0.7347;y[725]=0.2653;z[725]=0.0000;
        x[730]=0.7347;y[730]=0.2653;z[730]=0.0000;
        x[735]=0.7347;y[735]=0.2653;z[735]=0.0000;
        x[740]=0.7347;y[740]=0.2653;z[740]=0.0000;
        x[745]=0.7347;y[745]=0.2653;z[745]=0.0000;
        x[750]=0.7347;y[750]=0.2653;z[750]=0.0000;
        x[755]=0.7347;y[755]=0.2653;z[755]=0.0000;
        x[760]=0.7347;y[760]=0.2653;z[760]=0.0000;
        x[765]=0.7347;y[765]=0.2653;z[765]=0.0000;
        x[770]=0.7347;y[770]=0.2653;z[770]=0.0000;
        x[775]=0.7347;y[775]=0.2653;z[775]=0.0000;
        x[780]=0.7347;y[780]=0.2653;z[780]=0.0000;
        xx=x[b];yy=y[b];zz=z[b];mi=32768;

        for (i = 0; i <= 255; i++) {
            for (j = 0; j <= 255; j++) {
                for (k = 0; k <= 255; k++) {
                    if (i + k + j > 0) {
                        rr = 0.490 * i + 0.310 * j + 0.200 * k;
                        rr = rr / (0.667 * i + 1.132 * j + 1.200 * k);

                        gg = 0.117 * i + 0.812 * j + 0.010 * k;
                        gg = gg / (0.667 * i + 1.132 * j + 1.200 * k);

                        bb = 0.000 * i + 0.010 * j + 0.990 * k;
                        bb = bb / (0.667 * i + 1.132 * j + 1.200 * k);

                        m = (rr - xx) * (rr - xx) + (gg - yy) * (gg - yy) + (bb - zz) * (bb - zz);

                        if (m < mi) {
                            mi = m;
                            rrr = i;
                            ggg = j;
                            bbb = k;
                        }
                    }
                }
            }
        }
        String rr, gg ,bb;
        rr = Integer.toHexString(rrr);
        gg = Integer.toHexString(ggg);
        bb = Integer.toHexString(bbb);

        if (rr.length() == 1) {
            rr = "0" + rr;
        }
        if (gg.length() == 1) {
            gg = "0" + gg;
        }
        if (bb.length() == 1) {
            bb = "0" + bb;
        }
        return "#" + rr + gg + bb;

    }

}
