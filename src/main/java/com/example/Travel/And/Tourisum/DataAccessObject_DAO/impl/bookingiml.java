package com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl;
import java.sql.*;
import java.time.*;
import java.util.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.example.Travel.And.Tourisum.DataAccessObject_DAO.booking;
import com.example.Travel.And.Tourisum.models.bookings;



@Repository
public class bookingiml implements booking {
    public String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !authentication.getName().equals("anonymousUser")) {
            return authentication.getName(); // This returns the username
        }
        return null; // If no authenticated user or user is anonymous, return null
    }

    private final JdbcTemplate jdbcTemplate;
    // Make the constructor public
    public bookingiml(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public static class bookingMapper implements RowMapper<bookings> {
        @Override
        public bookings mapRow(ResultSet rs, int rowNum) throws SQLException {
            bookings booking = new bookings();
            booking.setBid(rs.getInt("bid")); // Assuming you have a bid column
            booking.setPlaceId(rs.getLong("placeId"));
            return booking;
        }
    }
    public static class mapper implements RowMapper<bookings> {
        @Override
        public bookings mapRow(ResultSet rs, int rowNum) throws SQLException {
            bookings booking = new bookings();
            booking.setBid(rs.getInt("bid"));
            booking.setPlaceId(rs.getLong("placeId"));
            booking.setHid(rs.getInt("Hid"));
            booking.setRid(rs.getInt("RoomID"));
            booking.setUsername(rs.getString("username"));
            booking.setOperatorId(rs.getLong("OperatorId"));
            booking.setTransportTypeId(rs.getLong("transportTypeId"));
            booking.setServiceId(rs.getLong("serviceId"));
            booking.setDay(rs.getDate("bookingDate"));
            return booking;
        }
    }
    @Override
    public List<bookings> allbookings() {
    try {
        String sql = "SELECT b.bid AS bid, "
                + "b.placeId AS placeId, "
                + "p.operatorId AS TransportOperatorID, "
                + "p.paymentid AS PaymentID, "
                + "p.mode_of_payment AS PaymentMode, "
                + "p.status AS PaymentStatus, "
                + "p.payment_date AS PaymentDate, "
                + "p.hid AS HotelID, "
                + "p.RoomID AS RoomID, "
                + "r.description AS ReviewText, "
                + "r.rating AS ReviewRating "
                + "FROM bookings b "
                + "LEFT JOIN reviews r ON r.username = b.uid AND b.placeId = r.placeId "
                + "LEFT JOIN payments p ON b.bid = p.bid "
                + "WHERE b.uid = ?";


        // Logging the query and parameters
        System.out.println("Executing query: " + sql);
        System.out.println("With parameter: " + getUserName());

        return jdbcTemplate.query(sql, new bookingMapper(), getUserName());
    } catch (BadSqlGrammarException e) {
        // Log SQL error
        System.err.println("SQL Error: " + e.getMessage());
        e.printStackTrace();
        return new ArrayList<>();
    } catch (Exception e) {
        // Log any other errors
        e.printStackTrace();
        return new ArrayList<>();
    }
}


    @Override
    public Integer addBooking(Long placeId,LocalDate date) {
        try {
            String sql = "INSERT INTO bookings (placeId, uid, bstatus, bookingdate) VALUES (?, ?, ?, ?)";
            String username = getUserName();
            java.sql.Date today = java.sql.Date.valueOf(date);
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[] { "bookingId" });
                ps.setLong(1, placeId);
                ps.setString(2, username);
                ps.setString(3, "confirmed");
                ps.setDate(4, today);
                return ps;
            }, keyHolder);
            Integer generatedBookingId = keyHolder.getKey().intValue();
            System.out.println("Generated Booking ID: " + generatedBookingId);
            return generatedBookingId;

        } catch (Exception e) {
            e.printStackTrace();
            return 0; // Log the exception
        }
    }
    public List<bookings> allbooks(){
        String sql = "SELECT rb.Hid AS Hid, rb.RoomID AS RoomID, tb.OperatorId AS OperatorId, " +
        "tb.transportTypeId AS transportTypeId, rb.bid AS bid, tb.serviceId AS serviceId, " +
        "b.placeId AS placeId, b.uid AS username, b.bookingdate AS bookingDate " +
        "FROM bookings b " +
        "LEFT JOIN Room_Bookings rb ON rb.bid = b.bid " +
        "LEFT JOIN TransportBookings tb ON rb.bid = tb.bid " +
        "WHERE b.uid = ?"; // Replace '?' with username parameter
        try{
            return jdbcTemplate.query(sql,new mapper(),getUserName());
        }
        catch(Exception e){
            System.err.println("SQL Error: " + e.getMessage());
            // e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public boolean findBidUser(Integer bid){
        try {
            String sql3 = "select * from bookings where bid = ? and uid = ?";
            int rows = jdbcTemplate.query(sql3,new bookingMapper(),bid,getUserName()).size();
            return rows>0;
        } catch (Exception e) {
            System.err.println("SQL Error: " + e.getMessage());
            return false;
        }
    }
    
    public float placeCost(Integer bid){
        try {
            float cost = jdbcTemplate.queryForObject("Select cost from travelPlaces as t ,bookings as b where b.bid = ? AND t.placeId = b.placeId",float.class,bid);
            System.out.println(cost+"place");
            return cost;
        } catch (Exception e) {

        }
        return 0.0f;
    }
}
