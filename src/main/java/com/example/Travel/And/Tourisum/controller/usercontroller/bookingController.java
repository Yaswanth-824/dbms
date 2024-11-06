package com.example.Travel.And.Tourisum.controller.usercontroller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// import com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl.ancellationimpl;
import com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl.bookingiml;
import com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl.cancellationImpl;
import com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl.hotelimpl;
import com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl.reviewImpl;
import com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl.roomimpl;
import com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl.tbookingimpl;
import com.example.Travel.And.Tourisum.models.Hotels;
import com.example.Travel.And.Tourisum.models.Rooms;
import com.example.Travel.And.Tourisum.models.bookings;
import com.example.Travel.And.Tourisum.models.cancellation;
import com.example.Travel.And.Tourisum.models.datamodel;
import com.example.Travel.And.Tourisum.models.review;
import com.example.Travel.And.Tourisum.models.transport;
import com.example.Travel.And.Tourisum.service.userservice.bookingservice;
import com.example.Travel.And.Tourisum.service.userservice.paymentservice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/Booking")
public class bookingController {
    public String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !authentication.getName().equals("anonymousUser")) {
            return authentication.getName();
        }
        return null;
    }
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
    cancellationImpl cancellationImpl;
    @Autowired
    paymentservice payments;
    @GetMapping("/test")
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
        model.addAttribute("placeId", placeId);
        if (startDate == null) {
            startDate = LocalDate.now().plus(2, ChronoUnit.DAYS);
        }
        model.addAttribute("startDate", startDate);
        return "hotel";
    }
    @GetMapping("/{placeId}/hotel/{hid}")
    public String hbook(@PathVariable Long placeId,Model model,@RequestParam Integer bid,@PathVariable Integer hid,@RequestParam LocalDate startDate,datamodel datamodel){
        List<Rooms> rooms = roomimpl.findById(hid,startDate);
        model.addAttribute("rooms", rooms);
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
    @GetMapping("/transport/skip")
    public String skipTransport(@RequestParam Integer bid, RedirectAttributes redirectAttributes,@RequestParam(required = false) boolean delete) {
        if (tbookingimpl.addbid(bid)) {
            redirectAttributes.addFlashAttribute("message", "Transport booking skipped successfully!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Transport is skipped already for Booking");
        }
        if(delete){
            return "redirect:/Booking";
        }
        System.out.println("Succes in Skiiping transport");
        return "redirect:/Booking";
    }
    @GetMapping("/room/skip")
    public String getMethodName(@RequestParam Integer bid,@RequestParam Long placeId,RedirectAttributes redirectAttributes,@RequestParam(required = false) boolean delete) {
        if (roomimpl.addbid(bid)) {
            redirectAttributes.addFlashAttribute("message", "Transport booking skipped successfully!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Transport is skipped already for Booking");
        }
        System.out.println("Succes in Skiiping Room");
        if(delete){
            return "redirect:/Booking";
        }
        return "redirect:/Booking/"+placeId+"/transport"+"?bid="+bid;
    }
    
    @GetMapping("/Addreview/{placeId}")
    public String postMethodName(@PathVariable Long placeId,Model model,@RequestParam Integer bid) {
        if(bid !=0){
            review newReview = new review();
            newReview.setPlaceId(placeId);
            newReview.setUsername(getUserName());
            newReview.setBid(bid); // Set the placeId from the URL
            model.addAttribute("review", newReview);
        }
        try{
            return "review";
        }
        catch(Exception e){
            return "redirect:/Booking";
        }
    }

    
    @PostMapping("/Addreview")
    public String add(@ModelAttribute review review) {
        reviewImpl.addReview(review);
        return "redirect:/Booking";
    }
    @GetMapping("/temp")
    public String temp(Model model) {
        List<Hotels> hotel= hotelimpl.findbyId(1L);
        model.addAttribute("hotel", hotel);
        return "hotel";
    }
    @GetMapping("")
    public String books(Model model){
        String message = (String) model.asMap().get("message");
        model.addAttribute("message", message);
        System.out.println(message);
        List<bookings> ans = bookingiml.allbooks();
        for (bookings booking : ans) {
            booking.setDate(booking.getDay().toLocalDate()); // Ensure Booking class has a LocalDate field for day
        }
        System.out.println(LocalDate.now());
        model.addAttribute("books",ans);
        try{
            return "temp";
        }
        catch(Exception e){
            return "redirect:/";
        }
    }
    @GetMapping("/Skip/all")
    public String skipAll(@RequestParam Integer bid,@RequestParam Long placeId) {
        roomimpl.addbid(bid);
        tbookingimpl.addbid(bid);
        return "redirect:/Booking";
    }
    @GetMapping("/{bid}/cancel")
    public String getMethodName(@PathVariable Integer bid,Model model,RedirectAttributes redirectAttributes) {
        if(!bookingiml.findBidUser(bid)){
            redirectAttributes.addFlashAttribute("message", "Booking ID not found for the user.");
            return "redirect:/Booking";
        }

        cancellation cancellationForm = new cancellation();
        cancellationForm.setBid(bid);
        cancellationForm.setUsername(getUserName());
        cancellationForm.setCanceldate(LocalDate.now());
        float cost = bookingiml.placeCost(bid);
        cost += tbookingimpl.transcost(bid);
        cost  += roomimpl.roomcost(bid);
        cancellationForm.setRefundableamount(cost*0.8f);
        float tax=cost*0.2f;
        // cancellationForm.setRefundableamount(1000.0f);
        model.addAttribute("cancellationForm", cancellationForm);
        model.addAttribute("tax", tax);
        model.addAttribute("amountPaid", cost);
        return "cancelForm";
    }
    @PostMapping("/cancel/done")
    public String cancellation(cancellation cancellation) {
        try {
            System.out.println(cancellation.getBid());
            System.out.println(cancellation.getRefundableamount());
            System.out.println(cancellation.getReason());
            System.out.println(cancellation.getCanceldate());
            cancellation.setCanceldate(LocalDate.now());
            System.out.println(cancellation.getUsername());
            // cancellationimpl.addCancellation(cancellation);
            cancellationImpl.addCancellation(cancellation);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return "redirect:/Booking";
    }
    
    
    // @GetMapping("/{placeId}/{hid}/{rid}/{tid}/paytment/pay")
    // public String paymentdetails(@PathVariable Long placeId,@RequestParam LocalDate startDate,
    // @PathVariable Integer hid,@PathVariable Integer rid,@RequestParam Long tid) {
    //     // trransport navail
    //     payment payment = new payment();
    //     payment.setRoomID(rid);
    //     payment.setHid(hid);
    //     payment.setMode_of_payment("UPI");
    //     payment.setOperatorId(tid);
    //     payment.setPayment_date(LocalDate.now());
    //     // boolean success = payments.bookingProgress(placeId,startDate,payment);
    //     System.out.println("Start Date: " + startDate);
    //     if(true){
    //         return "redirect:/Booking";
    //     }
    //     return new String();
    // }
    // @GetMapping("/cancel/{bid}")
    // public String cancel(@RequestParam Integer bid) {
        
    // }
}
