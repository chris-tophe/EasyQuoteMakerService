package com.christophe.quoteService.models;


import java.text.DecimalFormat;

abstract class Items {
    private DecimalFormat cost;

    public DecimalFormat getCost() {
        return cost;
    }
}
