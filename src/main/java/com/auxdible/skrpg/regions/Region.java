package com.auxdible.skrpg.regions;

import com.auxdible.skrpg.utils.Text;

public class Region {
    private double x;
    private double z;
    private double x2;
    private double z2;
    private String name;
    private int id;
    public Region(double x, double z, double x2, double z2, String name, int id) {
        this.x = x;
        this.z = z;
        this.x2 = x2;
        this.z2 = z2;
        this.name = name;
        this.id = id;
    }
    public double getX() { return x; }
    public double getZ() { return z; }
    public double getX2() { return x2; }
    public double getZ2() { return z2; }
    public String getName() { return Text.color(name); }
    public int getID() { return id; }
}
