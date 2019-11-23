package com.peterlaurence.book.javatokotlin.part1.fragments.java;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.peterlaurence.book.javatokotlin.part1.core.java.UserPurchases;

import java.util.List;

public class CallbackViewModel extends ViewModel {
    private BillingClient billingClient;
    private PurchasesProvider purchasesProvider;

    private MutableLiveData<UserPurchases> purchases;

    CallbackViewModel(BillingClient billingClient, PurchasesProvider purchasesProvider) {
        this.billingClient = billingClient;
        this.purchasesProvider = purchasesProvider;
    }

    void getUserPurchases(String user) {
        billingClient.init(ready -> {
            purchasesProvider.fetchPurchases(user, purchases -> {
                this.purchases.postValue(new UserPurchases(user, purchases));
            });
        });
    }

    LiveData<UserPurchases> getPurchasesLiveData(String user) {
        if (purchases == null) {
            purchases = new MutableLiveData<>();
            getUserPurchases(user);
        }
        return purchases;
    }

    public interface BillingClient {
        interface BillingCallback {
            void onInitDone(boolean ready);
        }

        void init(BillingCallback callback);
    }

    public interface PurchasesProvider {
        interface PurchaseFetchCallback {
            void onPurchaseFetchDone(List<String> purchases);
        }

        void fetchPurchases(String user, PurchaseFetchCallback callback);
    }
}