package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Collection<Booking> findByBookerId(long bookerId);

    Collection<Booking> findByOwnerId(long ownerId);

    @Query("select bk " +
            "from Booking as bk " +
            "join bk.item as i " +
            "join bk.booker as b " +
            "join i.owner as o " +
            "where b.id = :bookerId and bk.end < :now " +
            "group by bk.start " +
            "order by bk.start desc ")
    Collection<Booking> findAllBookingsByBookerAndPast(@Param("bookerId") long bookerId, @Param("now") LocalDateTime now);

    @Query("select bk " +
            "from Booking as bk " +
            "join bk.item as i " +
            "join bk.booker as b " +
            "join i.owner as o " +
            "where o.id = :ownerId and bk.end < :now " +
            "group by bk.start " +
            "order by bk.start desc ")
    Collection<Booking> findAllBookingsByOwnerAndPast(@Param("ownerId") long ownerId, @Param("now") LocalDateTime now);

    @Query("select bk " +
            "from Booking as bk " +
            "join bk.item as i " +
            "join bk.booker as b " +
            "join i.owner as o " +
            "where b.id = :bookerId and bk.start > :now " +
            "group by bk.start " +
            "order by bk.start desc ")
    Collection<Booking> findAllBookingsByBookerAndFuture(@Param("bookerId") long bookerId, @Param("now") LocalDateTime now);

    @Query("select bk " +
            "from Booking as bk " +
            "join bk.item as i " +
            "join bk.booker as b " +
            "join i.owner as o " +
            "where o.id = :ownerId and bk.start > :now " +
            "group by bk.start " +
            "order by bk.start desc ")
    Collection<Booking> findAllBookingsByOwnerAndFuture(@Param("ownerId") long ownerId, @Param("now") LocalDateTime now);

    @Query("select bk " +
            "from Booking as bk " +
            "join bk.item as i " +
            "join bk.booker as b " +
            "join i.owner as o " +
            "where b.id = :bookerId and bk.start < :now and bk.end > :now " +
            "group by bk.start " +
            "order by bk.start desc ")
    Collection<Booking> findAllBookingsByBookerAndCurrent(@Param("bookerId") long bookerId, @Param("now") LocalDateTime now);

    @Query("select bk " +
            "from Booking as bk " +
            "join bk.item as i " +
            "join bk.booker as b " +
            "join i.owner as o " +
            "where o.id = :ownerId and bk.start < :now and bk.end > :now " +
            "group by bk.start " +
            "order by bk.start desc ")
    Collection<Booking> findAllBookingsByOwnerAndCurrent(@Param("ownerId") long ownerId, @Param("now") LocalDateTime now);

    Collection<Booking> findByBookerIdAndStatus(long bookerId, String status);

    Collection<Booking> findByOwnerIdAndStatus(long ownerId, String status);

    @Query("select bk " +
            "from Booking as bk " +
            "join bk.item as i " +
            "join bk.booker as b " +
            "where i.id = :itemId ")
    Booking findByItemId(@Param("itemId") long itemId);

    @Query("select bk " +
            "from Booking as bk " +
            "join bk.item as i " +
            "join bk.booker as b " +
            "where i.id = :itemId and bk.end < :now and bk.status = :status ")
    Booking findByItemIdPast(@Param("itemId") long itemId,
                             @Param("now") LocalDateTime now,
                             @Param("status") Status status);

    @Query("select bk " +
            "from Booking as bk " +
            "join bk.item as i " +
            "join bk.booker as b " +
            "where i.id = :itemId and bk.start > :now and bk.status = :status ")
    Booking findByItemIdFuture(@Param("itemId") long itemId,
                               @Param("now") LocalDateTime now,
                               @Param("status") Status status);

    @Query("select bk " +
            "from Booking as bk " +
            "join bk.item as i " +
            "join bk.booker as b " +
            "where b.id = :userId and bk.end < :now ")
    Optional<Booking> findByUserId(@Param("userId") long userId, @Param("now") LocalDateTime now);
    //    @Query("select bk " +
//            "from Booking as bk " +
//            "join bk.item as i " +
//            "join bk.booker as b " +
//            "join i.owner as o " +
//            "where o.id = :ownerId and bk.status = :status " +
//            "group by bk.start " +
//            "order by bk.start desc ")
//    Collection<Booking> findAllBookingByOwnerAndStatus(@Param("ownerId") long ownerId, @Param("status") String status);
//    @Query("select bk " +
//            "from Booking as bk " +
//            "join bk.item as i " +
//            "join bk.booker as b " +
//            "join i.owner as o " +
//            "where o.id = :ownerId " +
//            "group by bk.start " +
//            "order by bk.start desc ")
//    Collection<Booking> findAllBookingByOwner(@Param("ownerId") long ownerId);
}