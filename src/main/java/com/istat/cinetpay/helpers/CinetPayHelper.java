package com.istat.cinetpay.helpers;

import com.istat.cinetcore.cinetpay.sdk.Customer;
import com.istat.cinetcore.cinetpay.sdk.Purchase;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by istat on 09/01/17.
 */

public class CinetPayHelper {

    //    String reference = "", amount, currency = "CFA", description = "",
//            origin = "";;
//    String paymentMethod = PAYMENT_METHOD_UNKNOW;
//    String title;
//    public String metaData;
//    int type = TYPE_PAYMENT;
//    Customer user;

    public static Purchase parsePurchase(Object obj) throws IllegalAccessException {
        Purchase purchase = new Purchase();
        List<Field> fields = Toolkit.getAllFieldFields(obj.getClass(), true, false);
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(PurchaseReference.class)) {
                purchase.setReference(field.get(obj) + "");
            } else if (field.isAnnotationPresent(PurchaseAmount.class)) {
                purchase.setAmount(field.getInt(obj));
            } else if (field.isAnnotationPresent(PurchaseDescription.class)) {
                purchase.setDescription(field.get(obj) + "");
            } else if (field.isAnnotationPresent(PurchaseOrigin.class)) {
                purchase.setOrigin(field.get(obj) + "");
            } else if (field.isAnnotationPresent(PurchaseMethod.class)) {
                purchase.setPaymentMethod(field.get(obj) + "");
            } else if (field.isAnnotationPresent(PurchaseTitle.class)) {
                purchase.setTitle(field.get(obj) + "");
            } else if (field.isAnnotationPresent(PurchaseType.class)) {
                purchase.setType(field.getInt(obj));
            } else if (field.isAnnotationPresent(PurchaseCurrency.class)) {
                purchase.setCurrency(field.get(obj) + "");
            } else if (field.isAnnotationPresent(PurchaseMetaData.class)) {
                purchase.setMetaData(field.get(obj) + "");
            } else if (field.isAnnotationPresent(PurchaseCustomerPhone.class) || field.isAnnotationPresent(PurchaseCustomerToken.class)) {
                String phone = null;
                String token = null;
                if (field.isAnnotationPresent(PurchaseCustomerPhone.class)) {
                    phone = field.get(obj) + "";
                } else if (field.isAnnotationPresent(PurchaseCustomerToken.class)) {
                    token = field.get(obj) + "";
                }
                Customer customer = new Customer(phone, token);
                purchase.setCustomer(customer);
            }
        }
        return purchase;
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PurchaseReference {
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PurchaseAmount {
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PurchaseDescription {
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PurchaseOrigin {
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PurchaseMethod {
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PurchaseTitle {
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PurchaseType {
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PurchaseCurrency {
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PurchaseMetaData {
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PurchaseCustomerPhone {
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PurchaseCustomerToken {
    }
}
