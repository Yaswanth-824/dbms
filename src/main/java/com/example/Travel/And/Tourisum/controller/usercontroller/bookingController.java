package com.example.Travel.And.Tourisum.controller.usercontroller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl.bookingiml;
import com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl.hotelimpl;
import com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl.reviewImpl;
import com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl.roomimpl;
import com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl.tbookingimpl;
import com.example.Travel.And.Tourisum.models.Hotels;
import com.example.Travel.And.Tourisum.models.RoomBookings;
import com.example.Travel.And.Tourisum.models.Rooms;
// import com.example.Travel.And.Tourisum.models.Rooms;
import com.example.Travel.And.Tourisum.models.bookings;
import com.example.Travel.And.Tourisum.models.datamodel;
import com.example.Travel.And.Tourisum.models.payment;
import com.example.Travel.And.Tourisum.models.review;
import com.example.Travel.And.Tourisum.models.transport;
// import com.example.Travel.And.Tourisum.models.transport;
import com.example.Travel.And.Tourisum.service.userservice.bookingservice;
import com.example.Travel.And.Tourisum.service.userservice.paymentservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@RequestMapping("/Booking")
public class bookingController {
    @Autowired
    bookingservice bookingservice;
    @Autowired
    bookingiml bookingiml;
    @Autowired
    reviewImpl reviewImpl;
    @Autowired
    tbookingimpl tbookingimpl;
    @Autowired
    hotelimpl hotelimpl;
    @Autowired
    roomimpl roomimpl;
    @Autowired
    paymentservice payments;
    @GetMapping("")
    public String allbookings(Model model) {
        List<bookings> data = bookingservice.allbookings();
        model.addAttribute("data", data);
        return "bookings";
    }
    @RequestMapping("/place/{placeId}")
    public String newBooking(@PathVariable Long placeId,@RequestParam Integer bid,Model model,@RequestParam(required = false) LocalDate startDate) {
        // Long bid=bookingiml.addBooking(placeId); // booking done for that place
        List<Hotels> hotel= hotelimpl.findbyId(placeId);
        model.addAttribute("hotel", hotel);
        model.addAttribute("bid", bid);
        System.out.println("Generated Booking ID: " + bid);
        model.addAttribute("placeId", placeId);
        if (startDate == null) {
            startDate = LocalDate.now().plus(2, ChronoUnit.DAYS);
        }
        model.addAttribute("startDate", startDate);
        // return "hotel";
        // if (true) {
        //     // Redirect to the booking page with an error message
        //     return "redirect:/place/" + placeId + "?paymentError=true";
        // }
        return "hotel";
    }
    @GetMapping("/{placeId}/hotel/{hid}")
    public String hbook(@PathVariable Long placeId,Model model,@RequestParam Integer bid,@PathVariable Integer hid,@RequestParam LocalDate startDate,@ModelAttribute datamodel datamodel){
        List<Rooms> rooms = roomimpl.findById(hid);
        
        model.addAttribute("rooms", rooms);          // Pass the list of rooms to the template
        datamodel.setHid(hid);
        datamodel.setPlaceId(placeId);
        datamodel.setStartDate(startDate);
        datamodel.setBid(bid);
        model.addAttribute("datamodel",datamodel);
        return "room";
    }
    @GetMapping("/{placeId}/transport")
    public String transportdetails(@PathVariable Long placeId,datamodel datamodel,@RequestParam Integer bid,Model model) {
        datamodel.setPlaceId(placeId);
        datamodel.setBid(bid);
        List<transport> ans = tbookingimpl.findTransportOperatorsByPlaceId(placeId);
        model.addAttribute("tranports", ans);
        model.addAttribute("datamodel",datamodel);
        return "tranport";
    }
    @GetMapping("/{placeId}/{hid}/{rid}/{tid}/paytment/pay")
    public String paymentdetails(@PathVariable Long placeId,@RequestParam LocalDate startDate,
    @PathVariable Integer hid,@PathVariable Integer rid,@RequestParam Long tid) {
        // trransport navail
        payment payment = new payment();
        payment.setRoomID(rid);
        payment.setHid(hid);
        payment.setMode_of_payment("UPI");
        payment.setOperatorId(tid);
        payment.setPayment_date(LocalDate.now());
        // boolean success = payments.bookingProgress(placeId,startDate,payment);
        System.out.println("Start Date: " + startDate);
        if(true){
            return "redirect:/Booking";
        }
        return new String();
    }
    
    @GetMapping("/Addreview/{placeId}")
    public String postMethodName(@PathVariable Long placeId,Model model) {
        review newReview = new review();
        newReview.setPlaceId(placeId); // Set the placeId from the URL
        model.addAttribute("review", newReview);
        return "review";
    }
    @PostMapping("/Addreview")
    public String add(@ModelAttribute review review) {
        reviewImpl.addReview(review);
        return "redirect:/Home/Booking";
    }
    @GetMapping("/temp")
    public String temp(Model model) {
        List<Hotels> hotel= hotelimpl.findbyId(1L);
        model.addAttribute("hotel", hotel);
        return "hotel";
    }
    
    
    
    
}
