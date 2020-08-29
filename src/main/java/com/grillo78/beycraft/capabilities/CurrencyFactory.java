package com.grillo78.beycraft.capabilities;

import java.util.concurrent.Callable;

public class CurrencyFactory implements Callable<ICurrency>{

    @Override
    public ICurrency call() throws Exception {
        return new Currency();
    }

}