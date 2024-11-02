package com.example.Travel.And.Tourisum.controller.usercontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl.bookingiml;
import com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl.roomimpl;
import com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl.tbookingimpl;
import com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl.travelimpl;
import com.example.Travel.And.Tourisum.models.RoomBookings;
import com.example.Travel.And.Tourisum.models.Rooms;
import com.example.Travel.And.Tourisum.models.datamodel;
import com.example.Travel.And.Tourisum.models.transport;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.time.*;



@Controller
@RequestMapping("/Booking/Payment")
public class paymentcont {
    public String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !authentication.getName().equals("anonymousUser")) {
            return authentication.getName(); 
        }
        return null;
    }
    @Autowired
    tbookingimpl tbookingimpl;
    @Autowired
    roomimpl roomimpl;
    @Autowired
    travelimpl travelimpl;
    @Autowired
    bookingiml bookingiml;

    @PostMapping("")
    public String Detaisl(@RequestParam Long placeId,@RequestParam Integer hid,@RequestParam Integer rid,@RequestParam Float cost,@RequestParam Long tid,@RequestParam LocalDate date,Model model) {
        model.addAttribute("placeCost",travelimpl.getcost(placeId));
        model.addAttribute("roomCost", roomimpl.getcost(rid));
        model.addAttribute("travelCost",cost);
        return "payment";
    }
    @GetMapping("/place/{placeId}")
    public String placeprocced(@PathVariable Long placeId,Model model,@RequestParam Integer bid) {
        model.addAttribute("data",travelimpl.findbyId(placeId));
        model.addAttribute("bid", bid);
        return "explore";
    }
    @PostMapping("/{placeId}/done")
    public String placeProcced(@RequestParam Long placeId,Model model,@RequestParam LocalDate startDate) {
        Integer bid = bookingiml.addBooking(placeId,startDate );
        String redirectUrl = "redirect:/Booking/Payment/place/"+placeId+"?bid="+bid;
        return redirectUrl;
    }
    @RequestMapping("/{bid}/room")
    public String getMethodName(datamodel datamodel,Model model,@RequestParam(required = false) Long RBID) {
        Rooms room=roomimpl.findroom(datamodel.getHid(),datamodel.getRid());
        if(RBID == null){
            RBID = 0L;
        }
        model.addAttribute("datamodel", datamodel);
        model.addAttribute("room",room);
        model.addAttribute("RBID", RBID);
        return "RPayment";
    }
    @RequestMapping("/room/done")
    public String postMethodName(datamodel datamodel,RedirectAttributes redirectAttributes) {
        if (datamodel == null) {
            return "redirect:/error"; // or whatever logic you want
        }
        redirectAttributes.addFlashAttribute("datamodel", datamodel);
        RoomBookings roomBookings = new RoomBookings();
        roomBookings.setRoomId(datamodel.getRid());
        roomBookings.setBid(datamodel.getBid());
        roomBookings.setStartDate(datamodel.getStartDate());
        roomBookings.setBookingstatus("Booked");
        roomBookings.setTotalDays(1);
        roomBookings.setHid(datamodel.getHid());
        Long rbid = roomimpl.book(roomBookings);
        String redirectUrl = "redirect:/Booking/Payment/"+datamodel.getBid()+"/room?RBID="+rbid;
        return redirectUrl;
    }
    @RequestMapping("/{tid}/transport")
    public String tronsportbook(datamodel datamodel,Model model,@RequestParam(required = false) Long TBID) {
        if(TBID == null){
            TBID =0L;
            transport transport = tbookingimpl.findTransport(datamodel.getPlaceId(),datamodel.getTid(),datamodel.getTypeId());
            model.addAttribute("transport", transport);
        }
        model.addAttribute("datamodel", datamodel);
        model.addAttribute("TBID", TBID);
        return "Tpayment";
    }
    @RequestMapping("/transport/done")
    public String tranportdone(datamodel datamodel,RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("datamodel", datamodel);
        transport transport = new transport();
        transport.setPlaceId(datamodel.getPlaceId());
        transport.setServiceId(datamodel.getSId());
        transport.setOperatorId(datamodel.getTid());
        transport.setTransportTypeId(datamodel.getTypeId());
        transport.setBid(datamodel.getBid());
        Long TBID=tbookingimpl.addBooking(transport);
        String redirectUrl = "redirect:/Booking/Payment/"+datamodel.getTid()+"/transport?TBID="+TBID;
        return redirectUrl;
    }
    

    
    
    
    
}
