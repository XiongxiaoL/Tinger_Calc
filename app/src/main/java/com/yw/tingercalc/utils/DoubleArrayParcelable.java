package com.yw.tingercalc.utils;
import android.os.Parcel;
import android.os.Parcelable;

public class DoubleArrayParcelable implements Parcelable {
    private double[][] data;

    public DoubleArrayParcelable(double[][] data) {
        this.data = data;
    }

    public double[][] getData() {
        return data;
    }

    protected DoubleArrayParcelable(Parcel in) {
        // 从 Parcel 中读取数据并恢复到对象的属性
        data = new double[in.readInt()][];
        for (int i = 0; i < data.length; i++) {
            data[i] = in.createDoubleArray();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // 将对象的属性写入 Parcel
        dest.writeInt(data.length);
        for (double[] row : data) {
            dest.writeDoubleArray(row);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DoubleArrayParcelable> CREATOR = new Creator<DoubleArrayParcelable>() {
        @Override
        public DoubleArrayParcelable createFromParcel(Parcel in) {
            return new DoubleArrayParcelable(in);
        }

        @Override
        public DoubleArrayParcelable[] newArray(int size) {
            return new DoubleArrayParcelable[size];
        }
    };
}
