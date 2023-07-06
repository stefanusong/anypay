package com.stefanusong.anypay.factories;

import com.stefanusong.anypay.enums.PaymentMethod;
import com.stefanusong.anypay.gateway.IPaymentGateway;
import com.stefanusong.anypay.gateway.midtrans.MidtransGateway;
import com.stefanusong.anypay.gateway.midtrans.channels.MidtransCreditCard;
import com.stefanusong.anypay.gateway.midtrans.channels.MidtransEWallet;
import com.stefanusong.anypay.gateway.midtrans.channels.MidtransVirtualAccount;
import com.stefanusong.anypay.gateway.xendit.XenditGateway;
import com.stefanusong.anypay.gateway.xendit.channels.XenditEWallet;
import com.stefanusong.anypay.gateway.xendit.channels.XenditVirtualAccount;

public class GatewayFactory {
    public static IPaymentGateway GetGatewayByMethod(PaymentMethod method) {
        switch(method) {
            case GOPAY, SHOPEEPAY:
                return new MidtransGateway(new MidtransEWallet());
            case OVO, DANA, LINKAJA:
                return new XenditGateway(new XenditEWallet());
            case BCA_VA, BNI_VA, BRI_VA:
                return new MidtransGateway(new MidtransVirtualAccount());
            case MANDIRI_VA, CIMB_VA, PERMATA_VA:
                return new XenditGateway(new XenditVirtualAccount());
            case CREDIT_CARD:
                return new MidtransGateway(new MidtransCreditCard());
            default:
                return null;
        }
    }
}
