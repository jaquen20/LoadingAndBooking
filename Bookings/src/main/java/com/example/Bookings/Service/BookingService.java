package com.example.Bookings.Service;

import com.example.Bookings.DTO.BookingDTO;
import com.example.Bookings.DTO.LoadDTO;
import com.example.Bookings.Enum.BookingStatus;
import com.example.Bookings.Enum.Status;
import com.example.Bookings.Exceptions.BookingStatusException;
import com.example.Bookings.Exceptions.LoadNotFoundException;
import com.example.Bookings.Model.Booking;
import com.example.Bookings.Model.Load;
import com.example.Bookings.Repository.BookingRepository;
import com.example.Bookings.Repository.LoadRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private LoadRepository loadRepository;


    public Booking createNewBooking(BookingDTO bookingBody) {
        Load load=loadRepository.findById(bookingBody.getLoadId()).orElseThrow(
                ()->new LoadNotFoundException("Load with id "+bookingBody.getLoadId()+" not found"));
        if (load.getStatus()== Status.CANCELLED){
            throw  new BookingStatusException("Load is already canceled");
        }
        if (load.getStatus()== Status.BOOKED){
            throw  new BookingStatusException("Load is already booked");
        }
        Booking booking=new Booking();
        booking.setId(UUID.randomUUID());
        booking.setLoadId(bookingBody.getLoadId());
        booking.setTransporterId(bookingBody.getTransporterId());
        booking.setRequestedAt(bookingBody.getRequestedAt());
        booking.setComment(bookingBody.getComment());
        booking.setProposedRate(bookingBody.getProposedRate());
        booking.setBookingStatus(BookingStatus.PENDING);

        Booking newSavedBooking=bookingRepository.save(booking);
            load.setStatus(Status.BOOKED);
            loadRepository.save(load);
            return newSavedBooking;
    }
    public List<BookingDTO> getBookingsByTransportIdAndShipperId(String transporterId, String shipperId) {
        List<Booking> bookingList= bookingRepository.findByTransporterId(transporterId);
        List<Load> loadList=loadRepository.findByShipperId(shipperId);
//        List<Booking> bookingList1=new ArrayList<>();
        for (Load load:loadList){
            Booking booking=bookingRepository.findByLoadId(load.getId());
            bookingList.add(booking);
        }
        return convertToDto(bookingList);
    }

    public List<BookingDTO> getBookingsByTransportationId(String transporterId) {
        List<Booking> bookingList= bookingRepository.findByTransporterId(transporterId);
        return convertToDto(bookingList);
    }

    public List<BookingDTO> getBookingsByShipperId(String shipperId) {
        List<Load> loadList=loadRepository.findByShipperId(shipperId);
        List<Booking> bookingList=new ArrayList<>();
        for (Load load:loadList){
        Booking booking=bookingRepository.findByLoadId(load.getId());
        bookingList.add(booking);
        }
        return convertToDto(bookingList);
    }
    public List<BookingDTO> getAllBooking() {
        List<Booking> booking= bookingRepository.findAll();
        return convertToDto(booking);
    }

    public BookingDTO getBookingById(UUID bookingId) {
        Booking booking=bookingRepository.findById(bookingId).orElseThrow(()->new BookingStatusException("Booking with id "+bookingId+" not found"));
        return ConvertToBookingDTO(booking);
    }

    public BookingDTO updateBooking(UUID bookingId, BookingDTO bookingDTO) {
       Booking booking=bookingRepository.findById(bookingId).orElseThrow(()->new BookingStatusException("Booking with id "+bookingId+" not found"));
       booking.setProposedRate(bookingDTO.getProposedRate());
       if (bookingDTO.getTransporterId()!=null)booking.setTransporterId(bookingDTO.getTransporterId());
       if (bookingDTO.getComment()!=null)booking.setComment(bookingDTO.getComment());
       bookingRepository.save(booking);
       return bookingDTO;
    }

    public void deleteBookingById(UUID bookingId) {
        Booking booking=bookingRepository.findById(bookingId).orElseThrow(()->new BookingStatusException("Booking with id "+bookingId+" not found"));
        Load load=loadRepository.findById(booking.getLoadId()).orElseThrow(()->new LoadNotFoundException("Load with id "+booking.getLoadId()+" not found"));
        load.setStatus(Status.CANCELLED);
        loadRepository.save(load);
        bookingRepository.delete(booking);
    }


//    public List<BookingDTO> searchBooking(String shipperId, String transporterId, BookingStatus bookingStatus) {
//        if (shipperId==null&&transporterId==null&&bookingStatus==null){
//            return bookingRepository.findAll().stream()
//                    .map(this::ConvertToBookingDTO).collect(Collectors.toList());
//        }
//        Specification<Booking> spec = (root, query, cb) -> {
//            List<Predicate> predicates = new ArrayList<>();
//            if (shipperId!=null) predicates.add(cb.equal(root.get("shipperId"), shipperId));
//            if (transporterId!=null) predicates.add(cb.equal(root.get("transporterId"),transporterId));
//            if (bookingStatus!=null) predicates.add(cb.equal(root.get("bookingStatus"),bookingStatus));
//            return cb.and(predicates.toArray(new Predicate[0]));
//        };
//        return bookingRepository.findAll(spec).stream().map(this::ConvertToBookingDTO).collect(Collectors.toList());
//    }

    private List<BookingDTO> convertToDto(List<Booking> bookingList){
        List<BookingDTO> bookingDTOList=new ArrayList<>();
        for (Booking booking:bookingList){
            bookingDTOList.add(ConvertToBookingDTO(booking));
        }
        return bookingDTOList;
    }

    private BookingDTO ConvertToBookingDTO(Booking booking) {
        BookingDTO bookingDTO=new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setLoadId(booking.getLoadId());
        bookingDTO.setTransporterId(booking.getTransporterId());
        bookingDTO.setRequestedAt(booking.getRequestedAt());
        bookingDTO.setComment(booking.getComment());
        bookingDTO.setProposedRate(booking.getProposedRate());
        bookingDTO.setStatus(booking.getBookingStatus());
        return bookingDTO;
    }

    public BookingDTO acceptBooking(UUID bookingId) {
        Booking booking=bookingRepository.findById(bookingId).orElseThrow(()->new BookingStatusException("booking  not found"));
        if (booking.getBookingStatus()!=BookingStatus.PENDING){
            throw new BookingStatusException("only pending status accepted");
        }
        booking.setBookingStatus(BookingStatus.ACCEPTED);
        Booking booking1=bookingRepository.save(booking);
        return ConvertToBookingDTO(booking1);
    }

    public BookingDTO rejectBooking(UUID bookingId) {
        Booking booking=bookingRepository.findById(bookingId).orElseThrow(()->new BookingStatusException("booking  not found"));
        if (booking.getBookingStatus()!=BookingStatus.PENDING){
            throw new BookingStatusException("status must be pending to reject");
        }
        booking.setBookingStatus(BookingStatus.REJECTED);
        Booking booking1=bookingRepository.save(booking);
        return ConvertToBookingDTO(booking1);
    }
}
