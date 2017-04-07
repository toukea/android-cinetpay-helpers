package com.istat.cinetpay.helpers;

import istat.android.network.utils.Connectivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Handler;
import android.widget.Toast;

import com.istat.cinetcore.cinetpay.sdk.MerchantService;
import com.istat.cinetcore.cinetpay.sdk.PaymentResponse;
import com.istat.cinetcore.cinetpay.sdk.interfaces.Purchasable;
import com.istat.cinetcore.cinetpay.sdk.Purchase;
import com.istat.cinetcore.cinetpay.sdk.PurchaseTransaction;
import com.istat.cinetcore.cinetpay.sdk.plugins.SmsPayPlugin;
import com.istat.cinetcore.cinetpay.sdk.process.CinetPay.PayCallBack;
import com.istat.cinetcore.cinetpay.sdk.process.CinetPay.TransactionCheckCallBack;
import com.istat.cinetcore.cinetpay.sdk.uis.CinetPayUI;
import com.istat.cinetcore.cinetpay.sdk.uis.PayPlugin;

public abstract class CinetPayBilling implements PayCallBack,
        TransactionCheckCallBack, ActivityFocusWatcher.OnFocusChangeListener {
    final static int WAITING_TIME_BEFORE_SUCCESS = 500;
    protected MerchantService mMerchant;
    protected CinetPayUI ui;
    protected Handler mHandler;
    protected Activity mActivity;
    Purchasable currentPurchase;
    PaymentResponse currentPayResponse;
    ProgressDialog mProgressDialog;
    protected ActivityFocusWatcher activityFocusWatcher;

    public CinetPayBilling(Activity activity, MerchantService merchant) {
        mActivity = activity;
        mMerchant = merchant;
        ui = CinetPayUI.getInstance(activity, mMerchant);
        activityFocusWatcher = new ActivityFocusWatcher(activity);
        mProgressDialog = initProgressDialog();
        mHandler = new Handler();
    }

    protected ProgressDialog initProgressDialog() {
        ProgressDialog dialog = new ProgressDialog(mActivity);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public void purchase(Purchasable purchase) {
        purchase(purchase, true);
    }

    public void purchase(Purchasable purchase, boolean use_sms_if_needful) {
        currentPurchase = purchase;
        PayPlugin plugin = null;
        if (!Connectivity.isConnected(mActivity) && use_sms_if_needful) {
            plugin = new SmsPayPlugin(ui);
        }
        ui.beginPayment(plugin, purchase, this);
    }

    public Dialog showProgressDialog(String message) {
        // TODO Auto-generated method stub
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
        return mProgressDialog;
    }

    public Dialog cancelProgressDialog() {
        // TODO Auto-generated method stub
        mProgressDialog.cancel();
        return mProgressDialog;
    }

    public boolean isPurchasing() {
        return currentPayResponse != null;
    }

    @Override
    public void onCheckComplete(PurchaseTransaction t, boolean succes) {
        // TODO Auto-generated method stub
        cancelProgressDialog();
        if (succes) {
            currentPurchase = t.getPurchase();
            currentPayResponse = t.getPaymentStatus();
            if (currentPayResponse.isWaiting()) {
                activityFocusWatcher.stopWatching();
                currentPayResponse = null;
                onPaymentWaiting(currentPurchase);
            }
        } else {
            onPaymentVerificationFail(currentPurchase);
        }
    }

    @Override
    public void onPurchaseComplete(PaymentResponse r, Purchase p) {
        // TODO Auto-generated method stub
        activityFocusWatcher.startWatching(this);
        currentPurchase = p;
        currentPayResponse = r;
    }

    @Override
    public void onFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        if (hasFocus && isPurchasing()) {
            if (currentPayResponse.hasBeenConfirmed()) {
                onPaymentSucces(currentPurchase);
                currentPayResponse = null;
                activityFocusWatcher.stopWatching();
            } else if (currentPayResponse.isWaiting()) {
                if (onVerifyPayment(currentPurchase)) {
                    verifyPurchase(currentPurchase);
                } else {
                    activityFocusWatcher.stopWatching();
                    onPaymentWaiting(currentPurchase);
                }
            } else {
                onPaymentFail(currentPurchase);
                currentPayResponse = null;
                activityFocusWatcher.stopWatching();
            }
        }
    }

    public void verifyPayment(String reference) {
        showProgressDialog("V�rification de votre paiement en cours.\nPatientez svp...");
        if (!activityFocusWatcher.isAlive()) {
            activityFocusWatcher = new ActivityFocusWatcher(mActivity);
            activityFocusWatcher.startWatching(this);
        }
        ui.getCinetPayInstance().doAsyncTransactionCheck(this, reference);
    }

    private void verifyPurchase(Purchasable purchase) {
        verifyPayment(purchase.getReference());
    }

    protected abstract void onPaymentSucces(Purchasable purchase);

    protected abstract void onPaymentWaiting(Purchasable purchase);

    protected abstract void onPaymentFail(Purchasable purchase);

    protected abstract boolean onVerifyPayment(Purchasable purchase);

    protected abstract void onPaymentVerificationFail(Purchasable purchase);

    protected void toast(String text) {
        Toast.makeText(mActivity, text, Toast.LENGTH_LONG).show();
    }
}
