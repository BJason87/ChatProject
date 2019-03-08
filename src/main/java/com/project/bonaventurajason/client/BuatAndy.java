package com.project.bonaventurajason.client;

public class BuatAndy {
    private final int a;
    private final int b;

    public BuatAndy(int a, int b){
        this.a = a;
        this.b = b;
    }

    public int perhitunganTambah(int a,int b){
        int c = a + b;
        return c;
    }

    public static void main(String[] args) {
        BuatAndy tes = new BuatAndy(2,5);
        System.out.println(tes);
    }
}
