package com.wemall.foundation.dao;

import com.wemall.core.base.GenericDAO;
import com.wemall.foundation.domain.Payment;
import org.springframework.stereotype.Repository;

@Repository("paymentDAO")
public class PaymentDAO extends GenericDAO<Payment> {
}
