package com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.Travel.And.Tourisum.models.payment;

@Repository
public class paymentimpl {
    private final JdbcTemplate jdbcTemplate;
    public paymentimpl(final JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }
    public class PaymentRowMapper implements RowMapper<payment> {
        @Override
        public payment mapRow(ResultSet rs, int rowNum) throws SQLException {
            payment payment = new payment();
            payment.setBid(rs.getInt("bid"));
            payment.setUsername(rs.getString("username"));
            payment.setStatus(rs.getString("status"));
            payment.setMode_of_payment(rs.getString("mode_of_payment"));
            payment.setPayment_date(rs.getDate("payment_date").toLocalDate());
            payment.setPaymentid(rs.getString("paymentid"));
            payment.setHid(rs.getInt("hid"));
            payment.setRoomID(rs.getInt("RoomID"));
            payment.setOperatorId(rs.getLong("operatorId"));
            return payment;
        }
    }
    public void addPayment(payment payment) {
        String sql = "INSERT INTO payments (bid, username, status, mode_of_payment, payment_date, paymentid, hid, RoomID, operatorId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
        try {
            jdbcTemplate.update(sql, 
                payment.getBid(),
                payment.getUsername(),
                payment.getStatus(),
                payment.getMode_of_payment(),
                payment.getPayment_date(),
                payment.getPaymentid(),
                payment.getHid(),
                payment.getRoomID(),
                payment.getOperatorId());
        } catch (Exception e) {
            System.out.println("An error occurred while adding payment: ");
            // Optionally log the stack trace
            e.printStackTrace();
        }
    }
    

}
