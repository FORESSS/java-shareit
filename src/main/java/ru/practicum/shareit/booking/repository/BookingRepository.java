package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.Collection;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId")
    Collection<Booking> findAllByBookerId(@Param("bookerId") long bookerId);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.start < :cur AND b.end > :cur2 ORDER BY b.start DESC")
    Collection<Booking> findCurrentBookings(@Param("bookerId") long bookerId, @Param("cur") LocalDateTime cur, @Param("cur2") LocalDateTime cur2);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.end < :cur ORDER BY b.start DESC")
    Collection<Booking> findPastBookings(@Param("bookerId") long bookerId, @Param("cur") LocalDateTime cur);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.start > :cur ORDER BY b.start DESC")
    Collection<Booking> findUpcomingBookings(@Param("bookerId") long bookerId, @Param("cur") LocalDateTime cur);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.status = :status ORDER BY b.start DESC")
    Collection<Booking> findBookingsByStatus(@Param("bookerId") long bookerId, @Param("status") BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId ORDER BY b.start DESC")
    Collection<Booking> findAllByItemOwnerId(@Param("ownerId") long ownerId);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId AND b.start < :cur AND b.end > :cur2 ORDER BY b.start DESC")
    Collection<Booking> findCurrentItemBookings(@Param("ownerId") long ownerId, @Param("cur") LocalDateTime cur, @Param("cur2") LocalDateTime cur2);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId AND b.end < :cur ORDER BY b.start DESC")
    Collection<Booking> findPastItemBookings(@Param("ownerId") long ownerId, @Param("cur") LocalDateTime cur);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId AND b.start > :cur ORDER BY b.start DESC")
    Collection<Booking> findUpcomingItemBookings(@Param("ownerId") long ownerId, @Param("cur") LocalDateTime cur);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId AND b.status = :status ORDER BY b.start DESC")
    Collection<Booking> findItemBookingsByStatus(@Param("ownerId") long ownerId, @Param("status") BookingStatus status);

    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.booker.id = :bookerId AND b.end < :end AND b.status <> :status")
    boolean existsByBookerIdAndEndBeforeAndStatusNot(@Param("bookerId") long bookerId, @Param("end") LocalDateTime end, @Param("status") BookingStatus status);
}